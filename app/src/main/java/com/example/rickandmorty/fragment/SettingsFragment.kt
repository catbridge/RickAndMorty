package com.example.rickandmorty.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentSettingsBinding
import com.example.rickandmorty.domain.model.NightMode
import com.example.rickandmorty.domain.service.PrefsService
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val prefsManager: PrefsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingsBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            settingsToolbar.setOnMenuItemClickListener {
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

            cvLanguage.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsToLanguage())
            }

            when (prefsManager.nightMode) {
                NightMode.LIGHT -> darkModeSwitch.isChecked = false
                NightMode.DARK -> darkModeSwitch.isChecked = true
            }

            darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    prefsManager.nightMode = NightMode.DARK
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    prefsManager.nightMode = NightMode.LIGHT
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.information)
            .setMessage(R.string.info_settings)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}