package com.example.rickandmorty.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemEpisodeBinding
import com.example.rickandmorty.model.Episode

class EpisodeViewHolder(
    private val binding: ItemEpisodeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(episode: Episode) {
        with(binding) {
            episodeCode.text = episode.episode
            episodeName.text = episode.name
            episodeExtra.text = "-"
        }
    }
}
