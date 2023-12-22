package com.choiri.bodybuddy.data

import android.graphics.PointF
import com.choiri.bodybuddy.data.BodyPart

data class KeyPoint(val bodyPart: BodyPart, var coordinate: PointF, val score: Float)
