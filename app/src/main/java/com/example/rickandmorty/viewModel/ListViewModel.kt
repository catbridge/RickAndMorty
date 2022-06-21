package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterLocalRepository
import com.example.rickandmorty.domain.usecase.GetAllCharactersFromDBUseCase
import com.example.rickandmorty.domain.usecase.GetCharactersUseCase
import com.example.rickandmorty.domain.usecase.GetSomeCharactersFromDBUseCase
import com.example.rickandmorty.domain.usecase.InsertCharacterToDBUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val insertCharacterToDBUseCase: InsertCharacterToDBUseCase,
    private val getSomeCharactersFromDBUseCase: GetSomeCharactersFromDBUseCase
) : ViewModel() {

    private val loadCharactersFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    private var isLoading = false
    private var currentPage = 1

    val dataFlow = loadCharactersFlow
        .filter { !isLoading }
        .map {
            runCatching { getCharactersUseCase(currentPage) }
                .apply { isLoading = false }
                .fold(
                    onSuccess = {
                        insertCharacterToDBUseCase(it)
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
            val list = getSomeCharactersFromDBUseCase()
            if (list.isNotEmpty()) {
                emit(getSomeCharactersFromDBUseCase())
            } else {
                onRefresh()
            }
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