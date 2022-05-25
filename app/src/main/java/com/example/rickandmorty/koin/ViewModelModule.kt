package com.example.rickandmorty.koin

import com.example.rickandmorty.viewModel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListViewModel(get(), get()) }
    viewModel { (id: Int) -> DetailsViewModel(get(), id) }
    viewModel { RoomViewModel(get()) }
    viewModel { LocationViewModel(get()) }
    viewModel { (id: Int) -> ResidentsViewModel(get(), id) }

}