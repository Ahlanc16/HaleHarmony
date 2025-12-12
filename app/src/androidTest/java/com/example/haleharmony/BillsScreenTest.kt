package com.example.haleharmony

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.haleharmony.ui.screens.BillsScreen
import org.junit.Rule
import org.junit.Test

class BillsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun billsScreen_displaysAddBillButton() {
        composeTestRule.setContent {
            BillsScreen()
        }

        composeTestRule.onNodeWithText("Add Bill").assertExists()
    }
}
