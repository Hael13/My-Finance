package com.example.myfinance

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myfinance.model.FinanceViewModel

object FinanceViewModelFactory {
    private var financeViewModel: FinanceViewModel? = null
    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if(financeViewModel == null) {
                val context = extras.financeApplication().applicationContext
                val financeRepository = extras.financeApplication().container.financeRepository
                financeViewModel =
                    FinanceViewModel(context, financeRepository)
            }
            return financeViewModel as T
        }
    }
}

fun CreationExtras.financeApplication(): FinanceApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FinanceApplication)