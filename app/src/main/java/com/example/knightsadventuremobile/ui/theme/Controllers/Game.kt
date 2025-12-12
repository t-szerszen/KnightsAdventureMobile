package com.example.knightsadventuremobile.ui.theme.Controllers

import Classes.City
import Classes.MonsterFactory
import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Classes.Monster

// Główna klasa gry definiująca obiekt game.
class Game ()
{
    private lateinit var hero: Hero
    private lateinit var opponent: Monster
    private var city: City = City()
    private var fightmode: FightMode = FightMode()
    private var monsterFactory: MonsterFactory = MonsterFactory()

    // Metoda tworząca bohatera
    fun createCharacter() {
        var heroName: String
        var firstItem: String

        while (true) {
            println("🧙‍♂️ Stwórz swojego bohatera! Podaj jego imię:")
            heroName = readLine()?.trim().orEmpty()

            println("🛡️ Wybierz jeden początkowy przedmiot:\n1 - Hełm\n2 - Kolczuga\n3 - Nagolenniki\n4 - Miecz")
            val itemChoice = readLine()?.toIntOrNull()

            if (heroName.isNotBlank() && itemChoice in 1..4) {
                firstItem = when (itemChoice) {
                    1 -> "Hełm"
                    2 -> "Kolczuga"
                    3 -> "Nagolenniki"
                    4 -> "Miecz"
                    else -> continue
                }
                break
            } else {
                println("❌ Niepoprawne dane, spróbuj ponownie.")
            }
        }

        val equipment = mutableListOf<String>(firstItem)
        hero = Hero(heroName, 100, 20, equipment, false, 0.0, false)

        println("\n✅ Stworzyłeś bohatera: ${hero.name}")
        println("🎒 Początkowy przedmiot: ${hero.equipment.joinToString()}")
        println("-".repeat(50))
    }

    fun selectPath (round: Int): Int {
        println("Gdzie udajesz się teraz? (1 - Arena, 2 - Wyprawa, 3 - Miasto)")
        val path = readLine()?.toIntOrNull()

        return when (path) {
            1, 2, 3 -> path
            else -> {
                println("❌ Niepoprawny wybór, spróbuj ponownie.")
                selectPath(round) // rekurencyjny powrót
            }
        } ?: 1 // domyślnie arena
    }

    fun analyzePath (place: Int, round: Int)
    {
        when (place) {
            1 -> {
                opponent = monsterFactory.generateMonster(round)
                println("👾 Na arenę wchodzi potwór: ${opponent.name}")
                println("   HP: ${opponent.health}, Atak: ${opponent.attackPower}")
                fightmode.startBattle(round, hero, opponent)
            }
            2 -> {
                opponent = monsterFactory.generateDragon(round)
                println("🐉 Spotykasz smoka: ${opponent.name}")
                println("   HP: ${opponent.health}, Atak: ${opponent.attackPower}")
                fightmode.startBattle(round, hero, opponent)
            }
            3 -> {
                city.visitMarketplace(hero, round)
            }
            else -> {
                println("🚫 Nie ma takiej ścieżki! Wybierz poprawną.")
                this.selectPath(round)
            }
        }
    }
}