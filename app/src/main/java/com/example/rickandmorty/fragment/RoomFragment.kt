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
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.databinding.FragmentRoomListBinding
import com.example.rickandmorty.paging.PagingData
import com.example.rickandmorty.viewModel.RoomViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент добавлен для просмотра элементов в базе данных.
 */
class RoomFragment : Fragment() {
    private var _binding: FragmentRoomListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val viewModel by viewModel<RoomViewModel>()

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            RoomFragmentDirections.actionRoomToDetails(character.id)
        )
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
            roomToolbar.setOnMenuItemClickListener {
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
        viewModel.characterDaoFlow
            .onEach { it ->
                adapter.submitList(it.map { PagingData.Content(it) })
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.information)
            .setMessage(R.string.info_room)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        private const val ITEM_SPACE = 50
    }
}
