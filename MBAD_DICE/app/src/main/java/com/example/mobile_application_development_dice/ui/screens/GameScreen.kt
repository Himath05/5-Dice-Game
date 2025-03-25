package com.example.mobile_application_development_dice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mobile_application_development_dice.R
import com.example.mobile_application_development_dice.model.Dice
import com.example.mobile_application_development_dice.ui.resources.DiceResourceProvider
import com.example.mobile_application_development_dice.viewmodel.GameViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import com.example.mobile_application_development_dice.ui.theme.DiceRed
import com.example.mobile_application_development_dice.ui.theme.DiceBlue
import com.example.mobile_application_development_dice.ui.theme.DiceSelected
import androidx.compose.foundation.shape.CircleShape
import com.example.mobile_application_development_dice.ui.theme.DiceYellow
import com.example.mobile_application_development_dice.ui.theme.DiceGreen
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding

/**
 * Game screen showing the dice game interface
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onBackClick: () -> Unit
) {
    val gameState by gameViewModel.gameState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable(onClick = onBackClick)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(DiceRed.copy(alpha = 0.15f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BACK",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = DiceRed
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Wrap content in a Box with background image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.bg2),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Game content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Space at the top for scores
                Spacer(modifier = Modifier.height(16.dp))

                // Game scores in bubbles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Win counter bubble
                    ScoreBubble(
                        title = "WINS",
                        value = "H: ${gameState.playerWins} | C: ${gameState.computerWins}",
                        color = DiceBlue
                    )
                    
                    // Target score bubble
                    ScoreBubble(
                        title = "TARGET",
                        value = "${gameState.targetScore}",
                        color = DiceGreen
                    )
                }

                // Player scores in bubbles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Human score
                    ScoreBubble(
                        title = "HUMAN",
                        value = "${gameState.playerScore}",
                        color = DiceRed
                    )
                    
                    // Roll counter in the middle
                    Card(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .shadow(elevation = 4.dp, shape = CircleShape),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier
                                .background(DiceYellow.copy(alpha = 0.2f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${gameState.currentRollCount}/3",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Computer score
                    ScoreBubble(
                        title = "COMPUTER",
                        value = "${gameState.computerScore}",
                        color = DiceBlue
                    )
                }

                // More space before dice sections
                Spacer(modifier = Modifier.height(32.dp))
                
                // Create a Box that pushes content down and takes up space
                Box(modifier = Modifier.weight(0.1f))
                
                // Player section with a card background
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "YOUR DICE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DiceRed
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Player dice row
                        DiceRow(
                            dice = gameState.playerDice,
                            onDiceClick = { index ->
                                if (!gameState.isGameOver && gameState.currentRollCount > 0) {
                                    gameViewModel.togglePlayerDiceSelection(index)
                                }
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Game controls
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Throw button
                    Button(
                        onClick = { gameViewModel.rollPlayerDice() },
                        enabled = !gameState.isGameOver && gameState.currentRollCount < 3 && !gameState.isTieBreaker,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DiceBlue,
                            disabledContainerColor = DiceBlue.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(8.dp)

                    ) {
                        Text(
                            text = "THROW",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                    
                    // Score button
                    Button(
                        onClick = { gameViewModel.scorePlayerDice() },
                        enabled = !gameState.isGameOver && gameState.currentRollCount > 0 && !gameState.isTieBreaker,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DiceRed,
                            disabledContainerColor = DiceRed.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "SCORE",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Computer dice section with card background
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.7f))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "COMPUTER'S DICE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DiceBlue
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Computer dice row
                        DiceRow(
                            dice = gameState.computerDice,
                            onDiceClick = { /* No action for computer dice */ }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Game over message
                if (gameState.isGameOver) {
                    GameOverMessage(isPlayerWinner = gameState.isPlayerWinner)
                }
                
                // Tie breaker message
                if (gameState.isTieBreaker) {
                    Text(
                        text = "Tie Breaker! Rolling again...",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Displays a row of dice
 */
@Composable
fun DiceRow(
    dice: List<Dice>,
    onDiceClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dice.forEachIndexed { index, die ->
            DiceItem(
                diceValue = die.value,
                isSelected = die.isSelected,
                onClick = { onDiceClick(index) }
            )
        }
    }
}

/**
 * Displays a single die
 */
@Composable
fun DiceItem(
    diceValue: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        DiceSelected // Using our custom green selection color
    } else {
        Color.Transparent
    }
    
    val elevation = if (isSelected) 8.dp else 3.dp
    
    Box(
        modifier = Modifier
            .size(65.dp)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(12.dp),
                spotColor = if (isSelected) DiceSelected else Color.Gray
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = DiceResourceProvider.getDiceDrawable(diceValue)),
            contentDescription = "Dice $diceValue",
            modifier = Modifier.size(50.dp)
        )
    }
}

/**
 * Displays the game over message
 */
@Composable
fun GameOverMessage(isPlayerWinner: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val messageText = if (isPlayerWinner) {
                "You Win!"
            } else {
                "You Lose!"
            }
            
            val messageColor = if (isPlayerWinner) {
                Color(0xFF4CAF50) // Green color for win
            } else {
                Color(0xFFF44336) // Red color for loss
            }
            
            Text(
                text = messageText,
                style = MaterialTheme.typography.headlineMedium,
                color = messageColor,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Press back to return to the main menu",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ScoreBubble(
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color.copy(alpha = 0.15f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 