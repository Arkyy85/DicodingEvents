package com.ammar.mybottomnavigation.ui.detail

import androidx.lifecycle.ViewModel
import com.ammar.core.domain.model.Events
import com.ammar.core.domain.usecase.EventsUseCase

class DetailsViewModel(private val eventsUseCase: EventsUseCase) : ViewModel() {

    fun setFavoriteTourism(events: Events, newStatus: Boolean, eventType: String) =
        eventsUseCase.setFavoriteEvents(events, newStatus, eventType)
}
