package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.*
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.databinding.FragmentListBinding
import com.example.rickandmorty.domain.model.LceState
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
                .onEach { lce ->
                    when(lce){
                        is LceState.Content -> {
                            isVisibleProgressBar(false)
                            adapter.submitList(lce.value.map { PagingData.Content(it)})
                        }
                        is LceState.Error -> {
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(), lce.throwable.message, Toast.LENGTH_SHORT).show()
                        }
                        LceState.Loading -> isVisibleProgressBar(true)
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isVisibleProgressBar(visible:Boolean) {
        binding.progressBar.isVisible = visible
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