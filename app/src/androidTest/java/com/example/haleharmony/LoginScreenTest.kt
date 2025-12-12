package com.example.haleharmony

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.haleharmony.ui.screens.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysHaleHarmonyText() {
        composeTestRule.setContent {
            LoginScreen(onLoginSuccess = {}) // on Login Success can be an empty lambda for this test
        }

        composeTestRule.onNodeWithText("HaleHarmony").assertExists()
    }
}
