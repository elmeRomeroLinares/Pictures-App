package com.example.pictures_app.fragments.about

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.utils.SharedPreferencesManager

class AboutFragmentViewModel : ViewModel() {

    val isDarkThemeSelected: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getPersistedSwitchState()
    }

    fun onDarkThemeSwitchPressed(state: Boolean) {
        SharedPreferencesManager.persistDarkThemeSwitchStateValue(state)
        isDarkThemeSelected.postValue(state)
    }

    private fun getPersistedSwitchState() {
        isDarkThemeSelected.postValue(SharedPreferencesManager.getSwitchStateValue())
    }
}