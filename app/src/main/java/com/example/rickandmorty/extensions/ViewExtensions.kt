package com.example.rickandmorty.extensions


import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rickandmorty.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Toolbar.searchQueryFlow: Flow<String>
    get() = callbackFlow {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                trySend(newText)
                return true
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)

        awaitClose {
            searchView.setOnQueryTextListener(null)
        }
    }

fun View.applyInsetsWithAppBar(block: (View, Insets) -> Insets){
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        block(v, insets)
        windowInsets
    }
}