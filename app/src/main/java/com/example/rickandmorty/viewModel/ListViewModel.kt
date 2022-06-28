package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _lceFlow = MutableStateFlow<LceState<List<Character>>>(LceState.Loading)
    val lceFlow: Flow<LceState<List<Character>>> = _lceFlow.asStateFlow()

    private val loadMoreFlow = MutableSharedFlow<Unit>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var isLoading = false
    private var currentPage = 1

    val dataFlow = loadMoreFlow
        .filter { !isLoading}
        .mapLatest {
            isLoading = true
            getCharactersUseCase(currentPage)
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
        }
        .onEach { isLoading = false }
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

    init {
        onLoadMore()
    }


    fun onLoadMore() {
        loadMoreFlow.tryEmit(Unit)
    }

    fun onRefresh(onLoadingFinished: () -> Unit = {}) {
        currentPage = 1
        loadMoreFlow.tryEmit(Unit)
        onLoadingFinished()
    }

}