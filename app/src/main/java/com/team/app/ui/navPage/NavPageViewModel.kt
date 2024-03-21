package com.team.app.ui.navPage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Abc
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import com.team.app.ui.home.Home
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavPageViewModel @Inject constructor() : ViewModel() {
    enum class PageType {
        HOME,
        A, B, C
    }

    class Page(
        val name: String,
        val icon: ImageVector,
        val loader: @Composable () -> Unit
    )

    private val mod = Modifier
        .fillMaxSize()
        .wrapContentHeight(align = Alignment.CenterVertically)
    private val centeredText: @Composable (String) -> Unit =
        { msg -> Text(text = msg, modifier = mod, textAlign = TextAlign.Center) }

    val pageMap = listOf(
        PageType.HOME to Page("Home", Icons.Rounded.Home) { Home() },
        PageType.A to Page("Page A", Icons.Rounded.Abc) { centeredText("Page A") },
        PageType.B to Page("Page B", Icons.Rounded.Abc) { centeredText("Page B") },
        PageType.C to Page("Page C", Icons.Rounded.Abc) { centeredText("Page C") },
    )

    private fun find(type: PageType): Page {
        return pageMap
            .first { p -> p.first == type }
            .second
    }

    private val currentTitle = mutableStateOf(find(PageType.HOME).name)
    private val pageLoader = mutableStateOf(find(PageType.HOME).loader)

    fun getCurrentTitle(): String {
        return currentTitle.value
    }

    fun getActivePage(): @Composable () -> Unit {
        return pageLoader.value
    }

    fun setActivePage(pageType: PageType) {
        currentTitle.value = find(pageType).name
        pageLoader.value = find(pageType).loader
    }
}