package com.example.myfinance.controller

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.myfinance.model.Categories
import com.example.myfinance.model.Finance
import com.example.myfinance.model.Operations
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("select c.id as id,c.name as name, " +
            "(select total(o.cost) from operations as o where o.date between :begin and :end and c.id = o.id) as cost " +
            "from categories as c where isIncome = :isIncome")
    fun readFinances(begin: Long, end: Long, isIncome: Boolean): Flow<List<Finance>>

    @Query("SELECT total(cost) as total FROM Operations WHERE date >= :begin AND date <= :end")
    fun readTotal(begin: Long, end: Long): Flow<Float>

    @Query("SELECT total(cost) as total FROM Operations WHERE date >= :begin AND date <= :end AND cost < 0")
    fun readTotalExpense(begin: Long, end: Long): Flow<Float>

    @Query("SELECT total(cost) as total FROM Operations WHERE date >= :begin AND date <= :end AND cost > 0")
    fun readTotalIncome(begin: Long, end: Long): Flow<Float>

    @Query("SELECT * FROM Categories WHERE id = :id")
    fun getCategory(id: Long): Flow<Categories>

    @Query("SELECT * FROM Operations WHERE id = :id AND date = :date")
    fun getOperation(id: Long, date: Long): Operations?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Categories): Long

    @Upsert
    fun insert(operation: Operations)
}