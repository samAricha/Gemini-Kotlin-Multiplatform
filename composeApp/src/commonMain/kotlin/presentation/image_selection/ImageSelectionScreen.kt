package presentation.image_selection

import ImagePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher

import domain.AppEvents
import presentation.components.SelectedPhoto
import presentation.components.SelectedPhoto2
import presentation.utils.rememberBitmapFromBytes

@Composable
fun ImageSelectionScreen(
    imagePicker: ImagePicker
){

    val imageSelectionViewModel = remember { ImageSelectionScreenViewModel() }
    val asyncGeminiData by imageSelectionViewModel.geminiData.collectAsState()
    val geminiQuiz by imageSelectionViewModel.geminiQuiz.collectAsState()


    var pickedImage: ByteArray? by remember { mutableStateOf(null) }


//    val imagePicker: ImagePicker = createPicker()



    imagePicker.registerPicker { imageBytes ->
        imageSelectionViewModel.onEvent(AppEvents.OnPhotoPicked(imageBytes))
//        pickedImage = imageBytes
    }

    val pickerLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            files.firstOrNull()?.let { file ->
                // Do something with the selected file
                // You can get the ByteArray of the file
                pickedImage = file.readByteArray()
            }
        }
    )

    var newGeminiModel = imageSelectionViewModel.geminiModel


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                          imagePicker.pickImage()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(pickedImage == null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(40))
                        .background(MaterialTheme.colors.secondary)
                        .clickable {
                            imagePicker.pickImage()
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
                            imagePicker.pickImage()
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
        }




    }

}