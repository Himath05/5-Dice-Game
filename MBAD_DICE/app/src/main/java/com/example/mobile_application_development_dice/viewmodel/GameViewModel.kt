package com.example.mobile_application_development_dice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_application_development_dice.model.ComputerAI
import com.example.mobile_application_development_dice.model.Dice
import com.example.mobile_application_development_dice.model.GameLogic
import com.example.mobile_application_development_dice.model.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the dice game state and logic
 */
class GameViewModel : ViewModel() {
    
    private val gameLogic = GameLogic()
    private val computerAI = ComputerAI()
    
    // Game state as a StateFlow for reactive UI updates
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    /**
     * Starts a new game with the given target score
     */
    fun startNewGame(targetScore: Int = 101) {
        _gameState.update { 
            GameState(targetScore = targetScore) 
        }
    }
    
    /**
     * Rolls the dice for the human player
     */
    fun rollPlayerDice() {
        val currentState = _gameState.value
        
        // Don't allow rolling if game is over or at max rolls
        if (currentState.isGameOver || currentState.currentRollCount >= 3) {
            return
        }
        
        // Roll the dice
        val newDice = gameLogic.rollDice(currentState.playerDice)
        val newRollCount = currentState.currentRollCount + 1
        
        // Update state
        _gameState.update { state ->
            state.copy(
                playerDice = newDice,
                currentRollCount = newRollCount
            )
        }
        
        // Auto-score if this was the third roll
        if (newRollCount >= 3) {
            scorePlayerDice()
        }
    }
    
    /**
     * Toggles selection state of a player die
     */
    fun togglePlayerDiceSelection(index: Int) {
        val currentState = _gameState.value
        
        // Don't allow selection if game is over or no rolls yet
        if (currentState.isGameOver || currentState.currentRollCount == 0) {
            return
        }
        
        // Toggle selection
        val newDice = gameLogic.toggleDiceSelection(currentState.playerDice, index)
        
        // Update state
        _gameState.update { state ->
            state.copy(playerDice = newDice)
        }
    }
    
    /**
     * Scores the current dice and ends the player's turn
     */
    fun scorePlayerDice() {
        val currentState = _gameState.value
        
        // Don't allow scoring if game is over or no rolls yet
        if (currentState.isGameOver || currentState.currentRollCount == 0) {
            return
        }
        
        // Calculate score
        val diceScore = gameLogic.calculateScore(currentState.playerDice)
        val newPlayerScore = currentState.playerScore + diceScore
        
        // Update state with new score
        _gameState.update { state ->
            state.copy(
                playerScore = newPlayerScore,
                playerDice = gameLogic.clearSelections(state.playerDice)
            )
        }
        
        // Check if game is over or tie breaker needed
        checkGameStatus()
        
        // If game continues, start computer's turn
        if (!_gameState.value.isGameOver && !_gameState.value.isTieBreaker) {
            playComputerTurn()
        }
    }
    
    /**
     * Plays the computer's turn
     */
    private fun playComputerTurn() {
        viewModelScope.launch {
            // Set computer turn flag
            _gameState.update { it.copy(isComputerTurn = true) }
            
            // Initial roll
            rollComputerDice()
            delay(1000) // Delay for animation
            
            // Get current state after roll
            var currentState = _gameState.value
            
            // Decide whether to reroll (up to 2 more times)
            var currentRoll = 1
            while (currentRoll < 3) {
                // Check if computer wants to reroll
                val shouldReroll = computerAI.shouldReroll(
                    currentRoll,
                    currentState.computerScore,
                    currentState.playerScore,
                    currentState.targetScore,
                    currentState.computerDice
                )
                
                if (shouldReroll) {
                    // Select dice to keep
                    val diceWithSelection = computerAI.selectDiceToKeep(
                        currentState.computerDice,
                        currentRoll,
                        currentState.computerScore,
                        currentState.targetScore
                    )
                    
                    // Update selections
                    _gameState.update { it.copy(computerDice = diceWithSelection) }
                    delay(500) // Delay to show selections
                    
                    // Roll again
                    rollComputerDice()
                    delay(1000) // Delay for animation
                    
                    // Update current state
                    currentState = _gameState.value
                    currentRoll++
                } else {
                    // Computer decides not to reroll
                    break
                }
            }
            
            // Score computer's dice
            scoreComputerDice()
            
            // End computer's turn
            _gameState.update { it.copy(isComputerTurn = false) }
            
            // Reset for next player turn if game continues
            if (!_gameState.value.isGameOver && !_gameState.value.isTieBreaker) {
                _gameState.update { it.copy(currentRollCount = 0) }
            }
        }
    }
    
