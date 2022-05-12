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
import com.example.rickandmorty.databinding.FragmentDetailsBinding
import com.example.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val args: DetailsFragmentArgs by navArgs()
    private val repository = RickAndMortyRepository()


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
        with(binding) {
            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCharacterDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val characterDetails = repository.getCharacterDetails(args.id)
                with(binding) {
                    characterImg.load(characterDetails.image)
                    characterName.text = characterDetails.name
                    characterSpecies.text =
                        requireContext().getString(R.string.species, characterDetails.species)
                    characterGender.text =
                        requireContext().getString(R.string.gender, characterDetails.gender)
                    characterStatus.text =
                        requireContext().getString(R.string.status, characterDetails.status)
                }
            } catch (e: Throwable) {
                error(ERROR_MESSAGE)
            }

        }
    }


    companion object {
        private const val ERROR_MESSAGE = "Something went wrong"
    }
}