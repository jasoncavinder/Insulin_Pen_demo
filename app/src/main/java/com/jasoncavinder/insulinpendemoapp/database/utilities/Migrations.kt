/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.utilities

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations : ArrayList<Migration>() {
    val from46to47 = object : Migration(46, 47) {
        override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("")
        }
    }

    init {
        for (migration in this) this.add(migration)
    }

}
