package com.example.knightsadventuremobile.ui.theme.Controllers

import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Classes.Monster
import kotlin.random.Random

class FightMode {
    // Metoda rozpoczynająca walkę na arenie potworów.
    fun startBattle(round: Int, hero: Hero, monster: Monster) {
        var i:Int = 1
        var message = """ 
            ${"=".repeat(50)}
            "🏹  Rozpoczyna się bitwa $round!"
            ${"=" .repeat(50)}
        """

        while (hero.health > 0 && monster.health > 0) {
            println("\n" + "-".repeat(40))
            Thread.sleep(700) // lekkie opóźnienie między turami

            val whoStarts = this.drawWhoStarts()
            when (whoStarts) {
                1 -> {
                    println("🎯 Turę $i rozpoczyna bohater!")
                    this.loadUserMove(hero, monster)
                    Thread.sleep(600)
                    this.doMonsterMove(monster, hero)
                }
                2 -> {
                    println("⚔️ Turę $i rozpoczyna potwór!")
                    this.doMonsterMove(monster, hero)
                    Thread.sleep(600)
                    this.loadUserMove(hero, monster)
                }
            }
            i++
        }

        println("-".repeat(40))
        if (hero.health > 0) battleSummary(1, round, hero)
        else battleSummary(2, round, hero)
        hero.elixirUsed = false
        monster.elixirUsed = false
    }

    fun loadUserMove (hero: Hero, monster: Monster)
    {
        var message = "\n❓ Co chcesz zrobić? (1 - Atak, 2 - Obrona, 3 - Eliksir)"
        while (true) {
            when (readLine()?.trim()) {
                "1" -> {hero.attack(monster); break}
                "2" -> {hero.defense(); break}
                "3" -> {
                    if (!hero.elixirUsed) {
                        hero.drinkElixir()
                        break
                    } else {
                        println("🚫 Użyłeś już elixiru nie masz go więcej!")
                        continue
                    }
                }

                else -> println("❌ Niepoprawny wybór.")
            }
        }
    }

    fun doMonsterMove (monster: Monster, hero: Hero)
    {
        if (monster.health > 0) {
            val move = this.drawMonsterMove()
            when (move) {
                1 -> monster.attack(hero)
                2 -> monster.defense()
                3 -> {
                    if (!monster.elixirUsed) {
                        monster.drinkElixir()
                    } else {
                        this.doMonsterMove(monster, hero)
                    }
                }
            }
        }
    }

    fun drawWhoStarts (): Int
    {
        val whoStartsTurn:Int = Random.Default.nextInt(1,3)
        return whoStartsTurn
    }

    fun drawMonsterMove (): Int
    {
        val move:Int = Random.Default.nextInt(1,4)
        return move
    }

    // Metoda podsumowująca wynik walki
    fun battleSummary(result: Int, round: Int, hero: Hero) {
        var goldChange = 0
        var regeneratedHealth = 0

        println("\n" + "=".repeat(50))
        when (result) {
            1 -> {
                println("🏆  Wygrałeś walkę!")
                goldChange = 25 * round
                hero.gold += goldChange
                regeneratedHealth = 25 * round
                hero.health += regeneratedHealth
                println("""
                    🪙 Zdobyłeś ${goldChange} sztuk złota.
                    🩹 Odzyskałeś ${regeneratedHealth} punktów zdrowia.
                    👝 Posiadasz w sakiewce ${hero.gold} sztuk złota.
                    💖 Masz ${hero.health} punktów zdrowia.
                    """.trimIndent()
                )
            }
            2 -> {
                println("💀  Przegrałeś walkę...")
                goldChange = 10 * round
                hero.gold -= goldChange
                regeneratedHealth = 15 * round
                hero.health += regeneratedHealth
                println("""
                    🪙 Straciłeś ${goldChange} sztuk złota.
                    🩹 Odzyskałeś ${regeneratedHealth} punktów zdrowia.
                    👝 Posiadasz w sakiewce ${hero.gold} sztuk złota.
                    💖 Masz ${hero.health} punktów zdrowia.
                    """.trimIndent()
                )
            }
        }

        println("=" .repeat(50) + "\n")
    }
}