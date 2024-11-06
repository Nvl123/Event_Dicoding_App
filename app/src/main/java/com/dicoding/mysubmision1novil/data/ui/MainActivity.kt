package com.dicoding.mysubmision1novil.data.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.dicoding.mysubmision1novil.R
import com.dicoding.mysubmision1novil.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment
import com.dicoding.mysubmision1novil.data.settings.SettingPreferences
import com.dicoding.mysubmision1novil.data.settings.datastore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout with ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan NavController menggunakan NavHostFragment secara langsung
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentParent) as? NavHostFragment
        val navController = navHostFragment?.navController

        settingPreferences = SettingPreferences.getInstance(datastore)

        lifecycleScope.launch {
            settingPreferences.getThemeSettings().collect{isDarkMode ->
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        if (navController == null) {
            Log.e("MainActivity", "NavController is null from NavHostFragment")
            return
        }

        // Add Custom Navigation Handler for BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    Log.d("BottomNav", "Home selected")
                    true
                }
                R.id.toSoonEvent -> {
                    navController.navigate(R.id.toSoonEvent)
                    Log.d("BottomNav", "SoonEvent selected")
                    true
                }
                R.id.toPassEvent -> {
                    navController.navigate(R.id.toPassEvent)
                    Log.d("BottomNav", "PassEvent selected")
                    true

                }
                R.id.toFavoritEvent -> {
                    navController.navigate(R.id.toFavoritEvent)
                    Log.d("BottomNav", "Favorite Event selected")
                    true
                }

                R.id.toSettings -> {
                    navController.navigate(R.id.toSettings)
                    Log.d("BottomNav", "Favorite Event selected")
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentParent)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
