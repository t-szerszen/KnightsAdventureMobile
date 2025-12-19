package com.example.knightsadventuremobile.ui.theme.Classes

import kotlin.random.Random

// Klasa modelująca obiekt potwora z areny.
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
        val bodyPart = Random.Default.nextInt(1, 4) // 1 - głowa, 2 - tułów, 3 - nogi

        // Sprawdzamy czy przeciwnik się broni
        if (isOpponentDefending(opponent)) {
            damage = (damage * 0.5).toInt()
        }

        // Redukujemy obrażenia jeśli bohater ma jakiś pancerz
        val result = bodyPartHitted(bodyPart, opponent, damage)
        damage = result.damage

        // Dodatkowy bonus do ataku z poprzedniej tury defensywnej lub eliksiru
        if (hasExtraAttack()) {
            damage = (damage * extraAttack).toInt()
            extraAttack = 0.0
        }

        isDefending = false
        opponent.health -= damage
        if (opponent.health < 0) opponent.health = 0

        val message = """
            ⚔️ ${name} zadaje ${damage} obrażeń ${opponent.name}!
            ❤️ HP ${opponent.name}: ${opponent.health}
        """

        return message
    }

    data class HitResult(
        val damage: Int,
        val message: String
    )

    private fun bodyPartHitted(bodyPart: Int, opponent: Character, initialDamage: Int): HitResult {
        var damage = initialDamage
        val armorFactor = Random.Default.nextDouble(0.5, 1.0)
        var message = ""

        when (bodyPart) {
            1 -> {
                message = "\uD83D\uDCA5 ${name} uderza w głowę!"
                if (opponent.equipment.contains("Hełm")) {
                    val reduced = (damage * armorFactor).toInt()
                    message += "🪖 Hełm ${opponent.name} redukuje (${(armorFactor * 100).toInt()}% obrażeń)"
                    opponent.equipment.remove("Hełm")
                    damage = reduced
                }
            }
            2 -> {
                message = "\uD83D\uDCA5 ${name} uderza w tułów!"
                if (opponent.equipment.contains("Kolczuga")) {
                    val reduced = (damage * armorFactor).toInt()
                    message += "🛡️ Kolczuga ${opponent.name} amortyzuje ${(armorFactor * 100).toInt()}% obrażeń"
                    opponent.equipment.remove("Kolczuga")
                    damage = reduced
                }
            }
            3 -> {
                message = "\uD83D\uDCA5 ${name} uderza w nogi!"
                if (opponent.equipment.contains("Nagolenniki")) {
                    val reduced = (damage * armorFactor).toInt()
                    message += "🩳 Nagolenniki ${opponent.name} redukują ${(armorFactor * 100).toInt()} obrażeń"
                    opponent.equipment.remove("Nagolenniki")
                    damage = reduced
                }
            }
        }
        return HitResult(damage, message)
    }

    override fun defense(): String
    {
        val message = """
            🛡️ ${name} przyjmuje pozycję obronną!
            Zyskuje zwiększoną ochronę przed obrażeniami 
            i przygotowuje silniejszy kontratak w następnej turze.
        """.trimIndent()
        isDefending = true
        extraAttack = 1.5

        return message
    }

    override fun drinkElixir(): String
    {
        var message = """
            🧪 ${name} wypija magiczny eliksir!
            ✨ Siła ataku chwilowo wzrasta o 20%.
            ❤️ ${name} odzyskuje 10 punktów zdrowia.
        """.trimIndent()
        extraAttack = 1.2
        health += 10
        elixirUsed = true
        message += "Obecne HP: ${health}\n"

        return message
    }
}