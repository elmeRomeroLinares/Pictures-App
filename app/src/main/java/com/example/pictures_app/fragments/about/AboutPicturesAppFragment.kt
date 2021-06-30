package com.example.pictures_app.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.databinding.FragmentAboutPicturesAppBinding

class AboutPicturesAppFragment : Fragment() {

    private lateinit var aboutFragmentViewModel: AboutFragmentViewModel
    private var _binding: FragmentAboutPicturesAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutPicturesAppBinding.inflate(inflater, container, false)
        val root: View = binding.root

        aboutFragmentViewModel =
            ViewModelProvider(this).get(AboutFragmentViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        aboutFragmentViewModel.isDarkThemeSelected.observe(viewLifecycleOwner, {
            binding.themeSelectorSwitch.isChecked = it
        })
        binding.themeSelectorSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                aboutFragmentViewModel.onDarkThemeSwitchPressed(isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                aboutFragmentViewModel.onDarkThemeSwitchPressed(isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}