package com.example.myfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.myfinance.controller.FinanceDatabase
import com.example.myfinance.controller.FinanceRepository
import com.example.myfinance.model.FinanceViewModel
import com.example.myfinance.navigation.NavHostGraph
import com.example.myfinance.ui.theme.MyFinanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FinanceRepository.initialize(FinanceDatabase.getDatabase(applicationContext).financeDao())
        FinanceViewModel.initialize(applicationContext, FinanceRepository.get())
        enableEdgeToEdge()
        setContent {
            MyFinanceTheme {
                NavHostGraph(rememberNavController())
            }
        }
    }
}