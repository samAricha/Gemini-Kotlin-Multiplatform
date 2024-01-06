import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageSelectionScreen(){

    val imagePicker:ImagePicker = createPicker()

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