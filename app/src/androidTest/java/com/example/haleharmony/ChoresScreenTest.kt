package com.example.haleharmony

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.haleharmony.ui.screens.ChoresScreen
import org.junit.Rule
import org.junit.Test

class ChoresScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun choresScreen_displaysAddChoreButton() {
        composeTestRule.setContent {
            ChoresScreen()
        }

        composeTestRule.onNodeWithText("Add Chore").assertExists()
    }
}
