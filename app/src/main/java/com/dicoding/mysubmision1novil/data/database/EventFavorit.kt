package com.dicoding.mysubmision1novil.data.database

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class EventFavorit (

    @PrimaryKey(autoGenerate = false)
    var id: String="",
    var name : String="",
    var summary : String="",
    var mediaCover : String? = null

) : Parcelable


