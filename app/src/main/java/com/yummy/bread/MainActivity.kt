package com.yummy.bread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.yummy.bread.ui.navigation.BreadNavHost
import com.yummy.bread.ui.theme.BreadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: BreadViewModel = viewModel()
            BreadTheme {
                BreadNavHost(navController = navController, viewModel = viewModel)
            }
        }
    }
}
