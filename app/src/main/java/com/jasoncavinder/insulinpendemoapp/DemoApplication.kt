package com.jasoncavinder.insulinpendemoapp

import android.R.raw
import android.app.Application
import android.content.Context
import android.util.Log
import java.io.File

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        // Copy demo files to internal storage if they do not exist yet
        var count = 0
        for (field in raw::class.java.fields) {
            if (field.name.length == 40
                && field.name.endsWith(".jpg")
                && !File(field.name).exists()
            ) {
                resources.openRawResource(field.getInt(field))
                    .copyTo(applicationContext.openFileOutput(field.name, Context.MODE_PRIVATE))
                count = count.inc()
            }
        }
        Log.d("Application Startup", "Copied %d picture to internal storage.")

    }
}
