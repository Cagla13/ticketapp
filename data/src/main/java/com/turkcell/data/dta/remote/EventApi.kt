package com.turkcell.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class EventDto(
    val id: Int,
    val title: String,
    val description: String?,
    val date: String?,
    val location: String?
)

@Serializable
data class TicketDto(
    val id: Int,
    val event: EventDto?,
    val number: String?
)

interface EventApi {
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    @GET("me/tickets") // Swagger'daki biletlerim endpoint'i
    suspend fun getTickets(): List<TicketDto>
}