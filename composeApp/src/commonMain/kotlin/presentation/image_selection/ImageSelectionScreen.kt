package presentation.image_selection

import ImagePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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

import domain.AppEvents
import presentation.components.SelectedPhoto
import presentation.utils.rememberBitmapFromBytes

@Composable
fun ImageSelectionScreen(
    imagePicker: ImagePicker
){

    val imageSelectionViewModel = remember { ImageSelectionScreenViewModel() }
    val asyncGeminiData by imageSelectionViewModel.geminiData.collectAsState()


    var pickedImage: ByteArray? by remember { mutableStateOf(null) }


//    val imagePicker: ImagePicker = createPicker()



    imagePicker.registerPicker { imageBytes ->
        imageSelectionViewModel.onEvent(AppEvents.OnPhotoPicked(imageBytes))
//        pickedImage = imageBytes
    }

    var newGeminiModel = imageSelectionViewModel.geminiModel


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          imagePicker.pickImage()
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

        if(asyncGeminiData?.imageBytes == null) {
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
            SelectedPhoto(
                geminiDataModel = asyncGeminiData,
                modifier = Modifier
                    .size(150.dp)
                    .clickable {
                        imagePicker.pickImage()
                    }
            )
        }


    }

}