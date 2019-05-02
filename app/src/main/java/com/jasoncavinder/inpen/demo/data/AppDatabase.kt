package com.jasoncavinder.inpen.demo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jasoncavinder.inpen.demo.data.entities.alert.Alert
import com.jasoncavinder.inpen.demo.data.entities.alert.AlertDao
import com.jasoncavinder.inpen.demo.data.entities.dose.Dose
import com.jasoncavinder.inpen.demo.data.entities.dose.DoseDao
import com.jasoncavinder.inpen.demo.data.entities.message.Message
import com.jasoncavinder.inpen.demo.data.entities.message.MessageDao
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import com.jasoncavinder.inpen.demo.data.entities.pen.PenDao
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPoint
import com.jasoncavinder.inpen.demo.data.entities.pendatapoint.PenDataPointDao
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import com.jasoncavinder.inpen.demo.data.entities.provider.ProviderDao
import com.jasoncavinder.inpen.demo.data.entities.user.User
import com.jasoncavinder.inpen.demo.data.entities.user.UserDao
import com.jasoncavinder.inpen.demo.data.workers.ProviderDatabaseWorker
import com.jasoncavinder.inpen.demo.utilities.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope

/**
 * The Room database for this app
 */
@Database(
    entities = [
        User::class,
        Message::class,
        Provider::class,
        Pen::class,
        PenDataPoint::class,
        Alert::class,
        Dose::class
    ], version = 20, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun providerDao(): ProviderDao
    abstract fun penDao(): PenDao
    abstract fun penDataPointDao(): PenDataPointDao
    abstract fun doseDao(): DoseDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return _instance ?: synchronized(this) {
                _instance
                    ?: buildDatabase(context, scope).also { _instance = it }
            }
        }


        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
//                .addMigrations()
                .addCallback(AppDatabaseCallback(context, scope))
                .build()
            _instance = instance
            return instance
        }

        private class AppDatabaseCallback(
            val context: Context,
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val request = OneTimeWorkRequestBuilder<ProviderDatabaseWorker>().build()
                WorkManager.getInstance(context).enqueue(request)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                val request = OneTimeWorkRequestBuilder<ProviderDatabaseWorker>().build()
                WorkManager.getInstance(context).enqueue(request)

/*
                _instance?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        PopulateDbAsync(database).execute()
                    }
                }
*/
            }
        }

/*
        private class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {

            private val userDao: UserDao

            init {
                userDao = db.userDao()
            }

            override fun doInBackground(vararg params: Void): Void? {
                // Start the app with a clean database every time.
                // Not needed if you only populate on creation.
//        userDao.deleteAllUsers()

                return null
            }
        }
*/

    }

}
