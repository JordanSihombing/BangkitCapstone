package com.choiri.bodybuddy.tracker

import com.choiri.bodybuddy.data.Person

data class Track(
    val person: Person,
    val lastTimestamp: Long
)
