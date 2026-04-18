package com.felipearpa.ui.lazy.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun App(useDarkTheme: Boolean = false) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ShowcaseScreen()
        }
    }
}
