package com.example.rickandmorty.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Country
import com.example.rickandmorty.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.flow.*

class MapViewModel(
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    val countryFlow = flow<List<Country>> {
        getCountriesUseCase().fold(
            onSuccess = {
                emit(it)
            },
            onFailure = {
                emit(emptyList())
            }
        )
    }.shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

}