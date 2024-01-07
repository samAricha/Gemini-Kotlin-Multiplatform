package presentation.image_selection

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.AppEvents
import domain.GeminiDataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ImageSelectionScreenViewModel: ViewModel() {


    private val _geminiData = MutableStateFlow<GeminiDataModel?>(null)
    val geminiData = _geminiData.asStateFlow()

    private val _pickedImage = MutableStateFlow<ByteArray?>(null)
    val pickedImage = _pickedImage.asStateFlow()

    private val _geminiQuiz = MutableStateFlow("")
    val geminiQuiz = _geminiQuiz.asStateFlow()

    fun updateQuiz(quiz: String){
        _geminiQuiz.update {quiz }
    }

    fun updatePickedImage(img: ByteArray){
        _pickedImage.update {img }
    }


    fun onEvent(event: AppEvents) {
        when(event) {
            is AppEvents.OnPhotoPicked -> {

                _geminiData.update {
                    it?.copy(imageBytes = event.bytes)
                }
            }
            else -> Unit
        }
    }

}