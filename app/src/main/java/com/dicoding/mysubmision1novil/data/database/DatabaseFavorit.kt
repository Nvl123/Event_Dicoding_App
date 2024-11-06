package com.dicoding.mysubmision1novil.data.database

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [EventFavorit::class], version = 1, exportSchema = false)

abstract class DatabaseFavorit : RoomDatabase() {

    abstract fun favorit() : DaoFavorite

    companion object{
        @Volatile
        private var INSTANCE : DatabaseFavorit? = null

        @JvmStatic
        fun getDatabase(context: Context): DatabaseFavorit{
            return INSTANCE ?: synchronized(this) {

                try {
                    val instance = Room.databaseBuilder(
                        context.applicationContext, DatabaseFavorit::class.java,
                        "database_favorit"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("DatabaseFavorit", "Database Created")
                            }
                        })
                        .build()
                    INSTANCE=instance
                    instance
                }catch (e : Exception) {
                    Log.e("Databasefavorit", "Error Creating Database ${e.message}")
                    throw RuntimeException("Error Creating database")
                }
            }


        }

    }
}