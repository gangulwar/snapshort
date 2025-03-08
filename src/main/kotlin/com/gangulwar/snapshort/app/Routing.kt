package com.gangulwar.snapshort.app

import com.gangulwar.snapshort.domain.services.UrlShortenerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val service = UrlShortenerService()

    routing {
        post("/shorten") {
            try {
                val contentType = call.request.contentType()
                val longUrl: String?

                if (contentType.match(ContentType.Application.FormUrlEncoded)) {
                    val parameters = call.receiveParameters()
                    longUrl = parameters["longUrl"]
                } else if (contentType.match(ContentType.Application.Json)) {
                    val requestBody = call.receiveText()
                    longUrl = Regex("\"longUrl\"\\s*:\\s*\"(.*?)\"")
                        .find(requestBody)?.groupValues?.get(1)
                } else {
                    return@post call.respondText(
                        "Unsupported Content-Type",
                        status = HttpStatusCode.UnsupportedMediaType
                    )
                }

                if (longUrl == null) {
                    return@post call.respondText(
                        "Missing 'longUrl' parameter",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val shortCode = service.shortenUrl(longUrl)
                call.respondText("Shortened URL: http://localhost:8080/$shortCode", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                println("Error: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }

        get("/{shortCode}") {
            val shortCode = call.parameters["shortCode"] ?: return@get call.respondText("Invalid Code")
            val urlEntry = service.getLongUrl(shortCode)

            if (urlEntry != null) {
                call.respondRedirect(urlEntry.longUrl)
            } else {
                call.respondText("URL not found")
            }
        }

        get("/") {
            call.respondText("Up and running!")
        }
    }
}
