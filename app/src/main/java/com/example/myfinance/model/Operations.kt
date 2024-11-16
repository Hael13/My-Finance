package com.example.myfinance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["id", "date"], foreignKeys = [ForeignKey(Categories::class, ["id"], ["id"])])
data class Operations(
    val id: Long,
    val date: Long,
    val cost: Float
)
