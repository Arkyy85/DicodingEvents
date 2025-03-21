package com.ammar.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class EventsResponse(

    @field:SerializedName("listEvents") val listEvents: List<ListEventsItem>?,

    @field:SerializedName("error") val error: Boolean,

    @field:SerializedName("message") val message: String
)
