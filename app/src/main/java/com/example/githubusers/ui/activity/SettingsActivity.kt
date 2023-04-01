package com.example.githubusers.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubusers.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.switchTheme.isChecked = sharedPrefs.getBoolean("dark_mode", false)

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Save the switch state to SharedPreferences
            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()

            // Set the appropriate mode based on the switch state
            setTheme(isChecked)
        }
        setTheme(binding.switchTheme.isChecked)
    }

    override fun onResume() {
        super.onResume()
        setTheme(binding.switchTheme.isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Save the switch state to SharedPreferences when the activity is destroyed
        sharedPrefs.edit().putBoolean("dark_mode", binding.switchTheme.isChecked).apply()
    }

    private fun setTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
        }
    }
}
