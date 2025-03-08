package com.gangulwar.domain.services

import com.gangulwar.data.database.Database
import com.gangulwar.data.models.UrlEntry
import com.gangulwar.utils.ShortCodeGenerator
import com.mongodb.client.model.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

class UrlShortenerService {
    private val collection = Database.urlCollection

    suspend fun shortenUrl(longUrl: String): String = withContext(Dispatchers.IO) {
        val shortCode = ShortCodeGenerator.generate()
        val urlEntry = UrlEntry(shortCode, longUrl)

        collection.insertOne(urlEntry.toDocument())
        shortCode
    }

    suspend fun getLongUrl(shortCode: String): UrlEntry? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("shortCode", shortCode)).first()?.let(UrlEntry::fromDocument)
    }

    suspend fun deleteUrl(id: String) = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
    }
}
