package com.example.rickandmorty.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.domain.model.Character


class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onCharacterClicked: (Character) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Character) {
        with(binding) {
            image.load(character.image) {
                scale(Scale.FIT)
                size(ViewSizeResolver(root))
            }
            textName.text = character.name

            root.setOnClickListener {
                onCharacterClicked(character)
            }
        }
    }
}