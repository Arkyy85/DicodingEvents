package com.ammar.core.utils

import com.ammar.core.data.source.local.entity.EventsEntity
import com.ammar.core.data.source.remote.response.ListEventsItem
import com.ammar.core.domain.model.Events

object DataMapper {

    fun mapResponsesToEntities(input: List<ListEventsItem>, eventType: String): List<EventsEntity> {
        return input.map {
            EventsEntity(
                id = it.id,
                name = it.name ?: "",
                summary = it.summary ?: "",
                mediaCover = it.mediaCover ?: "",
                registrants = it.registrants ?: 0,
                imageLogo = it.imageLogo ?: "",
                link = it.link ?: "",
                description = it.description ?: "",
                ownerName = it.ownerName ?: "",
                cityName = it.cityName ?: "",
                quota = it.quota ?: 0,
                beginTime = it.beginTime ?: "",
                endTime = it.endTime ?: "",
                category = it.category ?: "",
                isFav = false,
                eventType = eventType
            )
        }
    }

    fun mapEntitiesToDomain(input: List<EventsEntity>): List<Events> {
        return input.map {
            Events(
                id = it.id,
                name = it.name,
                summary = it.summary,
                mediaCover = it.mediaCover,
                registrants = it.registrants,
                imageLogo = it.imageLogo,
                link = it.link,
                description = it.description,
                ownerName = it.ownerName,
                cityName = it.cityName,
                quota = it.quota,
                beginTime = it.beginTime,
                endTime = it.endTime,
                category = it.category,
                isFav = it.isFav,
                eventType = it.eventType
            )
        }
    }

    fun mapDomainToEntity(input: Events, eventType: String) = EventsEntity(
        id = input.id,
        name = input.name,
        summary = input.summary,
        mediaCover = input.mediaCover,
        registrants = input.registrants,
        imageLogo = input.imageLogo,
        link = input.link,
        description = input.description,
        ownerName = input.ownerName,
        cityName = input.cityName,
        quota = input.quota,
        beginTime = input.beginTime,
        endTime = input.endTime,
        category = input.category,
        isFav = input.isFav,
        eventType = eventType
    )
}
