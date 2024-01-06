package domain


sealed interface AppEvents {
    class OnPhotoPicked(val bytes: ByteArray): AppEvents
}