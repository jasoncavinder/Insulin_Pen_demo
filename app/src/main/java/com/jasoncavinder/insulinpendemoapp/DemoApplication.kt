package com.jasoncavinder.insulinpendemoapp

import android.app.Application
import android.content.Context

class DemoApplication : Application() {
    private val TAG by lazy { this::class.java.simpleName }
    lateinit var demoAppContext: Context
        private set

}
