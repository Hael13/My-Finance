package com.example.myfinance.model

import android.content.Context
import android.text.format.DateUtils
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfinance.controller.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
class FinanceViewModel private constructor(val context: Context,
                                           val financeRepository: FinanceRepository): ViewModel() {

    companion object {
        const val insertErrorText = "Не удалось создать новую запись"
        const val insertSuccessText = "Новая запись создана"
        private var INSTANCE: FinanceViewModel? = null
        fun initialize(context: Context, financeRepository: FinanceRepository) {
            if (INSTANCE == null) {
                INSTANCE = FinanceViewModel(context, financeRepository)
            }
        }
        fun get(): FinanceViewModel {
            return INSTANCE ?: throw IllegalStateException("FinanceViewModel must be initialized")
        }
    }

    var fromMillis: Long
    var toMillis: Long
    init {
        val initDate = LocalDate.now().toEpochDay()*DateUtils.DAY_IN_MILLIS
        fromMillis = initDate
        toMillis = initDate
    }

    val fromStr: String
        get() = LocalDate.ofEpochDay(fromMillis/ DateUtils.DAY_IN_MILLIS).format(DateTimeFormatter.ISO_DATE)//dateRange.selectedStartDateMillis!!
    val toStr: String
        get() = LocalDate.ofEpochDay(toMillis/ DateUtils.DAY_IN_MILLIS).format(DateTimeFormatter.ISO_DATE)//dateRange.selectedEndDateMillis!!

    fun addExpenseCategory(name: String){
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO){
                financeRepository.addExpenseCategory(name.lowercase())
            }
            if(success){
                Toast.makeText(context, insertSuccessText, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, insertErrorText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addIncomeCategory(name: String){
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO){
                financeRepository.addIncomeCategory(name.lowercase())
            }
            if(success){
                Toast.makeText(context, insertSuccessText, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, insertErrorText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addOperation(id: Long, isIncome: Boolean, cost: Float){
        viewModelScope.launch(Dispatchers.IO) {
            if(isIncome) financeRepository.addOperation(id, cost)
            else financeRepository.addOperation(id, -cost)
        }
    }

    fun addExpenseOperation(id: Long, cost: Float){
        addIncomeOperation(id, -cost)
    }

    fun addIncomeOperation(id: Long, cost: Float){
        viewModelScope.launch(Dispatchers.IO) {
            financeRepository.addOperation(id, cost)
        }
    }

    fun getExpenses(): Flow<List<Finance>> {
        return financeRepository.getExpenses(fromMillis, toMillis)
    }

    fun getIncomes(): Flow<List<Finance>> {
        return financeRepository.getIncomes(fromMillis, toMillis)
    }

    fun getTotal(): Flow<Float> {
        return financeRepository.getTotal(fromMillis, toMillis)
    }

    fun getTotalIncome(): Flow<Float> {
        return financeRepository.getTotalIncome(fromMillis, toMillis)
    }

    fun getTotalExpense(): Flow<Float> {
        return financeRepository.getTotalExpense(fromMillis, toMillis)
    }
}