package com.felipearpa.ui.lazy.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName", "Unused")
fun MainViewController(): UIViewController = ComposeUIViewController { App() }
