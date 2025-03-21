package com.ammar.core.domain.usecase

import com.ammar.core.domain.model.Events
import com.ammar.core.domain.repository.IEventsRepository

class EventsInteractor(private val iEventsRepository: IEventsRepository) : EventsUseCase {

    override fun getSearchEvents(query: String?) = iEventsRepository.getSearchEvents(query)

    override fun getUpcomingEvents() = iEventsRepository.getUpcomingEvents()

    override fun getFinishedEvents() = iEventsRepository.getFinishedEvents()

    override fun getFavoriteEvents() = iEventsRepository.getFavoriteEvents()

    override fun setFavoriteEvents(event: Events, state: Boolean, eventType: String) = iEventsRepository.setFavoriteEvents(event, state, eventType)

    override fun getThemeSetting() = iEventsRepository.getThemeSetting()

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) = iEventsRepository.saveThemeSetting(isDarkModeActive)
}
