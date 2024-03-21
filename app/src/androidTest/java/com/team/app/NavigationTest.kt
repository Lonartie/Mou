package com.team.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun `first page is Home`() {
        // Check if the top bar has a text "Home"
        composeTestRule
            .onNodeWithTag("TitleBarText")
            .assertTextEquals("Home")

        // Optionally, you can check if other elements are displayed on the homepage
        composeTestRule
            .onNodeWithText("Homepage")
            .assertIsDisplayed()
    }

    @Test
    fun `can navigate to Page A`() {
        // Open navigation drawer
        composeTestRule
            .onNodeWithTag("NavDrawerButton")
            .assertIsDisplayed()
            .performClick()

        // Click on "Page A" in the navigation drawer
        composeTestRule
            .onNodeWithText("Page A")
            .assertIsDisplayed()
            .performClick()

        // Check if the top bar has a text "Page A"
        composeTestRule
            .onNodeWithTag("TitleBarText")
            .assertTextEquals("Page A")
    }
}