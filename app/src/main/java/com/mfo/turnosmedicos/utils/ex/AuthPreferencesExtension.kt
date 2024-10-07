package com.mfo.turnosmedicos.utils.ex

import android.content.Context
import android.content.SharedPreferences
import com.mfo.turnosmedicos.utils.PreferencesHelper

fun Context.defaultPrefs(): SharedPreferences = PreferencesHelper.defaultPrefs(this)

fun Context.saveToken(token: String) {
    val preferences = this.defaultPrefs()
    preferences.edit().putString("jwt", token).apply()
}

fun Context.getToken(): String {
    val preferences = this.defaultPrefs()
    return preferences.getString("jwt", "").orEmpty()
}

fun Context.clearSessionPreferences() {
    val preferences = this.defaultPrefs()
    preferences.edit().remove("jwt").apply()
}