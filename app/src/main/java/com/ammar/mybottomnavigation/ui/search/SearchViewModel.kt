package com.ammar.mybottomnavigation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammar.core.domain.usecase.EventsUseCase

class SearchViewModel(private val eventsUseCase: EventsUseCase) : ViewModel() {
    fun searchEvents(search: String) = eventsUseCase.getSearchEvents(search).asLiveData()
}
