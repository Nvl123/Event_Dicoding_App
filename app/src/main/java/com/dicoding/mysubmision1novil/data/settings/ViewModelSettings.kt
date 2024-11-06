package com.dicoding.mysubmision1novil.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelSettings (private val pref: SettingPreferences): ViewModel(){

    fun getThemeSettings(): LiveData<Boolean>{
        return pref.getThemeSettings().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive : Boolean){
        viewModelScope.launch (Dispatchers.IO){
            pref.saveThemeSettings(isDarkModeActive)
        }
    }

}