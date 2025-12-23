package eryaz.software.carParts.data.enums

enum class Language {
    EN,
    TR;

    companion object {
        fun find(lang: String?) =
            values().find { it.name.lowercase() == lang?.lowercase() } ?: TR
    }
}