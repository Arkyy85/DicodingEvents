package com.ammar.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Events(
    var id: Int?,
    var name: String? = "",
    var summary: String? = "",
    var mediaCover: String? = "",
    var registrants: Int? = 0,
    var imageLogo: String? = "",
    var link: String? = "",
    var description: String? = "",
    var ownerName: String? = "",
    var cityName: String? = "",
    var quota: Int? = 0,
    var beginTime: String? = "",
    var endTime: String? = "",
    var category: String? = "",
    var isFav: Boolean = false,
    var eventType: String
) : Parcelable
