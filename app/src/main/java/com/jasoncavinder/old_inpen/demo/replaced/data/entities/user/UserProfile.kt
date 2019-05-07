///*
// * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
// * This project is licenced to the client of Upwork contract #21949291. It is not
// * licensed for public use. See the LICENSE.md file for details
// */
//
//package com.jasoncavinder.inpen.demo.replaced.data.entities.user
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenWithDataPoints
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//
//class UserProfile {
//    @Embedded
//    lateinit var user: User
//
//    @Relation(
//        parentColumn = "providerID",
//        entityColumn = "id"
//    )
//    lateinit var provider: List<Provider>
//
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "penPoints.userID")
//    lateinit var penPoints: List<PenWithDataPoints>
//}
