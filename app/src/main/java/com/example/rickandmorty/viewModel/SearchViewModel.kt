package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.domain.usecase.GetCharactersByNameUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val useCase: GetCharactersByNameUseCase
) : ViewModel() {

    private var isLoading = false
    private var currentPage = 1

    private val queryFlow = MutableStateFlow("")

    private val loadMoreFlow = MutableSharedFlow<Unit>(
        replay =  1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


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

    private fun charactersDataFlow(query: String): Flow <LceState<List<Character>>> {
        return loadMoreFlow
            .filter { !isLoading }
            .mapLatest {
                isLoading = true
                useCase(currentPage, query)
                    .onSuccess { currentPage++ }
                    .fold(
                        onSuccess = {
                            LceState.Content(it)
                        },
                        onFailure = {
                            if(currentPage == 1){
                                LceState.Error(it)
                            }else{
                                LceState.Content(emptyList())
                            }
                        }
                    )
            }.onEach { isLoading = false }
            .runningReduce { accumulator, value ->
                if (currentPage == 1 && value is LceState.Error) {
                    value
                } else {
                    when (accumulator) {
                        is LceState.Content -> {
                            LceState.Content(
                                accumulator.value + (value as? LceState.Content)?.value.orEmpty()
                            )
                        }
                        is LceState.Error -> {
                            (value as? LceState.Content) ?: accumulator
                        }
                        LceState.Loading -> accumulator
                    }
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                LceState.Loading
            )
    }

}


