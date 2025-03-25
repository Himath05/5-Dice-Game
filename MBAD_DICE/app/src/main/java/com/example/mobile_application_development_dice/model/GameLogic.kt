/**
 * CourseWork : Mobile Application Development - CW 1
 * Student name : Himath De Silva
 * IIT ID : 20231127
 * UOW ID : W2051895
 */

package com.example.mobile_application_development_dice.model

/**
 * Handles the game logic for the dice game
 */
class GameLogic {
    /**
     * Rolls dice for a player based on selection
     * @param dice Current dice list
     * @return New dice list with updated values for unselected dice
     */
    fun rollDice(dice: List<Dice>): List<Dice> {
        return dice.map { die ->
            if (!die.isSelected) {
                die.copy(value = Dice.roll())
            } else {
                die
            }
        }
    }

    /**
     * Calculates the sum of dice values
     * @param dice List of dice
     * @return Sum of all dice values
     */
    fun calculateScore(dice: List<Dice>): Int {
        return dice.sumOf { it.value }
    }

    /**
     * Toggles the selection state of a die
     * @param dice Current dice list
     * @param index Index of the die to toggle
     * @return Updated dice list with toggled selection
     */
    fun toggleDiceSelection(dice: List<Dice>, index: Int): List<Dice> {
        return dice.mapIndexed { i, die ->
            if (i == index) {
                die.copy(isSelected = !die.isSelected)
            } else {
                die
            }
        }
    }

    /**
     * Resets all dice selections
     * @param dice Current dice list
     * @return Updated dice list with all selections cleared
     */
    fun clearSelections(dice: List<Dice>): List<Dice> {
        return dice.map { it.copy(isSelected = false) }
    }

    /**
     * Determines if the game is over based on scores
     * @param playerScore Current player score
     * @param computerScore Current computer score
     * @param targetScore Target score to win
     * @return Pair of (isGameOver, isPlayerWinner)
     */
    fun checkGameOver(playerScore: Int, computerScore: Int, targetScore: Int): Pair<Boolean, Boolean?> {
        val playerReachedTarget = playerScore >= targetScore
        val computerReachedTarget = computerScore >= targetScore

        return when {
            // Both reached target - tie breaker needed
            playerReachedTarget && computerReachedTarget -> Pair(false, null)
            
            // Player reached target
            playerReachedTarget -> Pair(true, true)
            
            // Computer reached target
            computerReachedTarget -> Pair(true, false)
            
            // Game continues
            else -> Pair(false, null)
        }
    }

    /**
     * Determines if a tie breaker is needed
     * @param playerScore Current player score
     * @param computerScore Current computer score
     * @param targetScore Target score to win
     * @return True if tie breaker is needed
     */
    fun isTieBreakerNeeded(playerScore: Int, computerScore: Int, targetScore: Int): Boolean {
        return playerScore >= targetScore && computerScore >= targetScore
    }

    /**
     * Resolves a tie breaker based on current dice values
     * @param playerDice Player's dice
     * @param computerDice Computer's dice
     * @return Winner (true for player, false for computer, null for another tie)
     */
    fun resolveTieBreaker(playerDice: List<Dice>, computerDice: List<Dice>): Boolean? {
        val playerSum = calculateScore(playerDice)
        val computerSum = calculateScore(computerDice)

        return when {
            playerSum > computerSum -> true
            computerSum > playerSum -> false
            else -> null // Still tied
        }
    }
} 