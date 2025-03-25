/**
 * CourseWork : Mobile Application Development - CW 1
 * Student name : Himath De Silva
 * IIT ID : 20231127
 * UOW ID : W2051895
 */

package com.example.mobile_application_development_dice.model

/**
 * Represents the current state of the dice game
 */
data class GameState(
    val playerDice: List<Dice> = List(5) { Dice() },
    
    val computerDice: List<Dice> = List(5) { Dice() },
    
    val playerScore: Int = 0,
    val computerScore: Int = 0,
    
    // Roll count (max 3 per turn)
    val currentRollCount: Int = 0,
    
    val isGameOver: Boolean = false,
    val isPlayerWinner: Boolean = false,
    
    val playerWins: Int = 0,
    val computerWins: Int = 0,
    
    val targetScore: Int = 101,
    
    val isTieBreaker: Boolean = false,
    val isComputerTurn: Boolean = false
) 