package com.example.pictures_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.pictures_app.PicturesApplication

const val APP_SETTINGS = "APP_SETTINGS"

const val SWITCH_DARK_MODE_STATE = "SWITCH_STATE"

object SharedPreferencesManager {

    fun persistDarkThemeSwitchStateValue(boolean: Boolean) {
        PicturesApplication.appSharedPreferences.edit()
            .putBoolean(SWITCH_DARK_MODE_STATE, boolean)
            .apply()
    }

    fun getSwitchStateValue(): Boolean {
        return PicturesApplication.appSharedPreferences
            .getBoolean(SWITCH_DARK_MODE_STATE, false)
    }

}