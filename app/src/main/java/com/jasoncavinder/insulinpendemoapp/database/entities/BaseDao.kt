package com.jasoncavinder.insulinpendemoapp.database.entities

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplace(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg obj: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplace(vararg obj: T): List<Long>

    @Update
    fun update(obj: T): Int

    @Update
    fun update(vararg obj: T)

    @Delete
    fun delete(obj: T): Int

}
