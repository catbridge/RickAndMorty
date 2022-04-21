package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.PagingData

class CharacterAdapter(
    private val onCharacterClicked: (Character) -> Unit
) : ListAdapter<PagingData<Character>, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingData.Content -> TYPE_ITEM
            PagingData.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ITEM -> {
                CharacterViewHolder(
                    binding = ItemCharacterBinding.inflate(layoutInflater, parent, false),
                    onCharacterClicked = onCharacterClicked
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("Unsupported viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = (getItem(position) as? PagingData.Content)?.data ?: return
        (holder as? CharacterViewHolder)?.bind(character)
    }

    companion object {

        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagingData<Character>>() {
            override fun areItemsTheSame(
                oldItem: PagingData<Character>,
                newItem: PagingData<Character>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PagingData<Character>,
                newItem: PagingData<Character>
            ): Boolean {
                val oldCharacter = oldItem as? PagingData.Content
                val newCharacter = newItem as? PagingData.Content
                return oldCharacter?.data == newCharacter?.data
            }
        }
    }
}