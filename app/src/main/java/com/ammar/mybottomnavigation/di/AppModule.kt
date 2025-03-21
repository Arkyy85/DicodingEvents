package com.ammar.mybottomnavigation.di

import com.ammar.core.domain.usecase.EventsInteractor
import com.ammar.core.domain.usecase.EventsUseCase
import com.ammar.mybottomnavigation.ui.MainViewModel
import com.ammar.mybottomnavigation.ui.detail.DetailsViewModel
import com.ammar.mybottomnavigation.ui.finished.FinishedViewModel
import com.ammar.mybottomnavigation.ui.home.HomeViewModel
import com.ammar.mybottomnavigation.ui.search.SearchViewModel
import com.ammar.mybottomnavigation.ui.upcoming.UpcomingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<EventsUseCase> { EventsInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { UpcomingViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { FinishedViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { MainViewModel(get()) }
}
