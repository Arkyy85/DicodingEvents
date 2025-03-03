package com.ammar.favorite.favorite

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapsModule = module {
    viewModel { FavoriteViewModel(get()) }
}