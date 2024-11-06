package com.dicoding.mysubmision1novil.data.database
import androidx.lifecycle.LiveData
import  androidx.room.*

@Dao
interface DaoFavorite  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event : EventFavorit)

    @Delete
    fun delete(event: EventFavorit)

    @Query("SELECT * FROM eventFavorit WHERE id = :id")
    fun getEventFavoritId(id: String) : LiveData<EventFavorit?>

    @Query("SELECT * FROM eventFavorit")
    fun getAllEventFavorit() : LiveData<List<EventFavorit>>


}