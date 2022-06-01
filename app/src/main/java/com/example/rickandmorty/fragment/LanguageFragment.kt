package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.data.service.PreferenceServiceImpl
import com.example.rickandmorty.databinding.FragmentLanguageBinding
import com.example.rickandmorty.domain.model.Language
import org.koin.android.ext.android.inject

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val prefsManager: PreferenceServiceImpl by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLanguageBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            languageToolbar.setupWithNavController(findNavController())

            languageToolbar.setOnMenuItemClickListener {
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

            when (prefsManager.language) {
                Language.EN -> radioButtonEnglish
                Language.RU -> radioButtonRussian
            }.isChecked = true

            radiogroupLanguage.setOnCheckedChangeListener { _, buttonId ->
                when (buttonId) {
                    radioButtonEnglish.id -> {
                        prefsManager.language = Language.EN
                    }
                    radioButtonRussian.id -> {
                        prefsManager.language = Language.RU
                    }
                    else -> error("incorrect buttonId $buttonId")
                }
                activity?.recreate()
            }

        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.information)
            .setMessage(R.string.info_language)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}