package com.example.pictures_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentAboutPicturesAppBinding
import com.example.pictures_app.utils.toast

class AboutPicturesAppFragment : Fragment() {

    private lateinit var binding: FragmentAboutPicturesAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutPicturesAppBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.themeSelectorSwitch.isChecked = true
        binding.themeSelectorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // dark theme
                activity?.toast("Dark Theme Please")
            } else {
                activity?.toast("Light Theme Please")
            }
        }
    }
}