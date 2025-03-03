package com.ammar.mybottomnavigation.ui.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammar.core.domain.usecase.EventsUseCase

class FinishedViewModel(eventsUseCase: EventsUseCase) : ViewModel() {

    val finishedEvents = eventsUseCase.getFinishedEvents().asLiveData()
}
