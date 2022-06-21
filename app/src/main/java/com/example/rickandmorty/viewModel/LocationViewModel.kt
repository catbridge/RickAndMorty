package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.usecase.GetLocationsUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class LocationViewModel(
    private val getLocationsUseCase: GetLocationsUseCase,
) : ViewModel() {

    private val loadLocationsFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var isLoading = false
    private var currentPage = 1

    val dataFlow = loadLocationsFlow
        .filter { !isLoading }
        .map {
            getLocationsUseCase(currentPage)
                .apply { isLoading = false }
                .fold(
                    onSuccess = {
                        currentPage++
                        it
                    },
                    onFailure = {
                        emptyList()
                    }
                )
        }
        .onEach { isLoading }
        .runningReduce { accumulator, value -> accumulator + value }
        .onStart {
            onRefresh()
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )


    fun onLoadMore() {
        loadLocationsFlow.tryEmit(LoadState.LOAD_MORE)
    }

    fun onRefresh(onLoadingFinished: () -> Unit = {}) {
        loadLocationsFlow.tryEmit(LoadState.REFRESH)
        onLoadingFinished()
    }

    enum class LoadState {
        LOAD_MORE, REFRESH
    }

}