package com.example.knightsadventuremobile.ui.theme.Classes

import kotlin.random.Random

// Klasa modelująca obiekt bohatera.
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

    override fun attack(opponent: Character)
    {
        var damage = Random.Default.nextInt(attackPower / 2, attackPower)

        // Sprawdzamy czy przeciwnik się broni
        if (isOpponentDefending(opponent)) {
            damage = (damage * 0.5).toInt()
        }

        // Dodatkowy bonus do ataku z poprzedniej tury defensywnej lub eliksiru
        if (hasExtraAttack()) {
            damage = (damage * extraAttack).toInt()
            extraAttack = 0.0
        }

        // Jeśli boahter ma miecz zadaje więcej obrażeń
        if (hasSword()) damage += 5

        isDefending = false
        opponent.health -= damage
        if (opponent.health < 0) opponent.health = 0

        println("\n⚔️ ${name} atakuje ${opponent.name} i zadaje ${damage} obrażeń!")
        println("❤️ HP przeciwnika: ${opponent.health}\n")
    }

    private fun hasSword(): Boolean
    {
        if (equipment.contains("Miecz")) return true
        return false
    }

    override fun defense()
    {
        println(
            """
            🛡️ ${name} przyjmuje pozycję obronną!
            Zyskuje zwiększoną ochronę przed obrażeniami 
            i przygotowuje silniejszy kontratak w następnej turze.
            """.trimIndent()
        )
        isDefending = true
        extraAttack = 1.5
    }

    override fun drinkElixir()
    {
        println(
            """
            🧪 ${name} wypija magiczny eliksir!
            ✨ Siła ataku chwilowo wzrasta o 20%.
            ❤️ Odzyskujesz 10 punktów zdrowia.
            """.trimIndent()
        )
        extraAttack = 1.2
        health += 10
        elixirUsed = true
        println("Obecne HP: ${health}\n")
    }
}