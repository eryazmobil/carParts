package eryaz.software.carParts.data.enums

enum class ResponseStatus(private val success: Boolean?) {
    OK(true),
    FAILED(false),
    NULL_DATA(null);
}