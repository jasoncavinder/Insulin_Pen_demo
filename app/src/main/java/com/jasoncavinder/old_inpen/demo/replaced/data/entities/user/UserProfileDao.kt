///*
// * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
// * This project is licenced to the client of Upwork contract #21949291. It is not
// * licensed for public use. See the LICENSE.md file for details
// */
//
//package com.jasoncavinder.inpen.demo.replaced.data.entities.user
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Query
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
//
//@Dao
//abstract class UserProfileDao(): UserDao() {
//
//    @Query("SELECT * FROM users WHERE id = :userID")
//    protected abstract fun load(userID: String): LiveData<UserProfile>
//
////    @Transaction
////    @Synchronized
//    open fun loadUserProfile(userID: String): LiveData<UserProfile>? {
//        return when (loggedIn(userID)){
//            false -> null
//            else -> load(userID)
//        }
//
//    }
//}