package com.example.myfinance.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myfinance.model.Categories
import com.example.myfinance.ui.theme.getBackgroundColor

@Composable
fun NewOperationScreen(category: Categories, onSave: (Long, Float)->Unit, onCancel: ()->Unit) {
    val newOperationCostState = remember { mutableStateOf(0f) }
    Dialog(onDismissRequest = onCancel) {
        Column(Modifier.fillMaxWidth().background(getBackgroundColor()).height(210.dp).padding(10.dp)) {
            NewOperationDialogTitle()
            NewOperationDialogCategoryName(category.name)
            NewOperationDialogCost(newOperationCostState)
            NewOperationDialogButtons(newOperationCostState, { cost -> onSave(category.id, cost) }, onCancel)
        }
    }
}

@Composable
private fun NewOperationDialogTitle() {
    Text("Новая операция", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}

@Composable
private fun NewOperationDialogCategoryName(categoryName: String) {
    TextField(value = categoryName, enabled = false, onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(disabledContainerColor = Color.Gray),
    )
}

@Composable
private fun NewOperationDialogCost(newOperationCostState: MutableState<Float>) {
    var newOperationCost by newOperationCostState
    var newOperationCostStr by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth().background(Color.Transparent),
        value = newOperationCostStr,
        onValueChange = {
            if(it.isEmpty() || it.last().isDigit() || it.last()=='.') newOperationCostStr = it
            if(it.isEmpty()) newOperationCost = 0f
            newOperationCostStr.toFloatOrNull()?.let { newOperationCost = it }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        supportingText = {
            if (newOperationCost <= 0f)
                Text(text = "Введите сумму операции", color = Color.Red)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun NewOperationDialogButtons(newOperationCostState: MutableState<Float>,
                              onSave: (Float) -> Unit, onCancel: () -> Unit) {
    val newOperationCost by newOperationCostState
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = {
                if (newOperationCost > 0f) onSave(newOperationCost)
                onCancel()
            }) {
            Text(text = "Добавить")
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onCancel) { Text(text = "Закрыть") }
    }
}