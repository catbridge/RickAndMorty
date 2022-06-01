package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.*
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.databinding.FragmentListBinding
import com.example.rickandmorty.extensions.applyInsetsWithAppBar
import com.example.rickandmorty.paging.PagingData
import com.example.rickandmorty.viewModel.ListViewModel
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val viewModel by viewModel<ListViewModel>()

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            ListFragmentDirections.toDetails(character.id)
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.info_button -> {
                        showDialog()
                        true
                    }
                    R.id.history -> {
                        findNavController().navigate(
                            ListFragmentDirections.listToRoom()
                        )
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            layoutSwiperefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                viewModel.onRefresh {
                    layoutSwiperefresh.isRefreshing = false
                }
            }

            val layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

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
            .setTitle(R.string.information)
            .setMessage(R.string.info_list)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        private const val ITEM_SPACE = 50
        private const val ITEMS_TO_LOAD = 20
    }
}