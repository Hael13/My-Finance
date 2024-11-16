package com.example.myfinance.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myfinance.FinanceViewModelFactory
import com.example.myfinance.model.FinanceViewModel
import com.example.myfinance.ui.theme.getPrimaryColor
import com.example.myfinance.view.FinanceListPage
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavHostGraph(navController: NavHostController){
    val viewModel: FinanceViewModel = viewModel(factory = FinanceViewModelFactory.Factory)
    Scaffold {
        NavHost(navController = navController, startDestination = NavigationRoutes.list) {
            composable(NavigationRoutes.list) {
                FinanceListPage(navController)
            }
            composable(NavigationRoutes.fromDatePicker) {
                CallDatePicker(
                    viewModel.fromMillis,
                    { chosen -> chosen <= viewModel.toMillis }) { chosen ->
                    viewModel.fromMillis = chosen
                    navController.popBackStack()
                }
            }
            composable(NavigationRoutes.toDatePicker) {
                CallDatePicker(
                    viewModel.toMillis,
                    { chosen -> chosen >= viewModel.fromMillis }) { chosen ->
                    viewModel.toMillis = chosen
                    navController.popBackStack()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallDatePicker(current: Long, dateValidator: (Long)->Boolean,
                   onOkPressed: (Long)->Unit) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = current,
        yearRange = IntRange(2024, LocalDate.now().year))
    Column(modifier = Modifier.padding(top = 40.dp)) {
        DatePicker(
            state = datePickerState, showModeToggle = false, dateValidator = dateValidator,
            colors = DatePickerDefaults.colors(selectedDayContainerColor = getPrimaryColor())
        )
        TextButton(onClick = { onOkPressed(datePickerState.selectedDateMillis!!) }) {
            Text(text = "Установить дату")
        }
    }
}