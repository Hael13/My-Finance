package com.example.myfinance.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myfinance.FinanceViewModelFactory
import com.example.myfinance.model.Categories
import com.example.myfinance.model.Finance
import com.example.myfinance.model.FinanceViewModel
import com.example.myfinance.navigation.NavigationRoutes
import com.example.myfinance.ui.theme.Typography
import com.example.myfinance.ui.theme.getPrimaryColor
import com.example.myfinance.ui.theme.getTextColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FinanceListPage(navController: NavHostController) {
    val viewModel: FinanceViewModel = viewModel(factory = FinanceViewModelFactory.Factory)
    val chooseFrom = {navController.navigate(NavigationRoutes.fromDatePicker)}
    val chooseTo = {navController.navigate(NavigationRoutes.toDatePicker)}

    val pagerState = rememberPagerState(pageCount = { 2 })

    val infoHeight = 160
    val listHeight = LocalConfiguration.current.screenHeightDp - infoHeight - 40

    var isNewCategoryDialogOpen by remember { mutableStateOf(false) }
    val toggleNewCategoryDialog = {isNewCategoryDialogOpen = !isNewCategoryDialogOpen}
    var chosenCategory by remember { mutableLongStateOf(0L) }
    val closeNewOperationDialog = {chosenCategory = 0L}

    val addCategory: (String) -> Unit
    val addOperation: (Long, Float) -> Unit
    if(pagerState.currentPage == 0){
        addCategory = viewModel::addExpenseCategory
        addOperation = viewModel::addExpenseOperation
    }else{
        addCategory = viewModel::addIncomeCategory
        addOperation = viewModel::addIncomeOperation
    }
    if (isNewCategoryDialogOpen)
        NewCategoryScreen({ name -> addCategory(name) }, toggleNewCategoryDialog)
    if (chosenCategory > 0) {
        val category by viewModel.financeRepository.getCategory(chosenCategory)
            .collectAsState(initial = Categories(chosenCategory, "", false))
        NewOperationScreen(category, addOperation, closeNewOperationDialog)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp)
    ) {
        FinanceList(pagerState, listHeight, { chosenCategory = it })
        AddCategoryButton(toggleNewCategoryDialog)
        FinanceInfo(infoHeight, chooseFrom, chooseTo)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FinanceList(pagerState: PagerState, listHeight: Int, onEntryClick: (Long) -> Unit) {
    val viewModel: FinanceViewModel = viewModel(factory = FinanceViewModelFactory.Factory)
    val tabs = arrayOf("Расходы", "Доходы")
    TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
        TabRowDefaults.Indicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
            color = getPrimaryColor()
        )
    }) {
        tabs.forEachIndexed { index, title ->
            Tab(text = { Text(title) }, selected = pagerState.currentPage == index,
                onClick = { }, enabled = false)
        }
    }
    HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
        val financeList by
        if (it == 0) viewModel.getExpenses().collectAsState(initial = listOf())
        else viewModel.getIncomes().collectAsState(initial = listOf())
        LazyColumn(modifier = Modifier.height(listHeight.dp), state = rememberLazyListState()) {
            items(items = financeList, key = { it.id }){
                FinanceItem(it, onEntryClick)
            }
        }
    }
}

@Composable
private fun AddCategoryButton(toggleNewCategoryDialog: ()->Unit) {
    FloatingActionButton(onClick = toggleNewCategoryDialog, modifier = Modifier.padding(top = 10.dp),
        shape = RoundedCornerShape(50.dp), containerColor = getPrimaryColor()){
        Text(text = "+", color = Color.Black, fontSize = 25.sp)
    }
}

@Composable
private fun FinanceInfo(infoHeight: Int, chooseFrom: ()->Unit, chooseTo: ()->Unit) {
    val viewModel: FinanceViewModel = viewModel(factory = FinanceViewModelFactory.Factory)
    val incomeTotal by viewModel.getTotalIncome().collectAsState(0.00)
    val expenseTotal by viewModel.getTotalExpense().collectAsState(0.00)
    val total by viewModel.getTotal().collectAsState(0.00)
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(infoHeight.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = "От: ${viewModel.fromStr}", modifier = Modifier.clickable{chooseFrom()})
            Text(text = "До: ${viewModel.toStr}", modifier = Modifier.clickable{chooseTo()})
        }
        Column {
            Text(text = "Доходы: ${"%.2f".format(incomeTotal)}", color = Color.Green)
            Text(text = "Расходы: ${"%.2f".format(expenseTotal)}", color = Color.Red)
            Text(text = "Остаток: ${"%.2f".format(total)}", color = getTextColor())
        }
    }
}

@Composable
private fun FinanceItem(finance: Finance, onEntryClick: (Long) -> Unit) {
    val costColor: Color =
        if(finance.cost < 0) Color.Red
        else if(finance.cost > 0) Color.Green
        else getTextColor()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(1.dp)
        .border(2.dp, costColor)
        .padding(5.dp)
        .clickable { onEntryClick(finance.id) }) {
        Text(text = finance.name, style = Typography.bodyLarge, color = costColor)
        Text(text = "%.2f".format(finance.cost), style = Typography.bodySmall, color = costColor)
    }
}













