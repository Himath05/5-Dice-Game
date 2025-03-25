package com.example.mobile_application_development_dice.ui.resources

import com.example.mobile_application_development_dice.R

/**
 * Provides resource IDs for dice images
 */
object DiceResourceProvider {
    /**
     * Maps a dice value to its corresponding drawable resource ID
     */
    fun getDiceDrawable(value: Int): Int {
        return when (value) {
            1 -> R.drawable.bdice_1
            2 -> R.drawable.bdice_2
            3 -> R.drawable.bdice_3
            4 -> R.drawable.bdice_4
            5 -> R.drawable.bdice_5
            6 -> R.drawable.bdice_6
            else -> R.drawable.bdice_1
        }
    }
    
    /**
     * Gets the resource ID for the selected dice border
     */
    fun getSelectedDiceDrawable(): Int {
        return R.drawable.dice_selected
    }
} 