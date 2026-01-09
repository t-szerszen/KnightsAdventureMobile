package Classes

import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Models.GameResponse

class City {
    fun visitMarketplace(hero: Hero, round: Int): GameResponse<String, String> {
        val helmetPrice = 25 * round
        val chestArmorPrice = 25 * round
        val legsArmorPrice = 25 * round
        val swordPrice = 50 * round

        var message = ""
        var info = ""

        while (true) {
            info = """
                🏙️  Witaj w Krakowie, ${hero.name}!
                💰 Masz w sakiewce: ${hero.gold} sztuk złota.
                ================================
                🏪 Sklep z uzbrojeniem:
                1️⃣  Hełm - ${helmetPrice} złota
                2️⃣  Kolczuga - ${chestArmorPrice} złota
                3️⃣  Nagolenniki - ${legsArmorPrice} złota
                4️⃣  Miecz - ${swordPrice} złota
                5️⃣  Powrót na szlak
                ================================

                🛒 Co chcesz kupić? Wybierz numer:
            """.trimIndent()
            when (readLine()?.trim()) {
                "1" -> buyItem(hero, "Hełm", helmetPrice)
                "2" -> buyItem(hero, "Kolczuga", chestArmorPrice)
                "3" -> buyItem(hero, "Nagolenniki", legsArmorPrice)
                "4" -> buyItem(hero, "Miecz", swordPrice)
                "5" -> {
                    message = "🚶 Wracasz na drogę przygody!\n"
                    break
                }
                else -> message = "❌ Niepoprawny wybór, spróbuj ponownie."
            }
        }

        return GameResponse(info, message);
    }

    private fun buyItem(hero: Hero, itemName: String, price: Int): String {
        var message = ""
        if (hero.gold < price) {
            message = "💸 Nie masz wystarczająco złota, ${hero.name}! Potrzebujesz ${price}, a masz tylko ${hero.gold}."
            return message
        }

        if (hero.equipment.contains(itemName)) {
            message = "⚠️ Już posiadasz ${itemName}! Nie potrzebujesz drugiego."
            return message
        }

        hero.gold -= price
        hero.equipment += itemName  // ✅ dodaje do tablicy nowy element
        return "✅ Kupiłeś ${itemName}! Pozostało ci ${hero.gold} sztuk złota."
    }
}