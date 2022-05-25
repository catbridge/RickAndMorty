package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.repository.RickAndMortyRepository
import com.example.rickandmorty.room.CharacterDao
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class ListViewModel(
    private val characterRepository: RickAndMortyRepository,
    private val characterDao: CharacterDao
) : ViewModel() {

    private val loadCharactersFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var isLoading = false
    private var currentPage = 1

    val dataFlow = loadCharactersFlow
        .filter { !isLoading }
        .map {
            runCatching { characterRepository.getAllCharacters(currentPage) }
                .apply { isLoading = false }
                .fold(
                    onSuccess = {
                        characterDao.insertList(it)
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
            if (characterDao.getAllCharacters().isNotEmpty()) {
                emit((characterDao.getCharacters(PAGE_SIZE, 0)))
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
        onLoadingFinished()
        loadCharactersFlow.tryEmit(LoadState.REFRESH)
    }

    enum class LoadState {
        LOAD_MORE, REFRESH
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}