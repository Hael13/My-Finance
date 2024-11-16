package com.example.myfinance.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myfinance.ui.theme.getBackgroundColor

@Composable
fun NewCategoryScreen(onSave: (String) -> Unit, onCancel: () -> Unit) {
    val newCategoryNameState = remember { mutableStateOf("") }

    Dialog(onDismissRequest = onCancel) {
        Column(modifier = Modifier.fillMaxWidth().background(getBackgroundColor()).height(160.dp).padding(10.dp)) {
            NewCategoryDialogTitle()
            NewCategoryDialogTextField(newCategoryNameState)
            NewCategoryDialogButtons(newCategoryNameState, onSave, onCancel)
        }
    }
}

@Composable
private fun NewCategoryDialogTitle() {
    Text("Новая категория", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}

@Composable
private fun NewCategoryDialogTextField(newCategoryNameState: MutableState<String>) {
    var newCategoryName by newCategoryNameState
    TextField(modifier = Modifier.fillMaxWidth(),
        value = newCategoryName,
        onValueChange = { newCategoryName = it },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        supportingText = {
            if (newCategoryName.isEmpty())
                Text(text = "Введите название новой категории", color = Color.Red)
        }
    )
}

@Composable
private fun NewCategoryDialogButtons(newCategoryNameState: MutableState<String>,
                             onSave: (String) -> Unit, onCancel: () -> Unit) {
    var newCategoryName by newCategoryNameState
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = {
            if (newCategoryName.isNotEmpty()) {
                onSave(newCategoryName)
                onCancel()
            }
        }) { Text(text = "Ок") }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onCancel) { Text(text = "Отмена") }
    }
}