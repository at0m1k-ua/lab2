package com.kpi.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.shape.RoundedCornerShape

class MainActivity : ComponentActivity() {
    private var inputsMap by mutableStateOf(mapOf(
        "coal" to "",
        "oil" to "",
        "gas" to ""
    ))

    private var calculationResult by mutableStateOf("Показники ще не обчислено")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InputsScreen(
                inputsMap = inputsMap,
                onValueChange = { key, value ->
                    inputsMap = inputsMap.toMutableMap().apply { this[key] = value }
                },
                calculationResult = calculationResult,
                onCalculate = { calculate() }
            )
        }
    }

    private fun calculate() {
        val coal = inputsMap["coal"] ?: ""
        val oil = inputsMap["oil"] ?: ""
        val gas = inputsMap["gas"] ?: ""

        calculationResult = "Вугілля - $coal т, Мазут - $oil т, Газ - $gas м³"
    }
}

@Composable
fun InputsScreen(
    inputsMap: Map<String, String>,
    onValueChange: (String, String) -> Unit,
    calculationResult: String,
    onCalculate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Input(
                label = "Донецьке газове вугілля марки ГР",
                units = "т",
                value = inputsMap["coal"] ?: "",
                onValueChange = { onValueChange("coal", it) }
            )
            Input(
                label = "Високосірчистий мазут марки 40",
                units = "т",
                value = inputsMap["oil"] ?: "",
                onValueChange = { onValueChange("oil", it) }
            )
            Input(
                label = "Природний газ із газопроводу Уренгой-Ужгород",
                units = "м³",
                value = inputsMap["gas"] ?: "",
                onValueChange = { onValueChange("gas", it) }
            )
        }

        Text(calculationResult)

        Button(
            modifier = Modifier
                .padding(8.dp)
                .height(72.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = { onCalculate() }
        ) {
            Text(
                "Calculate",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun Input(label: String, units: String, value: String, onValueChange: (String) -> Unit) {
    val regex = Regex("^\\d*\\.?\\d*\$")

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$label, $units")
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.isEmpty() || it.matches(regex)) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.height(48.dp).padding(horizontal = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}
