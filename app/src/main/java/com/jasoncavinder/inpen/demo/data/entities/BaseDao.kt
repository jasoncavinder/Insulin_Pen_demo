package com.jasoncavinder.inpen.demo.data.entities

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
    fun insertReplace(obj: List<T>): List<Long>

    @Update
    fun update(obj: T): Int

    @Delete
    fun delete(obj: T): Int
}