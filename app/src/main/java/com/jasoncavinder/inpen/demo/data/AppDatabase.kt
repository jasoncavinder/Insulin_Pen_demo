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
    ], version = 17, exportSchema = false
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

        // marking the _instance as volatile to ensure atomic AppAccess to the variable
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
                context, AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<ProviderDatabaseWorker>().build()
                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build()
        }
    }
}
//            Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java, "user.db"
//            )
////                .addMigrations()
////                .addCallback(sRoomDatabaseCallback)
//                .build()

/*
private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // If you want to keep the data through app restarts,
        // comment out the following line.
        PopulateDbAsync(INSTANCE!!).execute()
    }
}
*/

/*
    private class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {

        private val mDao: UserDao

        init {
            mDao = db.userDao()
        }

        override fun doInBackground(vararg params: Void): Void? {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll()

            var word = Word("Hello")
            mDao.insert(word)
            word = Word("World")
            mDao.insert(word)
            return null
        }
    }
*/