import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Base64

@Serializable
data class Part(val text: String)

@Serializable
data class Content(val parts: List<Part>)

@Serializable
data class Candidate(val content: Content)

@Serializable
data class Error(val message: String)

@Serializable
data class GenerateContentResponse(val error: Error? = null, val candidates: List<Candidate>? = null)

@Serializable
data class GenerateContentRequest(val contents: Content)





@Serializable
data class GenerateImageContentRequest(val contents: ImageContent)

@Serializable
data class ImageContent(val parts: List<TextImagePart>)

@Serializable
sealed class TextImagePart {
    @Serializable
    data class Text(val text: String) : TextImagePart()
    @Serializable
    data class Image(val inline_data: InlineData) : TextImagePart()
}

@Serializable
data class InlineData(
    val mime_type: String,
    val data: String
)

class GeminiApi {
    private val baseUrl = " https://generativelanguage.googleapis.com/v1beta/models"
    private val apiKey = "BuildKonfig.GEMINI_API_KEY"

    @OptIn(ExperimentalSerializationApi::class)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true; explicitNulls = false})
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

    suspend fun generateContentWithImage(contents: List<TextImagePart>): GenerateContentResponse {
        val request = GenerateImageContentRequest(ImageContent(contents))

        return client.post("$baseUrl/gemini-pro:generateContent") {
            contentType(ContentType.Application.Json)
            url { parameters.append("key", apiKey) }
            setBody(request)
        }.body<GenerateContentResponse>()
    }





    suspend fun generateContent(prompt: String): GenerateContentResponse {
        val part = Part(text = prompt)
        val contents = Content(listOf(part))
        val request = GenerateContentRequest(contents)

        return client.post("$baseUrl/gemini-pro:generateContent") {
            contentType(ContentType.Application.Json)
            url { parameters.append("key", apiKey) }
            setBody(request)
        }.body<GenerateContentResponse>()
    }



    fun createImagePart(
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
}