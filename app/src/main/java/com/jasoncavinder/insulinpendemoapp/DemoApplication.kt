package com.jasoncavinder.insulinpendemoapp

import android.app.Application
import android.content.Context
import android.util.Log
import java.io.File

class DemoApplication : Application() {
    private val TAG by lazy { this::class.java.simpleName }
    override fun onCreate() {
        super.onCreate()


        // Copy demo files to internal storage if they do not exist yet
        var count = 0
        Log.d(TAG, "Preparing to copy provider photos")
        Log.d(TAG, R.raw::class.java.fields.toString())
        for (field in R.raw::class.java.fields) {
            if (field.name.startsWith("doctor")
                && !File(field.name).exists()
            ) {
                resources.openRawResource(field.getInt(field))
                    .copyTo(applicationContext.openFileOutput(field.name, Context.MODE_PRIVATE))
                count = count.inc()
            }
        }
        Log.d(TAG, "Copied %d picture to internal storage.".format(count))

    }
}
