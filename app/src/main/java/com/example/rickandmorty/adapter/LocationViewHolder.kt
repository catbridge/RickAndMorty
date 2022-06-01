package com.example.rickandmorty.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ItemLocationBinding
import com.example.rickandmorty.domain.model.Location


class LocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onLocationClicked: (Location) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(location: Location) {
        with(binding) {
            locationName.text = itemView.context.getString(R.string.location_name, location.name)
            locationType.text = itemView.context.getString(R.string.location_type, location.type)
            locationDimension.text =
                itemView.context.getString(R.string.location_dimension, location.dimension)

            root.setOnClickListener {
                onLocationClicked(location)
            }
        }
    }
}

