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
        "oil" to ""
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
        val n_zu = 0.985

        val coal_qri = 20.47
        val coal_a_vin = 0.8
        val coal_a_r = 25.2

        val oil_qri = 40.4
        val oil_a_vin = 1
        val oil_a_r = 0.15

        val coal_amt = inputsMap["coal"]?.toDoubleOrNull() ?: .0
        val oil_amt = inputsMap["oil"]?.toDoubleOrNull() ?: .0

        val coal_k_tv = 1000000*coal_a_vin*coal_a_r*(1 - n_zu)/coal_qri/(100 - 1.5)
        val coal_e_tv = coal_k_tv*coal_qri*coal_amt/1000000

        val oil_k_tv = 1000000*oil_a_vin*oil_a_r*(1 - n_zu)/oil_qri/100
        val oil_e_tv = oil_k_tv*oil_qri*oil_amt/1000000

        calculationResult =
            """
                Kтв вугілля - %.2f г/ГДж\n
                Eтв вугілля - %.2f т\n
                Kтв мазуту - %.2f г/ГДж\n
                Eтв мазуту - %.2f т\n
            """.trimIndent().format(coal_k_tv, coal_e_tv, oil_k_tv, oil_e_tv)
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
        }

        Text(
            calculationResult,
            modifier = Modifier.padding(8.dp)
        )

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
            modifier = Modifier.height(64.dp).padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}
