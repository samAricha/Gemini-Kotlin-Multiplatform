package presentation.image_selection

import ImagePicker
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import createPicker
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun ImageSelectionScreen(){

    val ImageSelectionviewModel = remember { ImageSelectionScreenViewModel() }

    val imagePicker: ImagePicker = createPicker()

    imagePicker.registerPicker { imageBytes ->
//        onEvent(ContactListEvent.OnPhotoPicked(imageBytes))
    }
//    IconButton(
//        modifier = Modifier.padding(4.dp ),
//        onClick = {
//            imageSelection
//             }
//    ) {
//        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Image")
//    }


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
                    contentDescription = "Add contact"
                )
            }
        }
    ) {


    }

}