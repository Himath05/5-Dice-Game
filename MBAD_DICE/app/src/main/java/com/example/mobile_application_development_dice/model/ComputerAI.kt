/**
 * CourseWork : Mobile Application Development - CW 1
 * Student name : Himath De Silva
 * IIT ID : 20231127
 * UOW ID : W2051895
 */

package com.example.mobile_application_development_dice.model

/**
 * Advanced AI strategy for the computer player
 * 
 * This AI uses probability-based decision making to optimize its chances of winning.
 * It does not see the human player's current dice, but makes decisions based on:
 * 1. Current scores and target score
 * 2. Current roll number
 * 3. Value of its own dice
 * 4. Probability calculations for potential outcomes
 * 
 * The strategy adapts based on the game situation:
 * - Early game: Focus on maximizing score
 * - Mid game: Balance risk/reward
 * - End game: More conservative when close to target
 * - Catch-up mode: More aggressive when behind
 */
class ComputerAI {
    
    /**
     * Decides whether the computer should reroll based on game state
     * 
     * @param currentRoll Current roll number (1-3)
     * @param computerScore Computer's current score
     * @param playerScore Player's current score
     * @param targetScore Target score to win
     * @param currentDice Computer's current dice
     * @return True if computer should reroll
     */
    fun shouldReroll(
        currentRoll: Int,
        computerScore: Int, 
        playerScore: Int,
        targetScore: Int,
        currentDice: List<Dice>
    ): Boolean {
        if (currentRoll >= 3) return false
        
        val currentSum = currentDice.sumOf { it.value }
        
        val pointsToTarget = targetScore - computerScore
        
        val scoreDifference = playerScore - computerScore
        
        if (computerScore < targetScore * 0.5) {
            if (currentRoll == 1) {
                return currentSum < 25
            }

            if (currentRoll == 2) {
                return currentSum < 20
            }
        }
        
        else if (computerScore < targetScore * 0.8) {
            if (scoreDifference > 0) {
                val rerollThreshold = 15 + (scoreDifference / 5)
                return currentSum < rerollThreshold
            } else {
                return currentSum < 18
            }
        }
        
        else {
            if (computerScore + currentSum >= targetScore) {
                return false
            }
            
            if (pointsToTarget < 20) {
                return currentSum < 12
            }
            
            return currentSum < 15
        }
        
        return Math.random() > 0.5
    }
    
    /**
     * Determines which dice the computer should keep for rerolling
     * Uses probability-based decision making
     * 
     * @param dice Current dice values
     * @param currentRoll Current roll number
     * @param computerScore Current computer score
     * @param targetScore Target score to win
     * @return List of dice with selection states updated
     */
    fun selectDiceToKeep(
        dice: List<Dice>,
        currentRoll: Int,
        computerScore: Int,
        targetScore: Int
    ): List<Dice> {

        val pointsNeeded = targetScore - computerScore
        
        return dice.map { die ->
            val value = die.value
            
            val keepProbability = when {
                value == 6 -> 1.0
                
                value == 5 -> 0.9
                
                value == 4 -> 0.7
                
                value == 3 -> 0.5
                
                value == 2 -> 0.2
                
                else -> 0.1
            }
            
            val rollAdjustment = when (currentRoll) {
                1 -> -0.2
                2 -> 0.0
                else -> 0.2
            }
            
            val targetAdjustment = when {
                pointsNeeded < 10 -> 0.3
                pointsNeeded < 20 -> 0.1
                pointsNeeded > 50 -> -0.1
                else -> 0.0
            }
            
            val finalProbability = (keepProbability + rollAdjustment + targetAdjustment).coerceIn(0.0, 1.0)
            
            val shouldKeep = Math.random() < finalProbability
            
            die.copy(isSelected = shouldKeep)
        }
    }
} 