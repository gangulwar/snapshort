package com.gangulwar.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.bson.Document
import org.bson.types.ObjectId
import java.time.Instant

@Serializable
data class UrlEntry(
    val shortCode: String,
    val longUrl: String,
    val createdAt: String = Instant.now().toString()
) {
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        fun fromDocument(document: Document): UrlEntry = json.decodeFromString(document.toJson())
    }
}