    /**
     * Rolls the dice for the computer
     */
    private fun rollComputerDice() {
        val currentState = _gameState.value
        
        // Roll the dice (only unselected ones)
        val newDice = gameLogic.rollDice(currentState.computerDice)
        
        // Update state
        _gameState.update { state ->
            state.copy(computerDice = newDice)
        }
    }
    
    /**
     * Scores the computer's dice
     */
    private fun scoreComputerDice() {
        val currentState = _gameState.value
        
        // Calculate score
        val diceScore = gameLogic.calculateScore(currentState.computerDice)
        val newComputerScore = currentState.computerScore + diceScore
        
        // Update state with new score
        _gameState.update { state ->
            state.copy(
                computerScore = newComputerScore,
                computerDice = gameLogic.clearSelections(state.computerDice)
            )
        }
        
        // Check if game is over or tie breaker needed
        checkGameStatus()
    }
    
    /**
     * Checks if the game is over or if a tie breaker is needed
     */
    private fun checkGameStatus() {
        val currentState = _gameState.value
        
        // Check if tie breaker is needed
        if (gameLogic.isTieBreakerNeeded(
                currentState.playerScore,
                currentState.computerScore,
                currentState.targetScore
            )) {
            // Start tie breaker
            _gameState.update { it.copy(isTieBreaker = true) }
            startTieBreaker()
            return
        }
        
        // Check if game is over
        val (isGameOver, isPlayerWinner) = gameLogic.checkGameOver(
            currentState.playerScore,
            currentState.computerScore,
            currentState.targetScore
        )
        
        if (isGameOver) {
            // Update win statistics
            val playerWins = if (isPlayerWinner == true) currentState.playerWins + 1 else currentState.playerWins
            val computerWins = if (isPlayerWinner == false) currentState.computerWins + 1 else currentState.computerWins
            
            // Update game state
            _gameState.update { state ->
                state.copy(
                    isGameOver = true,
                    isPlayerWinner = isPlayerWinner ?: false,
                    playerWins = playerWins,
                    computerWins = computerWins
                )
            }
        }
    }
    
    /**
     * Starts the tie breaker process
     */
    private fun startTieBreaker() {
        viewModelScope.launch {
            // Reset dice for tie breaker
            _gameState.update { state ->
                state.copy(
                    playerDice = List(5) { Dice() },
                    computerDice = List(5) { Dice() },
                    currentRollCount = 0
                )
            }
            
            delay(1000) // Delay for UI update
            
            // Roll dice for both players
            val playerDice = gameLogic.rollDice(_gameState.value.playerDice)
            val computerDice = gameLogic.rollDice(_gameState.value.computerDice)
            
            // Update state with new dice
            _gameState.update { state ->
                state.copy(
                    playerDice = playerDice,
                    computerDice = computerDice
                )
            }
            
            delay(1000) // Delay for animation
            
            // Resolve tie breaker
            val winner = gameLogic.resolveTieBreaker(playerDice, computerDice)
            
            if (winner != null) {
                // We have a winner
                val playerWins = if (winner) _gameState.value.playerWins + 1 else _gameState.value.playerWins
                val computerWins = if (!winner) _gameState.value.computerWins + 1 else _gameState.value.computerWins
                
                // Update game state
                _gameState.update { state ->
                    state.copy(
                        isGameOver = true,
                        isPlayerWinner = winner,
                        isTieBreaker = false,
                        playerWins = playerWins,
                        computerWins = computerWins
                    )
                }
            } else {
                // Still tied, repeat tie breaker
                delay(1000)
                startTieBreaker()
            }
        }
    }
    
    /**
     * Updates the target score
     */
    fun updateTargetScore(newTargetScore: Int) {
        if (newTargetScore > 0) {
            _gameState.update { it.copy(targetScore = newTargetScore) }
        }
    }
} 