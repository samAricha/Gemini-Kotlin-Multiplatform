import data.Request
import data.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Base64

//@Serializable
//data class Part(val text: String)
//
//@Serializable
//data class Content(val parts: List<Part>)
//
//@Serializable
//data class Candidate(val content: Content)
//
//@Serializable
//data class Error(val message: String)
//
//@Serializable
//data class GenerateContentResponse(val error: Error? = null, val candidates: List<Candidate>? = null)
//
//@Serializable
//data class GenerateImageContentResponse(val error: Error? = null, val resp: String? = null)
//
//@Serializable
//data class GenerateContentRequest(val contents: Content)
//
//
//
//
//
//@Serializable
//data class GenerateImageContentRequest(val contents: ImageContent)
//
//@Serializable
//data class ImageContent(val parts: List<TextImagePart>)
//
//@Serializable
//sealed class TextImagePart {
//    @Serializable
//    data class Text(val text: String) : TextImagePart()
//    @Serializable
//    data class Image(val inlineData: InlineData) : TextImagePart()
//}
//
//@Serializable
//data class InlineData(
//    val mimeType: String,
//    val data: String
//)

class GeminiApi {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1/models"
    private val apiKey = "AIzaSyAPN3pRJqLIiSALpvs_vdehFQPEcAJZih4"

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true;
                ignoreUnknownKeys = true;
                explicitNulls = false}
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }


    fun resizeImage(originalImage: File, targetWidth: Int): File {
        // Implement the image resizing logic using a suitable image processing library
        // ...
        // For simplicity, this example returns the original image without resizing.
        return originalImage
    }

    fun encodeImageToBase64test(imageFile: ByteArray): String {
//        val imageBytes = imageFile.readBytes()
        val base64EncodedBytes = Base64.getEncoder().encode(imageFile)
        return String(base64EncodedBytes, StandardCharsets.UTF_8)
    }

    fun encodeImageToBase64(imageFile: File): String {
        val imageBytes = imageFile.readBytes()
        val base64EncodedBytes = Base64.getEncoder().encode(imageBytes)
        return String(base64EncodedBytes, StandardCharsets.UTF_8)
    }

//    fun createImagePart(
//        api: GeminiApi,
//        imageFile: ByteArray,
//        targetWidth: Int = 512
//    ): TextImagePart.Image {
////    val resizedImage = api.resizeImage(imageFile, targetWidth)
//
//        val base64EncodedImage = api.encodeImageToBase64test(imageFile)
//
//        return TextImagePart.Image(
//            InlineData(mimeType = "image/jpeg", data = base64EncodedImage)
//        )
//    }





//    suspend fun generateContentWithImage(contents: List<TextImagePart>): Response {
//        val request = GenerateImageContentRequest(ImageContent(contents))
//
//        val response: String =  client.post("$baseUrl/gemini-pro-vision:generateContent") {
//            contentType(ContentType.Application.Json)
//            url { parameters.append("key", apiKey) }
//            setBody(request)
//        }.bodyAsText()
//
//        return Json.decodeFromString(response)
//    }

//    suspend fun generateContentWithImage(contents: List<TextImagePart>): GenerateContentResponse {
//        val request = GenerateImageContentRequest(ImageContent(contents))
//
//        return client.post("$baseUrl/gemini-pro-vision:generateContent") {
//            contentType(ContentType.Application.Json)
//            url { parameters.append("key", apiKey) }
//            setBody(request)
//        }.body<GenerateContentResponse>()
//    }





//    suspend fun generateContentTest(prompt: String): GenerateContentResponse {
//        val part = Part(text = prompt)
//        val contents = Content(listOf(part))
//        val request = GenerateContentRequest(contents)
//
//        return client.post("$baseUrl/gemini-pro-vision:generateContent") {
//            contentType(ContentType.Application.Json)
//            url { parameters.append("key", apiKey) }
//            setBody(request)
//        }.body<GenerateContentResponse>()
//    }
//
//
//    suspend fun generateContent(
//        geminiApi: GeminiApi,
//        textPart: TextImagePart.Text,
//        imgPart: TextImagePart.Image
//    ): Response {
//        val result = geminiApi.generateContentWithImage(listOf(textPart, imgPart))
//
//        return result
//    }




    suspend fun generateContentWithMedia(prompt: String, image: ByteArray): Response {
        return makeApiRequest("$baseUrl/gemini-pro-vision:generateContent?key=$apiKey") {
            addText(prompt)
            addImage(image)
        }
    }

    @OptIn(InternalAPI::class)
    private suspend fun makeApiRequest(url: String, requestBuilder: Request.RequestBuilder.() -> Unit): Response {
        val request = Request.RequestBuilder().apply(requestBuilder).build()

        val response: String = client.post(url) {
            body = Json.encodeToString(request)
        }.bodyAsText()

        return Json.decodeFromString(response)
    }
}