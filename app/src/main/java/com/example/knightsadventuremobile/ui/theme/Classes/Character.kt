package com.example.knightsadventuremobile.ui.theme.Classes

import com.example.knightsadventuremobile.ui.theme.Models.GameResponse

// Klasa modelująca ogólny obiekt postaci w grze.
abstract class Character (
    val name: String,
    var health: Int,
    val attackPower: Int,
    var equipment: MutableList<String>,
    var isDefending: Boolean = false,
    var extraAttack: Double = 0.0,
    var elixirUsed: Boolean = false
)
{
    abstract fun attack (opponent: Character): String
    abstract fun defense (): String
    abstract fun drinkElixir (): String

    fun isOpponentDefending(opponent: Character): GameResponse<Boolean, String?>
    {
        if ((opponent as? Monster)?.isDefending == true) {
            opponent.isDefending = false
            val message = "🛡️ ${opponent.name} broni się! Obrażenia zostają zredukowane o połowę."
            return GameResponse(true, message)
        }
        return GameResponse(false, null)
    }

    fun hasExtraAttack() = extraAttack != 0.0
}