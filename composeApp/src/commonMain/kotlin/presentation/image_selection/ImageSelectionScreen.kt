package presentation.image_selection

import GeminiApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import presentation.components.SelectedPhoto2

@Composable
fun ImageSelectionScreen(){

    val geminiApi = remember { GeminiApi() }
    val coroutineScope = rememberCoroutineScope()

    val imageSelectionViewModel = remember { ImageSelectionScreenViewModel() }
    val asyncGeminiData by imageSelectionViewModel.geminiData.collectAsState()
    val geminiQuiz by imageSelectionViewModel.geminiQuiz.collectAsState()
    val pickedImage by imageSelectionViewModel.pickedImage.collectAsState()


    var content by remember { mutableStateOf("") }
    var showProgress by remember { mutableStateOf(false) }


    val pickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            files.firstOrNull()?.let { file ->
                imageSelectionViewModel.updatePickedImage(file.readByteArray())
            }
        }
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Gemini in KMP")},
                backgroundColor = MaterialTheme.colors.secondary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    pickerLauncher.launch()
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Image"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if(pickedImage == null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(40))
                        .background(MaterialTheme.colors.secondary)
                        .clickable {
                            pickerLauncher.launch()
                        }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSecondary,
                            shape = RoundedCornerShape(40)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Add photo",
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {
                SelectedPhoto2(
                    imageByteArray = pickedImage!!,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            pickerLauncher.launch()
                        }
                )
            }


            OutlinedTextField(
                value = geminiQuiz,
                placeholder = { Text(text = "Upload Image and ask question")},
                onValueChange = {
                    imageSelectionViewModel.updateQuiz(it)
                },
                label = { Text(text = "User Input")},
                modifier = Modifier.fillMaxWidth(0.82f)

            )


            Button(
                onClick = {
                    //here we place the actual implementation of calling Gemini api
                    if (geminiQuiz.isNotBlank() && pickedImage != null) {
                        coroutineScope.launch {
                            showProgress = true
                            val result = geminiApi.generateContentWithMedia(geminiQuiz, pickedImage!!)
                            println("our resp ----> ${result.getText()}")


                            if (result.error == null) {
                                content = result.getText().toString()
                            } else {
                                content = "No results"
                            }

                            showProgress = false
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = MaterialTheme.colors.secondary,
                ),
            ) {
                Text(text = "Search")
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