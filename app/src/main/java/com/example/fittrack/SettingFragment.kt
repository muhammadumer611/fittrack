package com.example.fittrack

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.fittrack.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences

    // Flags to ignore initial selection trigger of spinners
    private var isLanguageInitialized = false
    private var isThemeInitialized = false
    private var isCountryInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("SettingsPref", AppCompatActivity.MODE_PRIVATE)

        setupLanguageSpinner()
        setupThemeSpinner()
        setupCountrySpinner()
    }

    // ---------------- Language Spinner ----------------
    private fun setupLanguageSpinner() {
        val languages = arrayOf("English", "Urdu", "Arabic")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguage.adapter = adapter

        val savedLang = sharedPref.getString("language", "English")
        binding.spinnerLanguage.setSelection(languages.indexOf(savedLang))

        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!isLanguageInitialized) {
                    isLanguageInitialized = true
                    return
                }
                val selectedLang = languages[position]
                sharedPref.edit().putString("language", selectedLang).apply()
                applyLanguage(selectedLang)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // ---------------- Theme Spinner ----------------
    private fun setupThemeSpinner() {
        val themes = arrayOf("Light", "Dark")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTheme.adapter = adapter

        val savedTheme = sharedPref.getString("theme", "Light")
        binding.spinnerTheme.setSelection(themes.indexOf(savedTheme))

        binding.spinnerTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!isThemeInitialized) {
                    isThemeInitialized = true
                    return
                }
                val selectedTheme = themes[position]
                sharedPref.edit().putString("theme", selectedTheme).apply()

                if (selectedTheme == "Light") {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // ---------------- Country Spinner ----------------
    private fun setupCountrySpinner() {
        val countries = arrayOf("Pakistan", "USA", "UK", "India")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapter

        val savedCountry = sharedPref.getString("country", "Pakistan")
        binding.spinnerCountry.setSelection(countries.indexOf(savedCountry))

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!isCountryInitialized) {
                    isCountryInitialized = true
                    return
                }
                val selectedCountry = countries[position]
                sharedPref.edit().putString("country", selectedCountry).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // ---------------- Language Apply ----------------
    private fun applyLanguage(language: String) {
        val locale = when(language) {
            "Urdu" -> Locale("ur")
            "Arabic" -> Locale("ar")
            else -> Locale("en")
        }

        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Optional: refresh fragment UI strings if needed
        // requireActivity().recreate() // Use carefully, avoid dashboard fragment reset
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
