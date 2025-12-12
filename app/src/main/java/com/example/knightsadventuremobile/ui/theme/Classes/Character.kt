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
    abstract fun attack (opponent: Character)
    abstract fun defense ()
    abstract fun drinkElixir ()

    fun isOpponentDefending(opponent: Character): Boolean
    {
        if ((opponent as? Monster)?.isDefending == true) {
            opponent.isDefending = false
            println("🛡️ ${opponent.name} broni się! Obrażenia zostają zredukowane o połowę.")
            return true
        }
        return false
    }

    fun hasExtraAttack() = extraAttack != 0.0
}