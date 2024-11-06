package com.dicoding.mysubmision1novil.data.retrofit

import com.dicoding.mysubmision1novil.data.response.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    fun event(
        @Query("active") isActive: Int,
        @Query("q") searchQuery: String? = null
    ): Call<Response>

    @GET("events/{id}")
    fun detailEvent(
        @Path("id") id: String
    ) :Call<Response>

}