package com.ammar.mybottomnavigation.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammar.core.domain.usecase.EventsUseCase

class UpcomingViewModel(eventsUseCase: EventsUseCase) : ViewModel() {

    val upcomingEvents = eventsUseCase.getUpcomingEvents().asLiveData()
}
