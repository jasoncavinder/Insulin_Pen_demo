///*
// * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
// * This project is licenced to the client of Upwork contract #21949291. It is not
// * licensed for public use. See the LICENSE.md file for details
// */
//
//package com.jasoncavinder.inpen.demo.replaced.data.entities.pen
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPoint
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//
//class PenWithData {
//    @Embedded
//    lateinit var pen: Pen
//
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "penID"
//    )
//    lateinit var penDataPoints: List<PenDataPoint>
//
//    init {
//
//    }
//}
