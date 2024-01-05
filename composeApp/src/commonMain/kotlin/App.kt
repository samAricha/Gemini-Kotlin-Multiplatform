import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.io.File

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    val geminiApi = remember { GeminiApi() }

    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("Summarize the benefits of Kotlin Multiplatform") }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }

    var showFilePicker by remember { mutableStateOf(false) }

    val fileType = listOf("jpg", "png")
    FilePicker(show = showFilePicker, fileExtensions = fileType) { file ->
        showFilePicker = false
        // do something with the file
    }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
//        val textPart = TextImagePart.Text("Prompt text here")
//        val imagePart = createImagePart(geminiApi,"path/to/your/image.jpg") // Replace with the actual path


        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                TextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.weight(7f)
                )
                TextButton(
                    onClick = {
                        if (prompt.isNotBlank()) {
                            val textPart = TextImagePart.Text(prompt)

//                            coroutineScope.launch {
//                                showProgress = true
//                                content = generateContent(geminiApi, textPart, imagePart)
//                                showProgress = false
//                            }
                        }
                    },

                    modifier = Modifier
                        .weight(3f)
                        .padding(all = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text("Submit")
                }
            }

            Spacer(Modifier.height(16.dp))
            if (showProgress) {
                CircularProgressIndicator()
            } else {
                Text(content)
            }


        }
    }
}



fun createImagePart(
    api: GeminiApi,
    imageFilePath: String,
    targetWidth: Int = 512
): TextImagePart.Image {
    val imageFile = File(imageFilePath)
    val resizedImage = api.resizeImage(imageFile, targetWidth)

    val base64EncodedImage = api.encodeImageToBase64(resizedImage)

    return TextImagePart.Image(
        InlineData(mime_type = "image/jpeg", data = base64EncodedImage)
    )
}



suspend fun generateContent(
    geminiApi: GeminiApi,
    textPart: TextImagePart.Text,
    imgPart: TextImagePart.Image
): String {
    val result = geminiApi.generateContentWithImage(listOf(textPart, imgPart))

    return if (result.candidates != null) {
        result.candidates[0].content.parts[0].text
    } else {
        "No results"
    }
}