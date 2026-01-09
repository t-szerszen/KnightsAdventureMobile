package com.example.knightsadventuremobile.ui.theme.Classes

import kotlin.random.Random

class Hero(
    name: String,
    health: Int,
    attackPower: Int,
    equipment: MutableList<String>,
    isDefending: Boolean,
    extraAttack: Double,
    elixirUsed: Boolean
) : Character(name, health, attackPower, equipment, isDefending, extraAttack, elixirUsed)
{
    var gold: Int = 0

    override fun attack(opponent: Character): String {
        var damage = Random.Default.nextInt(attackPower / 2, attackPower + 5)
        var message = ""

        val result = isOpponentDefending(opponent)
        if (result.value) {
            damage = (damage * 0.5).toInt()
            message += (result.extra ?: "") + "\n"
        }

        if (hasExtraAttack()) {
            damage = (damage * extraAttack).toInt()
            extraAttack = 0.0
        }

        if (hasSword()) damage += 8 // Zwiększony bonus miecza

        isDefending = false
        opponent.health = (opponent.health - damage).coerceAtLeast(0)

        message += "⚔️ $name atakuje i zadaje $damage obrażeń!"
        return message
    }

    private fun hasSword(): Boolean = equipment.contains("Miecz")

    override fun defense(): String {
        isDefending = true
        extraAttack = 2.0 // Mocniejszy kontratak (z 1.5 na 2.0)
        return "🛡️ $name broni się! Następny atak będzie 2x silniejszy!"
    }

    override fun drinkElixir(): String {
        extraAttack = 1.3
        health += 25 // Lepsze leczenie (z 10 na 25)
        elixirUsed = true
        return "🧪 $name wypija eliksir! +25 HP i +30% siły."
    }
}