package com.dicoding.mysubmision1novil.data.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactorySettings (private val pref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ViewModelSettings::class.java)){
            return ViewModelSettings(pref) as T
        }

        throw IllegalArgumentException("Unknowwn View Model Class : " + modelClass.name)
    }
}