package eryaz.software.carParts.data.enums

enum class LanguageType(val fullName :String) {
    AZ("Azərbaycan"),
    RU("Русский"),
    EN("English"),
    TR("Türkçe"),
    AR("عربي");

    companion object {
        fun find(name: String?) = entries.find { it.name.lowercase() == name?.lowercase() } ?: AZ
    }
}