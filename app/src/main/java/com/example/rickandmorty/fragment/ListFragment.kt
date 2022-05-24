package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.*
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.databinding.FragmentListBinding
import com.example.rickandmorty.model.PagingData
import com.example.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.launch


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val characterDao by lazy {
        requireContext().characterDatabase.characterDao()
    }

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            ListFragmentDirections.toDetails(character.id)
        )
    }

    private var isLoading = false
    private var currentPage = 1
    private val repository = RickAndMortyRepository()

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

        loadCharacters()

        with(binding) {
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.info_button) {
                    showDialog()
                    true
                } else if (it.itemId == R.id.history) {
                    findNavController().navigate(
                        ListFragmentDirections.listToRoom()
                    )
                    true
                } else {
                    false
                }
            }

            layoutSwiperefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                currentPage = 1
                loadCharacters {
                    layoutSwiperefresh.isRefreshing = false
                }
            }

            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addHorizontalSpaceDecoration(ITEM_SPACE)
            recyclerView.addPaginationScrollListener(linearLayoutManager, ITEMS_TO_LOAD) {
                loadCharacters()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCharacters(onLoadingFinished: () -> Unit = {}) {
        if (isLoading) return
        val loadingFinishedCallback = {
            isLoading = false
            onLoadingFinished()
        }
        val since = currentPage

        viewLifecycleOwner.lifecycleScope.launch {
            try {
//          Тут я по разному пытался добавить правильное отображение списков из БД и АПИ, но даже
//          в лучшем случае списки перемешивались. Так и не понял как правильно резализовать это
//          вместе с paginationScrollListener, оставил так. Элементы из API в БД добавляются,
//          посмотреть их можно в отдельном фрагменте, нажав по новой кнопке на тулбаре.

//          val daoCharacters = characterDao.getCharacters()
//          val daoList = daoCharacters.map { PagingData.Content(it) }
//          adapter.submitList(daoList)
                val characters = repository.getCharacters(since)
                characterDao.insertList(characters.results)
                val apiList = adapter.currentList
                    .dropLastWhile { it == PagingData.Loading }
                    .plus(characters.results.map { PagingData.Content(it) })
                    .plus(PagingData.Loading)
                adapter.submitList(apiList)
                currentPage++
                loadingFinishedCallback()

            } catch (e: Throwable) {
                error(ERROR_MESSAGE)
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Information")
            .setMessage("Click on a character in the list to see more information about him")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }


    companion object {
        private const val ITEM_SPACE = 50
        private const val ITEMS_TO_LOAD = 15
        private const val ERROR_MESSAGE = "Something went wrong"
    }
}