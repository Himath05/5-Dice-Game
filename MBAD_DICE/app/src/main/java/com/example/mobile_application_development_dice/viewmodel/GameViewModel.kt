/**
 * CourseWork : Mobile Application Development - CW 1
 * Student name : Himath De Silva
 * IIT ID : 20231127
 * UOW ID : W2051895
 */

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
        
        if (currentState.isGameOver || currentState.currentRollCount >= 3) {
            return
        }
        
        val newDice = gameLogic.rollDice(currentState.playerDice)
        val newRollCount = currentState.currentRollCount + 1
        
        _gameState.update { state ->
            state.copy(
                playerDice = newDice,
                currentRollCount = newRollCount
            )
        }
        
        if (newRollCount >= 3) {
            scorePlayerDice()
        }
    }
    
    /**
     * Toggles selection state of a player die
     */
    fun togglePlayerDiceSelection(index: Int) {
        val currentState = _gameState.value
        
        if (currentState.isGameOver || currentState.currentRollCount == 0) {
            return
        }
        
        val newDice = gameLogic.toggleDiceSelection(currentState.playerDice, index)
        
        _gameState.update { state ->
            state.copy(playerDice = newDice)
        }
    }
    
    /**
     * Scores the current dice and ends the player's turn
     */
    fun scorePlayerDice() {
        val currentState = _gameState.value
        
        if (currentState.isGameOver || currentState.currentRollCount == 0) {
            return
        }
        
        val diceScore = gameLogic.calculateScore(currentState.playerDice)
        val newPlayerScore = currentState.playerScore + diceScore
        
        _gameState.update { state ->
            state.copy(
                playerScore = newPlayerScore,
                playerDice = gameLogic.clearSelections(state.playerDice)
            )
        }
        
        checkGameStatus()
        
        if (!_gameState.value.isGameOver && !_gameState.value.isTieBreaker) {
            playComputerTurn()
        }
    }
    
    /**
     * Plays the computer's turn
     */
    private fun playComputerTurn() {
        viewModelScope.launch {
            _gameState.update { it.copy(isComputerTurn = true) }
            
            rollComputerDice()
            delay(1000)
            
            var currentState = _gameState.value
            
            var currentRoll = 1
            while (currentRoll < 3) {
                val shouldReroll = computerAI.shouldReroll(
                    currentRoll,
                    currentState.computerScore,
                    currentState.playerScore,
                    currentState.targetScore,
                    currentState.computerDice
                )
                
                if (shouldReroll) {
                    val diceWithSelection = computerAI.selectDiceToKeep(
                        currentState.computerDice,
                        currentRoll,
                        currentState.computerScore,
                        currentState.targetScore
                    )
                    
                    _gameState.update { it.copy(computerDice = diceWithSelection) }
                    delay(500)
                    
                    rollComputerDice()
                    delay(1000)
                    
                    currentState = _gameState.value
                    currentRoll++
                } else {
                    break
                }
            }
            
            scoreComputerDice()
            
            _gameState.update { it.copy(isComputerTurn = false) }
            
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
        
        val newDice = gameLogic.rollDice(currentState.computerDice)
        
        _gameState.update { state ->
            state.copy(computerDice = newDice)
        }
    }
    
    /**
     * Scores the computer's dice
     */
    private fun scoreComputerDice() {
        val currentState = _gameState.value
        
        val diceScore = gameLogic.calculateScore(currentState.computerDice)
        val newComputerScore = currentState.computerScore + diceScore
        
        _gameState.update { state ->
            state.copy(
                computerScore = newComputerScore,
                computerDice = gameLogic.clearSelections(state.computerDice)
            )
        }
        
        checkGameStatus()
    }
    
    /**
     * Checks if the game is over or if a tie breaker is needed
     */
    private fun checkGameStatus() {
        val currentState = _gameState.value
        
        if (gameLogic.isTieBreakerNeeded(
                currentState.playerScore,
                currentState.computerScore,
                currentState.targetScore
            )) {
            _gameState.update { it.copy(isTieBreaker = true) }
            startTieBreaker()
            return
        }
        
        val (isGameOver, isPlayerWinner) = gameLogic.checkGameOver(
            currentState.playerScore,
            currentState.computerScore,
            currentState.targetScore
        )
        
        if (isGameOver) {
            val playerWins = if (isPlayerWinner == true) currentState.playerWins + 1 else currentState.playerWins
            val computerWins = if (isPlayerWinner == false) currentState.computerWins + 1 else currentState.computerWins
            
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
            _gameState.update { state ->
                state.copy(
                    playerDice = List(5) { Dice() },
                    computerDice = List(5) { Dice() },
                    currentRollCount = 0
                )
            }
            
            delay(1000)
            
            val playerDice = gameLogic.rollDice(_gameState.value.playerDice)
            val computerDice = gameLogic.rollDice(_gameState.value.computerDice)
            
            _gameState.update { state ->
                state.copy(
                    playerDice = playerDice,
                    computerDice = computerDice
                )
            }
            
            delay(1000)
            
            val winner = gameLogic.resolveTieBreaker(playerDice, computerDice)
            
            if (winner != null) {
                val playerWins = if (winner) _gameState.value.playerWins + 1 else _gameState.value.playerWins
                val computerWins = if (!winner) _gameState.value.computerWins + 1 else _gameState.value.computerWins
                
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