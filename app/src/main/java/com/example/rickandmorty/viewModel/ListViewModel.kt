package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val loadCharactersFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    private var isLoading = false
    private var currentPage = 1

    val dataFlow = loadCharactersFlow
        .filter { !isLoading }
        .map {
            getCharactersUseCase(currentPage)
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
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )


    fun onLoadMore() {
        loadCharactersFlow.tryEmit(LoadState.LOAD_MORE)
    }

    fun onRefresh(onLoadingFinished: () -> Unit = {}) {
        loadCharactersFlow.tryEmit(LoadState.REFRESH)
        onLoadingFinished()
    }

    enum class LoadState {
        LOAD_MORE, REFRESH
    }

}