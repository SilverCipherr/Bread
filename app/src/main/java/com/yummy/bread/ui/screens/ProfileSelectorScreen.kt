package com.yummy.bread.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yummy.bread.BreadViewModel
import com.yummy.bread.data.Profile
import com.yummy.bread.ui.components.GlassCard
import com.yummy.bread.ui.theme.Background
import com.yummy.bread.ui.theme.Primary

@Composable
fun ProfileSelectorScreen(
    viewModel: BreadViewModel,
    onProfileSelected: (String) -> Unit,
    onAddAccount: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Text(
            "Who's spending?",
            style = MaterialTheme.typography.displayMedium,
            color = Primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(uiState.profiles) { profile ->
                ProfileCard(
                    profile = profile,
                    onClick = { onProfileSelected(profile.id) }
                )
            }
        }

        Button(
            onClick = onAddAccount,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.3f))
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Add Account", color = Primary, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (profile.pictureUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profile.pictureUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                }
            }
            
            Text(
                profile.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
