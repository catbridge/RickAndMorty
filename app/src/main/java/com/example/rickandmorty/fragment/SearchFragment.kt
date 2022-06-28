package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.addPaginationScrollListener
import com.example.rickandmorty.databinding.FragmentSearchBinding
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.extensions.searchQueryFlow
import com.example.rickandmorty.paging.PagingData
import com.example.rickandmorty.viewModel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment  : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val viewModel by viewModel<SearchViewModel>()

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            SearchFragmentDirections.toDetails(character.id)
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            searchToolbar.setOnMenuItemClickListener {
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

            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            recyclerView.addHorizontalSpaceDecoration(ITEM_SPACE)
            recyclerView.addPaginationScrollListener(layoutManager, ITEMS_TO_LOAD){
                viewModel.onLoadMore()
            }


            searchToolbar.searchQueryFlow
                .onEach { query ->
                    viewModel.onQueryChanger(query)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)


            viewModel.dataFlow
                .onEach { lce ->
                    when(lce){
                        is LceState.Content ->{
                            isVisibleProgressBar(false)
                            adapter.submitList(lce.value.map { PagingData.Content(it) })
                        }
                        is LceState.Error ->{
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(), R.string.toast_error_query, Toast.LENGTH_SHORT).show()
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
            .setMessage(R.string.info_search)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        private const val ITEM_SPACE = 50
        private const val ITEMS_TO_LOAD = 20

    }
}
