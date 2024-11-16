package com.example.myfinance

import android.app.Application

class FinanceApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}