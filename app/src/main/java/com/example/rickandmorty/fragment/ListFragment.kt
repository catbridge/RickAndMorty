package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.addPaginationScrollListener
import com.example.rickandmorty.databinding.FragmentListBinding
import com.example.rickandmorty.model.CharacterList
import com.example.rickandmorty.model.PagingData
import com.example.rickandmorty.retrofit.RickAndMortyService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            ListFragmentDirections.toDetails(character.id)
        )
    }

    private var currentRequest: Call<CharacterList>? = null
    private var isLoading = false
    private var currentPage = 1

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
            toolbar.menu
                .findItem(R.id.info_button)
                .setOnMenuItemClickListener {
                    showDialog()
                    true
                }

            layoutSwiperefresh.setOnRefreshListener {
                adapter.submitList(emptyList())
                currentPage = 0
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
        currentRequest?.cancel()
        _binding = null
    }

    private fun loadCharacters(onLoadingFinished: () -> Unit = {}) {
        if (isLoading) return

        val loadingFinishedCallback = {
            isLoading = false
            onLoadingFinished()
        }

        val since = currentPage

        currentRequest = RickAndMortyService.rickAndMortyApi.getCharacters(since)
        currentRequest?.enqueue(object : Callback<CharacterList> {
            override fun onResponse(
                call: Call<CharacterList>,
                response: Response<CharacterList>
            ) {
                if (response.isSuccessful) {
                    val newList = adapter.currentList
                        .dropLastWhile { it == PagingData.Loading }
                        .plus(response.body()?.results?.map { PagingData.Content(it) }.orEmpty())
                        .plus(PagingData.Loading)
                    adapter.submitList(newList)
                    currentPage++

                } else {
                    handleErrors(response.errorBody()?.string() ?: ERROR_MESSAGE)
                }
                loadingFinishedCallback()
                currentRequest = null
            }

            override fun onFailure(call: Call<CharacterList>, t: Throwable) {
                handleErrors(t.message ?: ERROR_MESSAGE)
                loadingFinishedCallback()
                currentRequest = null
            }
        })
    }
    private fun handleErrors(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
            .setAction(android.R.string.ok) {}
            .show()
    }

    private  fun showDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("Information")
            .setMessage("Click on a character in the list to see more information about him")
            .setPositiveButton(android.R.string.ok){dialog, buttonId ->

            }
            .show()
    }


    companion object {
        private const val ITEM_SPACE = 50
        private const val ITEMS_TO_LOAD = 15
        private const val ERROR_MESSAGE = "Something went wrong"
    }
}