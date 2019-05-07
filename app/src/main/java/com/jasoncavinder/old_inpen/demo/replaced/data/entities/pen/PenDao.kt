//package com.jasoncavinder.inpen.demo.replaced.data.entities.pen
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Query
//import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//
//@Dao
//abstract class PenDao : BaseDao<Pen> {
//    @Query("SELECT * FROM pens")
//    abstract fun getData(): List<Pen>
//
//    @Query("SELECT * FROM pens WHERE id = :id LIMIT 1")
//    abstract fun getPenById(id: String): LiveData<Pen>
//}
//
//
