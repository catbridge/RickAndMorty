package com.example.rickandmorty.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemEpisodeBinding
import com.example.rickandmorty.domain.model.Episode

class EpisodeViewHolder(
    private val binding: ItemEpisodeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(episode: Episode) {
        with(binding) {
            episodeText.text =
                itemView.context.getString(R.string.dash, episode.episode, episode.name)
        }
    }
}
