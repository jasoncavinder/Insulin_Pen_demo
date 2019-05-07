//package com.jasoncavinder.inpen_demo.util
//
//class LoginStatus {
//
//    var userID: String? = null
//    var timestamp: Long = System.currentTimeMillis()
//
//    fun isLoggedIn(): Boolean {
//        return userID != null && System.currentTimeMillis() + 900000 < timestamp
//    }
//
//    fun update(userID: String) {
//        this.userID = userID
//        this.timestamp = System.currentTimeMillis()
//    }
//
//}