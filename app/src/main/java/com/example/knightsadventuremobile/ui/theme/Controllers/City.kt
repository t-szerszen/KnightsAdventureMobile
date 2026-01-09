package com.example.knightsadventuremobile.ui.theme.Controllers

import com.example.knightsadventuremobile.ui.theme.Classes.Hero

class City {
    fun getPrices(round: Int): Map<String, Int> {
        return mapOf(
            "Hełm" to 25 * round,
            "Kolczuga" to 25 * round,
            "Nagolenniki" to 25 * round,
            "Miecz" to 50 * round
        )
    }

    fun buyItem(hero: Hero, itemName: String, price: Int): String {
        if (hero.gold < price) {
            return "💸 Nie masz wystarczająco złota, ${hero.name}! Potrzebujesz ${price}, a masz tylko ${hero.gold}."
        }

        if (hero.equipment.contains(itemName)) {
            return "⚠️ Już posiadasz ${itemName}! Nie potrzebujesz drugiego."
        }

        hero.gold -= price
        hero.equipment.add(itemName)
        return "✅ Kupiłeś ${itemName}! Pozostało ci ${hero.gold} sztuk złota."
    }
}