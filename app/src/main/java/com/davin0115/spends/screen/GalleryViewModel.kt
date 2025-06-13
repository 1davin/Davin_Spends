import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davin0115.spends.model.Gallery
import com.davin0115.spends.network.GalleryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel() : ViewModel(){

    var data = mutableStateOf(emptyList<Gallery>())
        private set

    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data.value = GalleryApi.service.getGallery()
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}