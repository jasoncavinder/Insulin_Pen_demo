//package com.jasoncavinder.inpen_demo.z.utilities
//
//import android.content.Context
//import com.jasoncavinder.inpen.demo.data.AppDatabase
//import com.jasoncavinder.inpen.demo.data.entities.user.UserRepository
//import com.jasoncavinder.inpen_demo.z.ui.ui.LoginViewModelFactory
//import com.jasoncavinder.inpen_demo.z.viewmodels.user.UserViewModelFactory
//
//object InjectorUtils {
//    private fun getUserRepository(context: Context): UserRepository {
//        return UserRepository.getInstance(
//            AppDatabase.getInstance(context.applicationContext).userDao()
//        )
//    }
//
//    fun provideUserViewModelFactory(
//        context: Context,
//        userID: String
//    ): UserViewModelFactory {
//        return UserViewModelFactory(getUserRepository(context), userID)
//    }
//
//    fun provideLoginViewModelFactory(
//        context: Context
//    ): LoginViewModelFactory {
//        return LoginViewModelFactory(getUserRepository(context))
//    }
//}