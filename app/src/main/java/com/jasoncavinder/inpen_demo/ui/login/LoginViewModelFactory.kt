//package com.jasoncavinder.inpen_demo.ui.ui
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.jasoncavinder.inpen_demo.data.LocalLoginDataSource
//import com.jasoncavinder.inpen_demo.persistence.LoginDataSource
//import com.jasoncavinder.inpen_demo.data.LoginRepository
//
//
///**
// * ViewModel provider factory to instantiate LoginViewModel.
// * Required given LoginViewModel has a non-empty constructor
// */
//class LoginViewModelFactory : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
//            return LoginViewModel(
//                loginRepository = LoginRepository(
////                    dataSource = LoginDataSource(),
//                    application = Application()
//                )
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
