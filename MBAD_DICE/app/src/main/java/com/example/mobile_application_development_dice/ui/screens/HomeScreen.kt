/**
 * CourseWork : Mobile Application Development - CW 1
 * Student name : Himath De Silva
 * IIT ID : 20231127
 * UOW ID : W2051895
 */

package com.example.mobile_application_development_dice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_application_development_dice.R
import com.example.mobile_application_development_dice.ui.theme.DiceBlue
import com.example.mobile_application_development_dice.ui.theme.DiceGreen
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow

/**
 * Home screen with options to start a new game or view about information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewGameClick: (Int) -> Unit,
    onAboutClick: () -> Unit
) {
    var showTargetScoreDialog by rememberSaveable { mutableStateOf(false) }
    
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Dice Game",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = Color(0x80000000),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = { showTargetScoreDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DiceBlue,
                        disabledContainerColor = DiceBlue.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(elevation = 6.dp)
                ) {
                    Text(
                        text = "New Game",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onAboutClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DiceGreen,
                        disabledContainerColor = DiceGreen.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(elevation = 6.dp)
                ) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }
        }
    }
    
    if (showTargetScoreDialog) {
        TargetScoreDialog(
            onDismiss = { showTargetScoreDialog = false },
            onConfirm = { targetScore ->
                showTargetScoreDialog = false
                onNewGameClick(targetScore)
            }
        )
    }
}

/**
 * Dialog to set the target score for a new game
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetScoreDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var targetScoreText by remember { mutableStateOf("101") }
    var isError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set the Winning Score") },
        text = {
            Column {
                OutlinedTextField(
                    value = targetScoreText,
                    onValueChange = { 
                        targetScoreText = it
                        isError = it.toIntOrNull() == null || it.toIntOrNull() ?: 0 <= 0
                    },
                    label = { Text("Target Score: 101") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError,
                    singleLine = true
                )
                
                if (isError) {
                    Text(
                        text = "Please enter a valid positive number",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val targetScore = targetScoreText.toIntOrNull() ?: 101
                    if (targetScore > 0) {
                        onConfirm(targetScore)
                    }
                },
                enabled = !isError
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 