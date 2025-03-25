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
        // Don't reroll if already at max rolls
        if (currentRoll >= 3) return false
        
        // Calculate current dice sum
        val currentSum = currentDice.sumOf { it.value }
        
        // Calculate how close we are to target
        val pointsToTarget = targetScore - computerScore
        
        // Calculate score difference with player
        val scoreDifference = playerScore - computerScore
        
        // Early game strategy (far from target)
        if (computerScore < targetScore * 0.5) {
            // If first roll, almost always reroll unless we got a very high roll
            if (currentRoll == 1) {
                return currentSum < 25 // Only keep exceptional first rolls
            }
            
            // For second roll, be more selective
            if (currentRoll == 2) {
                return currentSum < 20 // Reroll if below average
            }
        }
        
        // Mid game strategy
        else if (computerScore < targetScore * 0.8) {
            // If behind, be more aggressive
            if (scoreDifference > 0) {
                // More likely to reroll if behind
                val rerollThreshold = 15 + (scoreDifference / 5)
                return currentSum < rerollThreshold
            } else {
                // If ahead, be more conservative
                return currentSum < 18
            }
        }
        
        // End game strategy (close to target)
        else {
            // If we can win with current roll, don't reroll
            if (computerScore + currentSum >= targetScore) {
                return false
            }
            
            // If very close to target, be more careful
            if (pointsToTarget < 20) {
                // Only reroll if current sum is very low
                return currentSum < 12
            }
            
            // Otherwise, standard strategy
            return currentSum < 15
        }
        
        // Default fallback - 50% chance
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
        // Always keep high value dice (5-6)
        // Usually keep medium value dice (3-4)
        // Usually reroll low value dice (1-2)
        
        // Points needed to reach target
        val pointsNeeded = targetScore - computerScore
        
        return dice.map { die ->
            val value = die.value
            
            // Different strategies based on game state
            val keepProbability = when {
                // Always keep 6s
                value == 6 -> 1.0
                
                // Almost always keep 5s
                value == 5 -> 0.9
                
                // Usually keep 4s
                value == 4 -> 0.7
                
                // Sometimes keep 3s
                value == 3 -> 0.5
                
                // Rarely keep 2s
                value == 2 -> 0.2
                
                // Almost never keep 1s
                else -> 0.1
            }
            
            // Adjust based on current roll
            val rollAdjustment = when (currentRoll) {
                1 -> -0.2  // Be more aggressive on first roll
                2 -> 0.0   // Standard on second roll
                else -> 0.2 // Be more conservative on last roll
            }
            
            // Adjust based on how close to target
            val targetAdjustment = when {
                pointsNeeded < 10 -> 0.3  // Very close to target, be conservative
                pointsNeeded < 20 -> 0.1  // Close to target
                pointsNeeded > 50 -> -0.1 // Far from target, be aggressive
                else -> 0.0               // Standard distance
            }
            
            // Calculate final probability
            val finalProbability = (keepProbability + rollAdjustment + targetAdjustment).coerceIn(0.0, 1.0)
            
            // Decide whether to keep based on calculated probability
            val shouldKeep = Math.random() < finalProbability
            
            die.copy(isSelected = shouldKeep)
        }
    }
} 