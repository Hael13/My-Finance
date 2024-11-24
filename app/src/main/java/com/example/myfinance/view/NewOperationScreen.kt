package com.example.myfinance.view

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myfinance.model.Categories
import com.example.myfinance.model.FinanceViewModel
import com.example.myfinance.ui.theme.getPrimaryColor
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewOperationScreen(navController: NavHostController, categoryId: Long/*, onSave: (Long, Float)->Unit, onCancel: ()->Unit*/) {
    val itemsCost = remember { mutableStateListOf("") }
    val viewModel: FinanceViewModel = FinanceViewModel.get()
    val category by viewModel.financeRepository.getCategory(categoryId)
        .collectAsState(initial = Categories(categoryId, "", false))

    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val listHeightMax = LocalConfiguration.current.screenHeightDp - 150
    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    var listHeight by remember { mutableStateOf(listHeightMax) }

    LaunchedEffect(key1 = keyboardHeight) {
        launch {
            val screenPixelDensity = displayMetrics.density
            val dpValue = keyboardHeight / screenPixelDensity
            listHeight = listHeightMax - dpValue.toInt()
            //scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        NewOperationDialogTitle()
        NewOperationDialogCategoryName(category.name)

        LazyColumn(Modifier.heightIn(max = listHeight.dp)){
            items(itemsCost.size){
                NewOperationDialogCost(itemsCost, it)
            }
        }
        FloatingActionButton(onClick = { itemsCost.add("") },
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            shape = CircleShape, containerColor = getPrimaryColor()
        ) { Text(text = "+", color = Color.Black, fontSize = 15.sp) }

        NewOperationDialogButtons(itemsCost,
            { cost -> viewModel.addOperation(category.id, category.isIncome, cost) },
            {navController.popBackStack()})

    }
}

@Composable
private fun NewOperationDialogTitle() {
    Text("Новая операция", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}

@Composable
private fun NewOperationDialogCategoryName(categoryName: String) {
    TextField(
        value = categoryName, enabled = false, onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(disabledContainerColor = Color.Gray),
    )
}

@Composable
private fun NewOperationDialogCost(itemsCost: MutableList<String>, index: Int/*newOperationCostState: MutableState<Float>*/) {
    //var newOperationCost by newOperationCostState
    //var newOperationCostStr by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        value = itemsCost[index],
        onValueChange = {
            if(it.isEmpty() || it.last().isDigit() || it.last()=='.' && itemsCost[index].last()!='.')
                itemsCost[index] = it
            //if(it.isEmpty()) newOperationCost = 0f
            //newOperationCostStr.toFloatOrNull()?.let { newOperationCost = it }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        supportingText = {
            if (itemsCost[index].isEmpty() || itemsCost[index].first() == '-')
                Text(text = "Введите сумму операции", color = Color.Red)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun NewOperationDialogButtons(itemsCost: List<String>,//newOperationCostState: MutableState<Float>,
                              onSave: (Float) -> Unit, onCancel: () -> Unit) {
    //val newOperationCost by newOperationCostState
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = {
                if(itemsCost.stream().anyMatch({it.isEmpty()})) return@TextButton
                val cost = itemsCost.stream().map { it.toFloat() }.reduce({a,b->a+b}).get()
                onSave(cost)
                onCancel()
            }) {
            Text(text = "Добавить")
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onCancel) { Text(text = "Закрыть") }
    }
}