package com.example.pictures_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.pictures_app.PicturesApplication

const val APP_SETTINGS = "APP_SETTINGS"

const val SWITCH_DARK_MODE_STATE = "SWITCH_STATE"

const val FIREBASE_TOKEN = "FIREBASE_TOKEN"

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

    fun persistFirebaseToken(token: String) {
        PicturesApplication.appSharedPreferences.edit()
            .putString(FIREBASE_TOKEN, token)
            .apply()
    }

}