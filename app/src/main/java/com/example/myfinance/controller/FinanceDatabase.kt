package com.example.myfinance.controller

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import com.example.myfinance.model.Categories
import com.example.myfinance.model.Operations

@Database(entities = [Categories::class, Operations::class], version = 1, exportSchema = false)
abstract class FinanceDatabase: RoomDatabase() {
    abstract fun financeDao(): FinanceDao

    companion object {
        @Volatile
        private var Instance: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FinanceDatabase::class.java, "fin")
                    .build().also { Instance = it }
            }
        }
    }
}