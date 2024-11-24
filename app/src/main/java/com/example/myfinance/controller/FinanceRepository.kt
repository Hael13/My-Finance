package com.example.myfinance.controller

import android.text.format.DateUtils
import com.example.myfinance.model.Categories
import com.example.myfinance.model.Finance
import com.example.myfinance.model.Operations
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class FinanceRepository private constructor(private val financeDao: FinanceDao) {

    companion object {
        private var INSTANCE: FinanceRepository? = null
        fun initialize(financeDao: FinanceDao) {
            if (INSTANCE == null) {
                INSTANCE = FinanceRepository(financeDao)
            }
        }
        fun get(): FinanceRepository {
            return INSTANCE ?: throw IllegalStateException("FinanceDao must be initialized")
        }
    }

    fun getExpenses(begin: Long, end: Long): Flow<List<Finance>> {
        return financeDao.readFinances(begin, end, false)
    }

    fun getIncomes(begin: Long, end: Long): Flow<List<Finance>> {
        return financeDao.readFinances(begin, end, true)
    }

    fun getTotal(begin: Long, end: Long): Flow<Float> {
        return financeDao.readTotal(begin, end)
    }

    fun getTotalExpense(begin: Long, end: Long): Flow<Float> {
        return financeDao.readTotalExpense(begin, end)
    }

    fun getTotalIncome(begin: Long, end: Long): Flow<Float> {
        return financeDao.readTotalIncome(begin, end)
    }

    fun getCategory(id: Long): Flow<Categories> {
        return financeDao.getCategory(id)
    }

    fun addExpenseCategory(name: String): Boolean {
        return financeDao.insert(Categories(name = name, isIncome = false)) > 0
    }

    fun addIncomeCategory(name: String): Boolean {
        return financeDao.insert(Categories(name = name, isIncome = true)) > 0
    }

    fun addOperation(id: Long, cost: Float) {
        val date = LocalDate.now().toEpochDay()*DateUtils.DAY_IN_MILLIS
        val operation = financeDao.getOperation(id, date)
        if(operation != null){
            financeDao.insert(Operations(id, date, operation.cost+cost))
        }else {
            financeDao.insert(Operations(id, date, cost))
        }
    }
}