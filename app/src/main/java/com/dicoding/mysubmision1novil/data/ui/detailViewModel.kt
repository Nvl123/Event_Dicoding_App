import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mysubmision1novil.data.database.EventFavorit
import com.dicoding.mysubmision1novil.data.repository.RepositoryFavorit
import com.dicoding.mysubmision1novil.data.response.ListEventsItem
import com.dicoding.mysubmision1novil.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _event = MutableLiveData<ListEventsItem?>()
    val event: LiveData<ListEventsItem?> get() = _event

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _loading

    private val _favoriteRepository: RepositoryFavorit = RepositoryFavorit(application)

    fun detailData(idEvent: String) {
        _loading.postValue(true)
        val api = ApiConfig.getApiService()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.detailEvent(idEvent).execute()
                if (response.isSuccessful) {
                    val evenItem =response.body()?.event
                    Log.d("DetailViewModel", "Event data loaded: $evenItem")
                    _event.postValue(evenItem)
                } else {
                    Log.e("DetailViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Exception: ${e.message}")
            } finally {
                _loading.postValue(false)
            }
        }
    }


    fun insert(favorit: EventFavorit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("DetailViewModel", "Inserting favorite: $favorit")
                _favoriteRepository.insert(favorit)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Insert error: ${e.message}")
            }
        }
    }

    fun delete(favorit: EventFavorit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("DetailViewModel", "Deleting favorite: $favorit")
                _favoriteRepository.delete(favorit)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Delete error: ${e.message}")
            }
        }
    }


    fun getfavoritId(id: String): LiveData<EventFavorit?> {
        return _favoriteRepository.getEventFavoritById(id)
    }
}
