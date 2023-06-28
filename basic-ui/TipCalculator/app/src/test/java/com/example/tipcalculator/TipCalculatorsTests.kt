package com.example.tipcalculator

import org.junit.Test
import org.junit.Assert.*
import java.text.NumberFormat

class TipCalculatorsTests {
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = calculateTip(false, amount, tipPercent)
        assertEquals(expectedTip, actualTip)
    }
}