package com.dicoding.mysubmision1novil.data.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mysubmision1novil.data.response.ListEventsItem
import com.dicoding.mysubmision1novil.data.response.Response
import com.dicoding.mysubmision1novil.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call

class EventViewModel : ViewModel() {

    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> get() = _event

    private val _soonEvent = MutableLiveData<List<ListEventsItem>>()
    val soonEvent: LiveData<List<ListEventsItem>> get() = _soonEvent

    private val _pastEvent = MutableLiveData<List<ListEventsItem>>()
    val pastEvent: LiveData<List<ListEventsItem>> get() = _pastEvent

    private val _Message = MutableLiveData<String?>()
    val Message: LiveData<String?> get() = _Message



    fun loadSoonEvent() {

        Log.d("EventViewModel", "loadSoonEvent dipanggil")

        val api = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {

            api.event(isActive = 1).enqueue(object : Callback<Response> {
                override fun onResponse(p0: Call<Response>, p1: retrofit2.Response<Response>) {
                    if (p1.isSuccessful) {
                        p1.body()?.let { eventrespons ->
                            eventrespons.listEvents?.let { _soonEvent.postValue(it) }
                        }
                    } else {
                        Log.e("onFailure", p1.message())
                    }

                }

                override fun onFailure(p0: Call<Response>, p1: Throwable) {
                    Log.e("onFailure", "${p1.message}")
                }

            })

        }
    }

    fun loadPastEvent() {

        Log.d("EventViewModel", "loadPastEvent dipanggil")
        val api = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            api.event(isActive = 0).enqueue(object : Callback<Response> {

                override fun onResponse(p0: Call<Response>, p1: retrofit2.Response<Response>) {
                    if (p1.isSuccessful) {
                        p1.body()?.let { eventrespons ->
                            eventrespons.listEvents?.let { _pastEvent.postValue(it) }
                        }
                    } else {
                        Log.e("on Failure", p1.message())
                    }


                }

                override fun onFailure(p0: Call<Response>, p1: Throwable) {
                    Log.e("onfailure", "${p1.message}")
                }
            })

        }
    }

    fun searchSoonEvent(q: String) {
        if (q.isBlank()) {
            _event.postValue(emptyList())
            return
        }

        val api = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            // Execute the API call
            api.event(isActive = 1).enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val filteredList = responseBody.listEvents?.filter {
                                it.name.contains(q, ignoreCase = true)
                            } ?: emptyList()

                            if (filteredList.isEmpty()) {
                                _Message.postValue("Item tidak ditemukan")
                            }

                            _event.postValue(filteredList)
                        } ?: run {
                            _Message.postValue("Item tidak ditemukan")
                            _event.postValue(emptyList())
                        }
                    } else {
                        _Message.postValue("Terjadi kesalahan: ${response.message()}")
                        _event.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    _Message.postValue("Kesalahan: ${t.message}")
                    _event.postValue(emptyList())
                }
            })
        }
    }



    fun searchPastEvent(q: String) {
        if (q.isBlank()) {
            _event.postValue(emptyList())
            return
        }

        val api = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            // Execute the API call
            api.event(isActive = 0).enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val filteredList = responseBody.listEvents?.filter {
                                it.name.contains(q, ignoreCase = true)
                            } ?: emptyList()

                            if (filteredList.isEmpty()) {
                                _Message.postValue("Item tidak ditemukan")
                            }

                            _event.postValue(filteredList)
                        } ?: run {
                            _Message.postValue("Item tidak ditemukan")
                            _event.postValue(emptyList())
                        }
                    } else {
                        _Message.postValue("Terjadi kesalahan: ${response.message()}")
                        _event.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    _Message.postValue("Kesalahan: ${t.message}")
                    _event.postValue(emptyList())
                }
            })
        }
    }




    fun resetMessage() {
        _Message.value = null
    }
}


