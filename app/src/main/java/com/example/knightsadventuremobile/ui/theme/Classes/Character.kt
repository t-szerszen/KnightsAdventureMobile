package com.example.knightsadventuremobile.ui.theme.Classes

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

    data class isDefendingResult (
        val isDefending: Boolean,
        val message: String?
    )

    fun isOpponentDefending(opponent: Character): isDefendingResult
    {
        if ((opponent as? Monster)?.isDefending == true) {
            opponent.isDefending = false
            val message = "🛡️ ${opponent.name} broni się! Obrażenia zostają zredukowane o połowę."
            return isDefendingResult(true, message)
        }
        return isDefendingResult(false, null)
    }

    fun hasExtraAttack() = extraAttack != 0.0
}