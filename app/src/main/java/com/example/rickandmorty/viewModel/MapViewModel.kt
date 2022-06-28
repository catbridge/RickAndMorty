package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Country
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.flow.*

class MapViewModel(
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    val countryFlow = flow {
        val state = getCountriesUseCase().fold(
            onSuccess = {
                LceState.Content(it)
            },
            onFailure = {
                LceState.Error(it)
            }
        )
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = LceState.Loading)

}