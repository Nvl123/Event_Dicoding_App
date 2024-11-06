package com.dicoding.mysubmision1novil.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.mysubmision1novil.data.database.DaoFavorite
import com.dicoding.mysubmision1novil.data.database.DatabaseFavorit
import com.dicoding.mysubmision1novil.data.database.EventFavorit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.util.Log

class RepositoryFavorit(application: Application) {

    private val _daoFavorit: DaoFavorite
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = DatabaseFavorit.getDatabase(application)
        _daoFavorit = db.favorit()
    }

    suspend fun insert(event: EventFavorit) {
        try {
            withContext(Dispatchers.IO) {
                _daoFavorit.insert(event)
            }
        } catch (e: Exception) {
            Log.e("RepositoryFavorit", "Error inserting event: ${e.message}")
        }
    }

    suspend fun delete(event: EventFavorit) {
        try {
            withContext(Dispatchers.IO) {
                _daoFavorit.delete(event)
            }
        } catch (e: Exception) {
            Log.e("RepositoryFavorit", "Error deleting event: ${e.message}")
        }
    }

    fun getEventFavoritById(id: String): LiveData<EventFavorit?> {
        return _daoFavorit.getEventFavoritId(id)
    }

    fun getAllEventFavorit(): LiveData<List<EventFavorit>> {
        return _daoFavorit.getAllEventFavorit()
    }

}
