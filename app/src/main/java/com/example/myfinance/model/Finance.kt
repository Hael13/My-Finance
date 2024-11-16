package com.example.myfinance.model

import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDate

data class Finance(
    val id: Long,
    val name: String,
    val cost: Float
)