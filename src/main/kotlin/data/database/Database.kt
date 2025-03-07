package com.gangulwar.data.database

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.Document
import com.mongodb.client.MongoCollection

object Database {
    private val client = MongoClients.create("mongodb://localhost:27017")
    val database: MongoDatabase = client.getDatabase("SnapShort")

    val urlCollection: MongoCollection<Document> = database.getCollection("urls")
}