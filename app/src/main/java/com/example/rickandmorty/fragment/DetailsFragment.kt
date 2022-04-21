package com.example.rickandmorty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.rickandmorty.databinding.FragmentDetailsBinding
import com.example.rickandmorty.model.CharacterDetails
import com.example.rickandmorty.retrofit.RickAndMortyService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding) { "View was destroyed" }

    private val args: DetailsFragmentArgs by navArgs()


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
        RickAndMortyService.rickAndMortyApi.getCharacterDetails(args.id)
            .enqueue(object : Callback<CharacterDetails> {
                override fun onResponse(
                    call: Call<CharacterDetails>,
                    response: Response<CharacterDetails>
                ) {
                    if (response.isSuccessful) {
                        val characterDetails = response.body() ?: return
                        with(binding) {
                            characterImg.load(characterDetails.image)
                            characterName.text = characterDetails.name
                            characterSpecies.text = "Species: ${characterDetails.species}"
                            characterGender.text = "Gender: ${characterDetails.gender}"
                            characterStatus.text = "Status: ${characterDetails.status}"
                        }
                    } else {
                        handleErrors(response.errorBody()?.string() ?: "")
                    }
                }

                override fun onFailure(call: Call<CharacterDetails>, t: Throwable) {
                    handleErrors(t.message ?: "")
                }
            })
    }


    private fun handleErrors(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
            .setAction(android.R.string.ok) {}
            .show()
    }
}