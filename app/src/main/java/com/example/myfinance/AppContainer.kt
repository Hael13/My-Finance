package com.example.myfinance

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.Room
import com.example.myfinance.controller.FinanceDatabase
import com.example.myfinance.controller.FinanceRepository

class AppContainer(private val context: Context) {
    val financeRepository by
        lazy { FinanceRepository(FinanceDatabase.getDatabase(context).financeDao()) }
}