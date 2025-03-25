package com.example.mobile_application_development_dice.model

/**
 * Represents a single die with its value and selected state
 */
data class Dice(
    val value: Int = 1,
    val isSelected: Boolean = false
) {
    companion object {
        /**
         * Generates a random dice value between 1 and 6
         */
        fun roll(): Int {
            return (1..6).random()
        }
    }
} 