package com.example.githubusers.handler

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // Retrieve the current value of the switch state from SharedPreferences
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkModeEnabled = sharedPrefs.getBoolean("dark_mode", false)

        // Set the appropriate mode based on the switch state
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}