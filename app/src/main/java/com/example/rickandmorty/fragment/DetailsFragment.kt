package com.example.rickandmorty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.rickandmorty.domain.model.CharacterDetails
import com.example.rickandmorty.viewModel.DetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

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

        loadCharacterDetails()
        changeStatus()

        with(binding) {
            detailsToolbar.setupWithNavController(findNavController())
            recyclerViewEpisodes.addHorizontalSpaceDecoration(SPACE)
            recyclerViewEpisodes.adapter = adapter


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCharacterDetails() {
        with(binding) {
            viewModel.characterDetailsFlow
                .onEach { characterDetails ->
                        characterImg.load(characterDetails.image)
                        characterName.text = characterDetails.name
                        characterSpecies.text =
                            requireContext().getString(R.string.species, characterDetails.species)
                        characterGender.text =
                            requireContext().getString(R.string.gender, characterDetails.gender)
                        characterStatus.text =
                            requireContext().getString(R.string.status, characterDetails.status)

                        if (characterDetails.favourite){
                            imgFavorites.setImageResource(android.R.drawable.star_big_on)
                        }else{
                            imgFavorites.setImageResource(android.R.drawable.star_off)
                        }
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

            viewModel.episodesFlow
                .onEach { episodes ->
                    adapter.submitList(episodes)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun changeStatus(){
        with(binding) {
            viewModel.characterDetailsFlow.onEach { characterDetails ->
                imgFavorites.setOnClickListener {
                    if (characterDetails.favourite) {
                        imgFavorites.setImageResource(android.R.drawable.star_off)
                        characterDetails.favourite = false
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.deleteCharacter(characterDetails)
                        }
                    } else {
                        imgFavorites.setImageResource(android.R.drawable.star_big_on)
                        characterDetails.favourite = true
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.insertCharacter(characterDetails)
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }


    companion object {
        private const val SPACE = 10
    }
}