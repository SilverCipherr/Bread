package com.yummy.bread.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.yummy.bread.BreadViewModel
import com.yummy.bread.R
import com.yummy.bread.ui.components.BottomNavBar
import com.yummy.bread.ui.components.glassPanelHeavy
import com.yummy.bread.ui.navigation.Screen
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary

import androidx.compose.foundation.border
import com.yummy.bread.ui.components.GlassBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: BreadViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val uiState by viewModel.uiState.collectAsState()

    val showBars = currentRoute != Screen.Splash.route && 
                   currentRoute != Screen.ProfileSetup.route &&
                   currentRoute != Screen.ProfileSelector.route &&
                   currentRoute?.startsWith("lock") != true

    GlassBackground {
        Scaffold(
            topBar = {
                if (showBars) {
                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .glassPanelHeavy(shape = RoundedCornerShape(24.dp))
                    ) {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.bread_logo),
                                        contentDescription = "Logo",
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Bread",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (uiState.profilePictureUri != null) {
                                            Image(
                                                painter = rememberAsyncImagePainter(uiState.profilePictureUri),
                                                contentDescription = "Profile",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.Person,
                                                contentDescription = "Profile",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                                scrolledContainerColor = Color.Transparent,
                                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface,
                                actionIconContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            },
            floatingActionButton = {
                if (showBars && currentRoute == Screen.Dashboard.route) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddTransaction.route) },
                        containerColor = Primary.copy(alpha = 0.9f),
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(64.dp)
                            .border(0.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
                    }
                }
            },
            bottomBar = {
                if (showBars) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        BottomNavBar(
                            selectedRoute = currentRoute ?: Screen.Dashboard.route,
                            onRouteSelected = { route ->
                                if (currentRoute != route) {
                                    navController.navigate(route) {
                                        popUpTo(Screen.Dashboard.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            },
            containerColor = Color.Transparent
        ) { padding ->
            content(padding)
        }
    }
}
