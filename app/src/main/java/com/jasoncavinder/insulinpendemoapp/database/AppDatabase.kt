/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jasoncavinder.insulinpendemoapp.database.entities.alert.Alert
import com.jasoncavinder.insulinpendemoapp.database.entities.alert.AlertDao
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseDao
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageDao
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.PaymentDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenDao
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPoint
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.PenDataPointDao
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.ProviderDao
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserDao
import com.jasoncavinder.insulinpendemoapp.database.utilities.Converters
import com.jasoncavinder.insulinpendemoapp.database.utilities.ProviderDatabaseWorker
import com.jasoncavinder.insulinpendemoapp.utilities.DATABASE_NAME

@Database(
    entities = [
        User::class,
        Provider::class,
        Pen::class,
        PenDataPoint::class,
        Payment::class,
        Dose::class,
        Message::class,
        Alert::class
    ],
    version = 43,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun providerDao(): ProviderDao
    abstract fun penDao(): PenDao
    abstract fun penDataPointDao(): PenDataPointDao
    abstract fun paymentDao(): PaymentDao
    abstract fun doseDao(): DoseDao
    abstract fun messageDao(): MessageDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return _instance ?: synchronized(this) {
                _instance
                    ?: buildDatabase(context).also { _instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(
                    AppDatabaseCallback(
                        context
                    )
                )
                .fallbackToDestructiveMigration()
//                .addMigrations()  // TODO: Add data migrations to database
                .build()
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                _instance?.let { _ ->
                    val request = OneTimeWorkRequestBuilder<ProviderDatabaseWorker>().build()
                    WorkManager.getInstance(context).enqueue(request)
                }
            }
        }
    }
}
