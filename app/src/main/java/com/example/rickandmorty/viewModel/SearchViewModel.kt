package com.example.rickandmorty.viewModel

import android.app.DownloadManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.GetCharactersByNameUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val useCase: GetCharactersByNameUseCase
) : ViewModel() {

    private var isLoading = false
    private var currentPage = 1
    private val queryFlow = MutableStateFlow("")
    private val loadMoreFlow = MutableSharedFlow<Unit>(replay =  1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val dataFlow = queryFlow
        .debounce(1000)
        .onEach {
            currentPage = 1
            isLoading = false
        }
        .flatMapLatest { query ->
            if(query.isNotEmpty()) charactersDataFlow(query)
            else emptyFlow()

        }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)


    init {
        onLoadMore()
    }

    fun onQueryChanger(newQuery: String) {
        queryFlow.value = newQuery
    }

    fun onLoadMore() {
        loadMoreFlow.tryEmit(Unit)
    }

    private fun charactersDataFlow(query: String): Flow<List<Character>> {
        return loadMoreFlow
            .filter { !isLoading }
            .onEach { isLoading = true }
            .mapLatest {
                useCase(currentPage, query)
                    .onSuccess { currentPage++ }
                    .getOrDefault(emptyList())
            }
            .runningReduce { accumulator, value -> accumulator + value }
            .onEach { isLoading = false }
    }

}


