/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel

class LoginActivity : AppCompatActivity() {

    val TAG: String by lazy { this::class.java.simpleName }
    lateinit var viewModel: MainViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Entered onCreate")
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders
            .of(this)
            .get(MainViewModel::class.java)

        navController = findNavController(R.id.nav_host_login)


    }

}

