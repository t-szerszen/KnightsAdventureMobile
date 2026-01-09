package com.example.knightsadventuremobile.ui.theme.Classes

import com.example.knightsadventuremobile.ui.theme.Models.GameResponse
import kotlin.random.Random

class Monster(
    name: String,
    health: Int,
    attackPower: Int,
    equipment: MutableList<String>,
    isDefending: Boolean,
    extraAttack: Double,
    elixirUsed: Boolean
) : Character(name, health, attackPower, equipment, isDefending, extraAttack, elixirUsed)
{
    override fun attack(opponent: Character): String {
        var damage = Random.Default.nextInt(attackPower / 2, attackPower)
        val bodyPart = Random.Default.nextInt(1, 4)
        var message = ""

        val defendingResult = isOpponentDefending(opponent)
        if (defendingResult.value) {
            damage = (damage * 0.5).toInt()
            message += (defendingResult.extra ?: "") + "\n"
        }

        val hitResult = bodyPartHitted(bodyPart, opponent, damage)
        damage = hitResult.value
        message += hitResult.extra + "\n"

        if (hasExtraAttack()) {
            damage = (damage * extraAttack).toInt()
            extraAttack = 0.0
        }

        isDefending = false
        opponent.health = (opponent.health - damage).coerceAtLeast(0)

        message += "⚔️ $name zadaje $damage obrażeń ${opponent.name}!"
        return message
    }

    private fun bodyPartHitted(bodyPart: Int, opponent: Character, initialDamage: Int): GameResponse<Int, String> {
        var damage = initialDamage
        val armorFactor = Random.Default.nextDouble(0.5, 1.0)
        var message = ""

        when (bodyPart) {
            1 -> {
                message = "💥 $name uderza w głowę!"
                if (opponent.equipment.contains("Hełm")) {
                    damage = (damage * armorFactor).toInt()
                    message += "\n🪖 Hełm redukuje obrażenia!"
                    opponent.equipment.remove("Hełm")
                }
            }
            2 -> {
                message = "💥 $name uderza w tułów!"
                if (opponent.equipment.contains("Kolczuga")) {
                    damage = (damage * armorFactor).toInt()
                    message += "\n🛡️ Kolczuga amortyzuje cios!"
                    opponent.equipment.remove("Kolczuga")
                }
            }
            3 -> {
                message = "💥 $name uderza w nogi!"
                if (opponent.equipment.contains("Nagolenniki")) {
                    damage = (damage * armorFactor).toInt()
                    message += "\n🩳 Nagolenniki przyjmują uderzenie!"
                    opponent.equipment.remove("Nagolenniki")
                }
            }
        }
        return GameResponse(damage, message)
    }

    override fun defense(): String {
        isDefending = true
        extraAttack = 1.5
        return "🛡️ $name przyjmuje pozycję obronną i szykuje kontratak!"
    }

    override fun drinkElixir(): String {
        extraAttack = 1.2
        health += 10
        elixirUsed = true
        return "🧪 $name wypija eliksir! Siła wzrasta, HP rośnie o 10 pkt."
    }
}