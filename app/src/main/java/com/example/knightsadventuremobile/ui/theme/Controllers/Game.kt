package com.example.knightsadventuremobile.ui.theme.Controllers

import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Classes.Monster
import com.example.knightsadventuremobile.ui.theme.Classes.MonsterFactory

class Game {
    var hero: Hero? = null
    var opponent: Monster? = null
    val city: City = City()
    val fightmode: FightMode = FightMode()
    private val monsterFactory: MonsterFactory = MonsterFactory()

    fun initHero(name: String, itemChoice: Int) {
        val firstItem = when (itemChoice) {
            1 -> "Hełm"
            2 -> "Kolczuga"
            3 -> "Nagolenniki"
            4 -> "Miecz"
            else -> "Miecz"
        }
        val equipment = mutableListOf(firstItem)
        hero = Hero(name, 100, 20, equipment, false, 0.0, false)
    }

    fun prepareArena(round: Int): Monster {
        val monster = monsterFactory.generateMonster(round)
        opponent = monster
        return monster
    }

    fun prepareExpedition(round: Int): Monster {
        val dragon = monsterFactory.generateDragon(round)
        opponent = dragon
        return dragon
    }
}