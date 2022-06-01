package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemLoadingBinding
import com.example.rickandmorty.databinding.ItemLocationBinding
import com.example.rickandmorty.paging.PagingData
import com.example.rickandmorty.domain.model.Location


class LocationAdapter(
    private val onLocationClicked: (Location) -> Unit
) : ListAdapter<PagingData<Location>, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

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
                LocationViewHolder(
                    binding = ItemLocationBinding.inflate(layoutInflater, parent, false),
                    onLocationClicked = onLocationClicked
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
        val location = (getItem(position) as? PagingData.Content)?.data ?: return
        (holder as? LocationViewHolder)?.bind(location)
    }

    companion object {

        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PagingData<Location>>() {
            override fun areItemsTheSame(
                oldItem: PagingData<Location>,
                newItem: PagingData<Location>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PagingData<Location>,
                newItem: PagingData<Location>
            ): Boolean {
                val oldLocation = oldItem as? PagingData.Content
                val newLocation = newItem as? PagingData.Content
                return oldLocation?.data == newLocation?.data
            }
        }
    }
}