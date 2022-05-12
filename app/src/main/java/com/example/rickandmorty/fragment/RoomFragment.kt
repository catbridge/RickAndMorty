package com.example.rickandmorty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.characterDatabase
import com.example.rickandmorty.databinding.FragmentRoomListBinding
import com.example.rickandmorty.model.PagingData
import kotlinx.coroutines.launch

/**
 * Фрагмент добавлен для просмотра элементов в базе данных.
 */
class RoomFragment : Fragment() {
    private var _binding: FragmentRoomListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val characterDao by lazy {
        requireContext().characterDatabase.characterDao()
    }

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            RoomFragmentDirections.actionRoomToDetails(character.id)
        )
    }

    override fun onResume() {
        super.onResume()
        showCharacters()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRoomListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCharacters()

        with(binding) {
            roomToolbar.setupWithNavController(findNavController())
            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addHorizontalSpaceDecoration(ITEM_SPACE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCharacters() {
        viewLifecycleOwner.lifecycleScope.launch {
            val daoCharacters = characterDao.getCharacters()
            val daoList = daoCharacters.map { PagingData.Content(it) }
            adapter.submitList(daoList)
        }
    }

    companion object {
        private const val ITEM_SPACE = 50
    }
}
