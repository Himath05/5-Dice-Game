package com.example.mobile_application_development_dice.model

/**
 * Represents the current state of the dice game
 */
data class GameState(
    // Player dice
    val playerDice: List<Dice> = List(5) { Dice() },
    
    // Computer dice
    val computerDice: List<Dice> = List(5) { Dice() },
    
    // Current scores
    val playerScore: Int = 0,
    val computerScore: Int = 0,
    
    // Roll count (max 3 per turn)
    val currentRollCount: Int = 0,
    
    // Game status
    val isGameOver: Boolean = false,
    val isPlayerWinner: Boolean = false,
    
    // Win statistics
    val playerWins: Int = 0,
    val computerWins: Int = 0,
    
    // Target score (default 101)
    val targetScore: Int = 101,
    
    // Game phase
    val isTieBreaker: Boolean = false,
    val isComputerTurn: Boolean = false
) 