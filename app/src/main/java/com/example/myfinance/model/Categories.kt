package com.example.myfinance.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name", "isIncome"], unique = true)])
data class Categories(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isIncome: Boolean
)