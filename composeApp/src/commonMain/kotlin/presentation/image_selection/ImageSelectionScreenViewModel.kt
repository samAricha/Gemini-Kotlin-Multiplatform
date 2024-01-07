package presentation.image_selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.AppEvents
import domain.GeminiDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ImageSelectionScreenViewModel: ViewModel() {

    var geminiModel: GeminiDataModel? by mutableStateOf(null)
        private set

    private val _geminiData = MutableStateFlow<GeminiDataModel?>(null)
    val geminiData = _geminiData.asStateFlow()

    private val _geminiQuiz = MutableStateFlow("")
    val geminiQuiz = _geminiQuiz.asStateFlow()

    fun updateQuiz(quiz: String){
        _geminiQuiz.update {quiz }
    }


    fun onEvent(event: AppEvents) {
        when(event) {
            is AppEvents.OnPhotoPicked -> {
                geminiModel = geminiModel?.copy(
                    imageBytes = event.bytes
                )

                _geminiData.update {
                    it?.copy(imageBytes = event.bytes)
                }
            }
            else -> Unit
        }
    }

}