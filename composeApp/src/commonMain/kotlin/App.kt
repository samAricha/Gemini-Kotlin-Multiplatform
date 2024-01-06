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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.util.Identity.encode
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.io.File

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {







    val geminiApi = remember { GeminiApi() }
    lateinit var createdImage: TextImagePart.Image

    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("Summarize the benefits of Kotlin Multiplatform") }
    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }

    var pickedImage by remember { mutableStateOf("") }

    var showFilePicker by remember { mutableStateOf(false) }
    val fileType = listOf("jpg", "png")
    val pickMediaLauncher = FilePicker(
        show = showFilePicker,
        fileExtensions = fileType
    ) { file ->
        showFilePicker = false
        // do something with the file
        if (file != null) {
            coroutineScope.launch {
                createdImage = createImagePart(geminiApi, replaceContentWithFile(file.path))
            }
        }

    }

    MaterialTheme {


        ImageSelectionScreen()
//        var showContent by remember { mutableStateOf(false) }


//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text(text = "Gemini Multiplatform ChatBot")},
//                    backgroundColor = MaterialTheme.colors.primary,
//                    contentColor = MaterialTheme.colors.onPrimary
//                )
//            },
//        ) {
//            Column(
//                Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Row {
//                    //Add Image Icon
//                    IconButton(
//                        modifier = Modifier.padding(4.dp ),
//                        onClick = {
//                            showFilePicker = true
//                             }
//                    ) {
//                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Image")
//                    }
//
//
//                    TextField(
//                        value = prompt,
//                        onValueChange = { prompt = it },
//                        modifier = Modifier.weight(7f)
//                    )
//                    TextButton(
//                        onClick = {
//                            if (prompt.isNotBlank()) {
//                                val textPart = TextImagePart.Text(prompt)
//
//                                coroutineScope.launch {
//                                    showProgress = true
////                                    val textPart = TextImagePart.Text("Prompt text here")
////                                    val imagePart = createImagePart(geminiApi,pickedImage)
//                                    val imagePart = createdImage
//
//                                    content = generateContent(geminiApi, textPart, imagePart)
//                                    showProgress = false
//                                }
//                            }
//                        },
//
//                        modifier = Modifier
//                            .weight(3f)
//                            .padding(all = 4.dp)
//                            .align(Alignment.CenterVertically)
//                    ) {
//                        Text("Submit")
//                    }
//                }
//
//                Spacer(Modifier.height(16.dp))
//                if (showProgress) {
//                    CircularProgressIndicator()
//                } else {
//                    Text(content)
//                }
//
//
//            }
//        }

    }
}


fun replaceContentWithFile(inputString: String): String {
    return inputString.replace("content", "file")
}


fun createImagePart3(
    api: GeminiApi,
    imageFile: ByteArray,
    targetWidth: Int = 512
): TextImagePart.Image {
//    val resizedImage = api.resizeImage(imageFile, targetWidth)

    val base64EncodedImage = api.encodeImageToBase64test(imageFile)

    return TextImagePart.Image(
        InlineData(mime_type = "image/jpeg", data = base64EncodedImage)
    )
}


fun createImagePart2(
    api: GeminiApi,
    imageFile: File,
    targetWidth: Int = 512
): TextImagePart.Image {
    val resizedImage = api.resizeImage(imageFile, targetWidth)

    val base64EncodedImage = api.encodeImageToBase64(resizedImage)

    return TextImagePart.Image(
        InlineData(mime_type = "image/jpeg", data = base64EncodedImage)
    )
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