package com.jasoncavinder.inpen_demo.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Message::class,
        Provider::class,
        Pen::class,
        PenData::class,
        Alert::class,
        Dose::class
    ], version = 4
)
@TypeConverters(Converters::class)
abstract class InPenAppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: InPenAppDatabase? = null

        internal fun getDatabase(context: Context): InPenAppDatabase? {
            return INSTANCE ?: synchronized(InPenAppDatabase::class.java) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

//            if (INSTANCE == null) {
//                synchronized(InPenAppDatabase::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = Room.databaseBuilder(
//                            context.applicationContext,
//                            InPenAppDatabase::class.java, "user.db"
//                        )
//                            // Wipes and rebuilds instead of migrating if no Migration object.
//                            // Migration is not part of this codelab.
//                            .fallbackToDestructiveMigration()
//                            .addCallback(sRoomDatabaseCallback)
//                            .build()
//                    }
//                }
//            }
//            return INSTANCE
        }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                InPenAppDatabase::class.java, "user.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}


//    companion object {
//        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
//
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                super.onOpen(db)
//                // If you want to keep the data through app restarts,
//                // comment out the following line.
//                PopulateDbAsync(INSTANCE!!).execute()
//            }
//        }
//    }

//    private class PopulateDbAsync internal constructor(db: InPenAppDatabase) : AsyncTask<Void, Void, Void>() {
//
//        private val mDao: UserDao
//
//        init {
//            mDao = db.userDao()
//        }
//
//        override fun doInBackground(vararg params: Void): Void? {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate on creation.
//            mDao.deleteAll()
//
//            var word = Word("Hello")
//            mDao.insert(word)
//            word = Word("World")
//            mDao.insert(word)
//            return null
//        }
//    }
