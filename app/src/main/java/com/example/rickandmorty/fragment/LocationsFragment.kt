package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.LocationAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.addPaginationScrollListener
import com.example.rickandmorty.databinding.FragmentLocationListBinding
import com.example.rickandmorty.model.PagingData
import com.example.rickandmorty.viewModel.LocationViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationsFragment : Fragment() {
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val viewModel by viewModel<LocationViewModel>()

    private val adapter = LocationAdapter{location ->
        findNavController().navigate(
            LocationsFragmentDirections.actionLocationsToResidents(location.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLocationListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            locationToolbar.setupWithNavController(findNavController())
            locationToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.info_button -> {
                        showDialog()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            val layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            layoutSwiperefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                viewModel.onRefresh {
                    layoutSwiperefresh.isRefreshing = false
                }
            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            recyclerView.addHorizontalSpaceDecoration(ITEM_SPACE)
            recyclerView.addPaginationScrollListener(layoutManager, ITEMS_TO_LOAD) {
                viewModel.onLoadMore()
            }

            viewModel.dataFlow
                .onEach { it ->
                    adapter.submitList(it.map { PagingData.Content(it) })
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Information")
            .setMessage("Click on the location to see a list of characters who have been there")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        private const val ITEM_SPACE = 50
        private const val ITEMS_TO_LOAD = 20
    }
}
