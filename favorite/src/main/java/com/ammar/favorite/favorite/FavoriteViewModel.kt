package com.ammar.favorite.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ammar.core.domain.usecase.EventsUseCase

class FavoriteViewModel(eventsUseCase: EventsUseCase) : ViewModel() {
    val favoriteEvents = eventsUseCase.getFavoriteEvents().asLiveData()
}
