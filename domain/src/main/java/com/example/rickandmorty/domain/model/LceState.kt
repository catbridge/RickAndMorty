package com.example.rickandmorty.domain.model

sealed class LceState<out T> {
    object Loading : LceState<Nothing>()

    data class Content<T>(val value: T) : LceState<T>()

    data class Error(val throwable: Throwable) : LceState<Nothing>()
}