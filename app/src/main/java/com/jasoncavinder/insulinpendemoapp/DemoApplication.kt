package com.jasoncavinder.insulinpendemoapp

import android.app.Application

class DemoApplication : Application() {
    private val TAG by lazy { this::class.java.simpleName }

/* no longer using internal storage, loading photos directly from raw
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
*/
}
