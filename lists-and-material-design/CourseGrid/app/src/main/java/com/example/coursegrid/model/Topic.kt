package com.example.coursegrid.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val titleRes: Int,
    val availableCourses: Int,
    @DrawableRes val imageRes: Int
)
