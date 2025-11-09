package com.example.tipscalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipscalc.ui.theme.TipsCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipsCalcTheme {
                TipCalculatorScreen()
            }
        }
    }
}

@Composable
fun TipCalculatorScreen() {
    var billAmount by remember { mutableStateOf("") }
    var dishCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0f) }
    var selectedDiscount by remember { mutableStateOf(0) }

    val dishCountInt = dishCount.toIntOrNull() ?: 0

    val discountIndex = when {
        dishCountInt > 10 -> 3
        dishCountInt >= 6 -> 2
        dishCountInt >= 3 -> 1
        dishCountInt >= 1 -> 0
        else -> 0
    }

    LaunchedEffect(dishCountInt) {
        selectedDiscount = discountIndex
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Сумма заказа
        Text("Сумма заказа:")
        OutlinedTextField(
            value = billAmount,
            onValueChange = { billAmount = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        // Количество блюд
        Text("Количество блюд:")
        OutlinedTextField(
            value = dishCount,
            onValueChange = { dishCount = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Чаевые
        Text("Чаевые:")
        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )
        Text("${tipPercent.toInt()}%")

        // Скидка
        Text("Скидка:")
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(3, 5, 7, 10).forEachIndexed { index, percent ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedDiscount == index,
                        onClick = null // Программный выбор
                    )
                    Text("$percent%")
                }
            }
        }

        val bill = billAmount.toDoubleOrNull() ?: 0.0
        val tipAmount = bill * tipPercent / 100
        val discountPercent = when (selectedDiscount) {
            0 -> 3; 1 -> 5; 2 -> 7; else -> 10
        }
        val discountAmount = bill * discountPercent / 100
        val total = bill + tipAmount - discountAmount

        Column {
            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Чаевые:")
                Text("%.2f".format(tipAmount))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Скидка:")
                Text("-%.2f".format(discountAmount))
            }
            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("К оплате:")
                Text("%.2f".format(total), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipsCalcTheme {
        TipCalculatorScreen()
    }
}