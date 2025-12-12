package Classes

import com.example.knightsadventuremobile.ui.theme.Classes.Monster

class MonsterFactory {
    fun generateMonster (round: Int): Monster
    {
        val health = 50 + round * 10
        val attack = 10 + round * 3
        val monsterName = "Potwór #$round"

        return Monster(monsterName, health, attack,  mutableListOf(), false,
            0.0, false)
    }

    fun generateDragon (round: Int): Monster
    {
        val health = 150 + round * 3
        val attack = 20 + round * 2
        val monsterName = "Dragon #$round"

        return Monster(monsterName, health, attack, mutableListOf(), false,
            0.0, false)
    }
}