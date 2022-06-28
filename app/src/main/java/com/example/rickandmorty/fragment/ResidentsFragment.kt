package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.databinding.FragmentResidentsListBinding
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.paging.PagingData
import com.example.rickandmorty.viewModel.ResidentsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ResidentsFragment : Fragment() {
    private var _binding: FragmentResidentsListBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val viewModel by viewModel<ResidentsViewModel> {
        parametersOf(args.id)
    }

    private val args: ResidentsFragmentArgs by navArgs()

    private val adapter = CharacterAdapter { character ->
        findNavController().navigate(
            ResidentsFragmentDirections.actionResidentsToDetails(character.id)
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentResidentsListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbarResidents.setupWithNavController(findNavController())
            toolbarResidents.setOnMenuItemClickListener {
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

            recyclerView.adapter = adapter
            recyclerView.addHorizontalSpaceDecoration(ITEM_SPACE)

            viewModel.residentsFlow
                .onEach { lce ->
                    when (lce) {

                        is LceState.Content -> {
                            isVisibleProgressBar(false)
                            adapter.submitList(lce.value.map { PagingData.Content(it) })
                        }
                        is LceState.Error -> {
                            isVisibleProgressBar(false)
                            Toast.makeText(
                                requireContext(), lce.throwable.message ?: "", Toast.LENGTH_SHORT
                            ).show()
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

    private fun isVisibleProgressBar(visible: Boolean) {
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
    }
}