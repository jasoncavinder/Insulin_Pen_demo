/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.pen

import androidx.room.Embedded
import androidx.room.Relation
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPoint

class PenWithDataPoints {

    @Embedded
    var pen: Pen? = null

    @Relation(parentColumn = "id", entityColumn = "penId", entity = PenDataPoint::class)
    var dataPoints: List<PenDataPoint> = arrayListOf()

}
