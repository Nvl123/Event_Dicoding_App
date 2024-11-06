import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mysubmision1novil.data.database.EventFavorit
import com.dicoding.mysubmision1novil.data.repository.RepositoryFavorit
import kotlinx.coroutines.launch

class ViewModelFavorit(application: Application) : AndroidViewModel(application) {

    private val _repositoryFavorit: RepositoryFavorit = RepositoryFavorit(application)

    // LiveData untuk data event favorit
    private val _event = MutableLiveData<List<EventFavorit>>()
    val event: LiveData<List<EventFavorit>> get() = _event

    // LiveData untuk pesan kesalahan
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Mengambil semua event favorit
    fun getEventFavorit() {
        _isLoading.postValue(true)  // Set loading ke true
        viewModelScope.launch {
            _repositoryFavorit.getAllEventFavorit().observeForever { allFavorites ->
                _event.postValue(allFavorites)
                _isLoading.postValue(false)  // Set loading ke false setelah data didapat
            }
        }
    }

    // Fungsi pencarian
    fun searchFavoriteEvent(q: String) {
        _isLoading.postValue(true)  // Set loading ke true saat pencarian dimulai
        viewModelScope.launch {
            _repositoryFavorit.getAllEventFavorit().observeForever { allFavorites ->
                if (q.isBlank()) {
                    _event.postValue(allFavorites)
                    _isLoading.postValue(false)  // Set loading ke false setelah selesai
                    return@observeForever
                }

                // Filter daftar favorit berdasarkan query
                val filteredList = allFavorites.filter { it.name.contains(q, ignoreCase = true) }

                if (filteredList.isEmpty()) {
                    _message.postValue("Item tidak ditemukan")
                }

                _event.postValue(filteredList)
                _isLoading.postValue(false)  // Set loading ke false setelah selesai
            }
        }
    }
}
