package Classes

import com.example.knightsadventuremobile.ui.theme.Classes.Hero

class City {
    fun visitMarketplace(hero: Hero, round: Int) {
        val helmetPrice = 25 * round
        val chestArmorPrice = 25 * round
        val legsArmorPrice = 25 * round
        val swordPrice = 50 * round

        while (true) {
            println("\n🏙️  Witaj w Krakowie, ${hero.name}!")
            println("💰 Masz w sakiewce: ${hero.gold} sztuk złota.")
            println(
                """
            ================================
            🏪 Sklep z uzbrojeniem:
            1️⃣  Hełm - ${helmetPrice} złota
            2️⃣  Kolczuga - ${chestArmorPrice} złota
            3️⃣  Nagolenniki - ${legsArmorPrice} złota
            4️⃣  Miecz - ${swordPrice} złota
            5️⃣  Powrót na szlak
            ================================
            """.trimIndent()
            )

            print("🛒 Co chcesz kupić? Wybierz numer: ")
            when (readLine()?.trim()) {
                "1" -> buyItem(hero, "Hełm", helmetPrice)
                "2" -> buyItem(hero, "Kolczuga", chestArmorPrice)
                "3" -> buyItem(hero, "Nagolenniki", legsArmorPrice)
                "4" -> buyItem(hero, "Miecz", swordPrice)
                "5" -> {
                    println("🚶 Wracasz na drogę przygody!\n")
                    break
                }
                else -> println("❌ Niepoprawny wybór, spróbuj ponownie.")
            }
        }
    }

    private fun buyItem(hero: Hero, itemName: String, price: Int) {
        if (hero.gold < price) {
            println("💸 Nie masz wystarczająco złota, ${hero.name}! Potrzebujesz ${price}, a masz tylko ${hero.gold}.")
            return
        }

        if (hero.equipment.contains(itemName)) {
            println("⚠️ Już posiadasz ${itemName}! Nie potrzebujesz drugiego.")
            return
        }

        hero.gold -= price
        hero.equipment += itemName  // ✅ dodaje do tablicy nowy element
        println("✅ Kupiłeś ${itemName}! Pozostało ci ${hero.gold} sztuk złota.")
    }
}