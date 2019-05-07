/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.utilities

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.utilities.PROVIDER_DATA_FILENAME
import kotlinx.coroutines.coroutineScope

class ProviderDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy { ProviderDatabaseWorker::class.java.simpleName }

    override suspend fun doWork(): Result = coroutineScope {

        try {
            applicationContext.assets.open(PROVIDER_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val providerType = object : TypeToken<List<Provider>>() {}.type
                    val providerList: List<Provider> = Gson().fromJson(jsonReader, providerType)
                    val database = AppDatabase.getInstance(applicationContext)
                    database.providerDao().insertReplace(*providerList.toTypedArray())

                    Result.success()
                }
            }
            Log.d(TAG, "Filled database")
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error filling database", ex)
            Result.failure()
        }
    }
}
