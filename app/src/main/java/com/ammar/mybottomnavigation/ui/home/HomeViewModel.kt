package com.ammar.mybottomnavigation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammar.core.domain.usecase.EventsUseCase

class HomeViewModel(eventsUseCase: EventsUseCase) : ViewModel() {

    val upcomingEvents = eventsUseCase.getUpcomingEvents().asLiveData()
    val finishedEvents = eventsUseCase.getFinishedEvents().asLiveData()
}
