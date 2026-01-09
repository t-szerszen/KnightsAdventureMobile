package com.example.knightsadventuremobile.ui.theme.Controllers

import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Classes.Monster
import kotlin.random.Random

class FightMode {
    
    fun executeHeroAttack(hero: Hero, monster: Monster): String {
        return hero.attack(monster)
    }

    fun executeHeroDefense(hero: Hero): String {
        return hero.defense()
    }

    fun executeHeroDrinkElixir(hero: Hero): String {
        return if (!hero.elixirUsed) {
            hero.drinkElixir()
        } else {
            "🚫 Użyłeś już elixiru nie masz go więcej!"
        }
    }

    fun executeMonsterMove(monster: Monster, hero: Hero): String {
        if (monster.health <= 0) return ""
        
        val move = Random.Default.nextInt(1, 4)
        return when (move) {
            1 -> monster.attack(hero)
            2 -> monster.defense()
            3 -> {
                if (!monster.elixirUsed) {
                    monster.drinkElixir()
                } else {
                    // Jeśli eliksir użyty, spróbuj zaatakować
                    monster.attack(hero)
                }
            }
            else -> ""
        }
    }

    fun getBattleSummary(isVictory: Boolean, round: Int, hero: Hero): String {
        var goldChange: Int
        var regeneratedHealth: Int
        val resultText: String

        if (isVictory) {
            resultText = "🏆 Wygrałeś walkę!"
            goldChange = 25 * round
            hero.gold += goldChange
            regeneratedHealth = 25 * round
            hero.health += regeneratedHealth
        } else {
            resultText = "💀 Przegrałeś walkę..."
            goldChange = 10 * round
            hero.gold = (hero.gold - goldChange).coerceAtLeast(0)
            regeneratedHealth = 15 * round
            hero.health += regeneratedHealth
        }

        hero.elixirUsed = false // Reset na koniec walki

        return """
            $resultText
            ${if (isVictory) "🪙 Zdobyłeś" else "🪙 Straciłeś"} $goldChange sztuk złota.
            🩹 Odzyskałeś $regeneratedHealth punktów zdrowia.
            👝 Posiadasz w sakiewce ${hero.gold} sztuk złota.
            💖 Masz ${hero.health} punktów zdrowia.
        """.trimIndent()
    }
}