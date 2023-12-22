package com.choiri.bodybuddy.ml

import android.graphics.Bitmap
import com.choiri.bodybuddy.data.Person

interface PoseDetector : AutoCloseable {

    fun estimatePoses(bitmap: Bitmap): List<Person>

    fun lastInferenceTimeNanos(): Long
}
