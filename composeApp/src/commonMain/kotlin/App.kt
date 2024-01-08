import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import presentation.image_selection.ImageSelectionScreen
import java.io.File

@Composable
fun App() {

    MaterialTheme {
        ImageSelectionScreen()
    }
}


fun replaceContentWithFile(inputString: String): String {
    return inputString.replace("content", "file")
}



