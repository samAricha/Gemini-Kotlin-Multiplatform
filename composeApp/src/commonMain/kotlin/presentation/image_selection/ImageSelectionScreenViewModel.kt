package presentation.image_selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.AppEvents
import domain.GeminiDataModel



class ImageSelectionScreenViewModel: ViewModel() {

    var geminiModel: GeminiDataModel? by mutableStateOf(null)
        private set

    fun onEvent(event: AppEvents) {
        when(event) {
            is AppEvents.OnPhotoPicked -> {
                geminiModel = geminiModel?.copy(
                    imageBytes = event.bytes
                )
            }
            else -> Unit
        }
    }

}