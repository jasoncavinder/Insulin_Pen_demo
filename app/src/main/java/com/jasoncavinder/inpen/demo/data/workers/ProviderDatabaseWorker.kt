package com.jasoncavinder.inpen.demo.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jasoncavinder.inpen.demo.data.AppDatabase
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import com.jasoncavinder.inpen.demo.utilities.PROVIDER_DATA_FILENAME
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
                    val database = AppDatabase.getInstance(applicationContext, this)
                    database.providerDao().insertReplace(providerList)

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
