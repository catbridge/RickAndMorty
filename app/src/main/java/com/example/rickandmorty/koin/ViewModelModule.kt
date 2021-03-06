package com.example.rickandmorty.koin

import com.example.rickandmorty.viewModel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ListViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::FavouriteViewModel)
    viewModelOf(::LocationViewModel)
    viewModelOf(::ResidentsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf (::MapViewModel)
}