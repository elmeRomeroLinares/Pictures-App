package com.example.pictures_app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.pictures_app.database.PicturesAppDatabase
import com.example.pictures_app.networking.RemoteDataSourceImplementation
import com.example.pictures_app.networking.RemoteApiService
import com.example.pictures_app.networking.buildApiService
import com.example.pictures_app.repository.PicturesRepository
import com.example.pictures_app.repository.PicturesRepositoryImplementation
import com.example.pictures_app.utils.APP_SETTINGS
import com.example.pictures_app.utils.SWITCH_DARK_MODE_STATE
import com.example.pictures_app.utils.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PicturesApplication : Application() {

    companion object {
        private lateinit var instance: PicturesApplication
        lateinit var appSharedPreferences: SharedPreferences
        lateinit var picturesRepository: PicturesRepository
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        picturesRepository =  ServiceLocator.providePicturesRepository(this)

        appSharedPreferences = instance.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        setAppLightDarkTheme()
    }

    private fun setAppLightDarkTheme() {
        if (appSharedPreferences.contains(SWITCH_DARK_MODE_STATE)) {
            when (SharedPreferencesManager.getSwitchStateValue()) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        } else {
            getCurrentPhoneTheme()
        }
    }

    private fun getCurrentPhoneTheme() {
        val nightModeFlags = instance.resources
            .configuration
            .uiMode
            .and(Configuration.UI_MODE_NIGHT_MASK)

        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                SharedPreferencesManager.persistDarkThemeSwitchStateValue(true)
            }
            else -> {
                SharedPreferencesManager.persistDarkThemeSwitchStateValue(false)
            }
        }

    }
}