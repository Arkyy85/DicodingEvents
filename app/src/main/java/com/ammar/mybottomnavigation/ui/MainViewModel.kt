package com.ammar.mybottomnavigation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ammar.core.domain.usecase.EventsUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val eventsUseCase: EventsUseCase) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return eventsUseCase.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            eventsUseCase.saveThemeSetting(isDarkModeActive)
        }
    }
}
