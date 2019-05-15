/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jasoncavinder.insulinpendemoapp.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    val TAG: String by lazy { this::class.java.simpleName }
    lateinit var _loginViewModel: LoginViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _loginViewModel = ViewModelProviders
            .of(this)
            .get(LoginViewModel::class.java)

        navController = findNavController(R.id.nav_host_login)


    }

}

