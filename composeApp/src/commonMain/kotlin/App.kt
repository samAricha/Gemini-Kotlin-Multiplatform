import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import presentation.image_selection.ImageSelectionScreen
import java.io.File

@Composable
fun App(
    imagePicker: ImagePicker
) {


    MaterialTheme {

        ImageSelectionScreen(
            imagePicker = imagePicker
        )

    }
}


fun replaceContentWithFile(inputString: String): String {
    return inputString.replace("content", "file")
}


//fun createImagePart3(
//    api: GeminiApi,
//    imageFile: ByteArray,
//    targetWidth: Int = 512
//): TextImagePart.Image {
////    val resizedImage = api.resizeImage(imageFile, targetWidth)
//
//    val base64EncodedImage = api.encodeImageToBase64test(imageFile)
//
//    return TextImagePart.Image(
//        InlineData(mimeType = "image/jpeg", data = base64EncodedImage)
//    )
//}
//
//
//fun createImagePart2(
//    api: GeminiApi,
//    imageFile: File,
//    targetWidth: Int = 512
//): TextImagePart.Image {
//    val resizedImage = api.resizeImage(imageFile, targetWidth)
//
//    val base64EncodedImage = api.encodeImageToBase64(resizedImage)
//
//    return TextImagePart.Image(
//        InlineData(mimeType = "image/jpeg", data = base64EncodedImage)
//    )
//}
//
//
//
//fun createImagePart(
//    api: GeminiApi,
//    imageFilePath: String,
//    targetWidth: Int = 512
//): TextImagePart.Image {
//    val imageFile = File(imageFilePath)
//    val resizedImage = api.resizeImage(imageFile, targetWidth)
//
//    val base64EncodedImage = api.encodeImageToBase64(resizedImage)
//
//    return TextImagePart.Image(
//        InlineData(mimeType = "image/jpeg", data = base64EncodedImage)
//    )
//}


