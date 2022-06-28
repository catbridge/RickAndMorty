package com.example.rickandmorty.fragment

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
import coil.load
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.EpisodeAdapter
import com.example.rickandmorty.addHorizontalSpaceDecoration
import com.example.rickandmorty.databinding.FragmentDetailsBinding
import com.example.rickandmorty.domain.model.LceState
import com.example.rickandmorty.viewModel.DetailsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }
    private val args: DetailsFragmentArgs by navArgs()

    private val viewModel by viewModel<DetailsViewModel> {
        parametersOf(args.id)
    }
    private val adapter = EpisodeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            detailsToolbar.setupWithNavController(findNavController())
            recyclerViewEpisodes.addHorizontalSpaceDecoration(SPACE)
            recyclerViewEpisodes.adapter = adapter

            viewModel.characterDetailsFlow
                .onEach { lce ->
                    when (lce) {
                        is LceState.Content -> {
                            isVisibleDetailsProgressBar(false)
                            val characterDetails = lce.value
                            characterImg.load(characterDetails.image)
                            characterName.text = characterDetails.name
                            characterSpecies.text =
                                requireContext().getString(
                                    R.string.species,
                                    characterDetails.species
                                )
                            characterGender.text =
                                requireContext().getString(R.string.gender, characterDetails.gender)
                            characterStatus.text =
                                requireContext().getString(R.string.status, characterDetails.status)

                            if (characterDetails.isFavourite) {
                                imgFavorites.setImageResource(android.R.drawable.star_big_on)
                            } else {
                                imgFavorites.setImageResource(android.R.drawable.star_off)
                            }
                        }
                        is LceState.Error -> {
                            isVisibleDetailsProgressBar(false)
                            Toast.makeText(requireContext(), lce.throwable.message,
                                Toast.LENGTH_SHORT).show()
                        }
                        LceState.Loading -> isVisibleDetailsProgressBar(true)
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)

            imgFavorites.setOnClickListener {
                viewModel.onFavouriteClicked()
                Toast.makeText(requireContext(), R.string.toast_favourite,
                    Toast.LENGTH_SHORT).show()
            }

            viewModel.episodesFlow
                .onEach { lce ->
                    when (lce) {
                        is LceState.Content ->{
                            isVisibleEpisodesProgressBar(false)
                            val episodes = lce.value
                            adapter.submitList(episodes)
                        }
                        is LceState.Error ->{
                            isVisibleEpisodesProgressBar(false)
                            Toast.makeText(
                                requireContext(), lce.throwable.message ?: "", Toast.LENGTH_SHORT).show()
                        }
                        LceState.Loading -> isVisibleEpisodesProgressBar(true)
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isVisibleEpisodesProgressBar(visible:Boolean) {
        binding.progressBarEpisodes.isVisible = visible
    }

    private fun isVisibleDetailsProgressBar(visible:Boolean) {
        binding.progressBarEpisodes.isVisible = visible
    }


    companion object {
        private const val SPACE = 10
    }
}