package com.yummy.bread

import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.yummy.bread.ui.navigation.BreadNavHost
import com.yummy.bread.ui.theme.BreadTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: BreadViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            val useDarkMode = uiState.isDarkMode ?: isSystemInDarkTheme()
            
            BreadTheme(darkTheme = useDarkMode) {
                BreadNavHost(navController = navController, viewModel = viewModel)
            }
        }
    }
}
