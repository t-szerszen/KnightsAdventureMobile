package com.example.knightsadventuremobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.knightsadventuremobile.ui.theme.Classes.Hero
import com.example.knightsadventuremobile.ui.theme.Classes.Monster
import com.example.knightsadventuremobile.ui.theme.Controllers.Game
import com.example.knightsadventuremobile.ui.theme.KnightsAdventureMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnightsAdventureMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KnightsAdventureApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class GameState {
    CREATION, MENU, ARENA, EXPEDITION, CITY, GAME_OVER
}

@Composable
fun KnightsAdventureApp(modifier: Modifier = Modifier) {
    val game = remember { Game() }
    var currentState by remember { mutableStateOf(GameState.CREATION) }
    var round by remember { mutableIntStateOf(1) }
    var logs by remember { mutableStateOf(listOf("Witaj w Knights Adventure!")) }

    fun addLog(msg: String) {
        logs = (listOf(msg) + logs).take(15)
    }

    fun clearLogs(initialMsg: String) {
        logs = listOf(initialMsg)
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (currentState) {
            GameState.CREATION -> CharacterCreationScreen { name, itemIndex ->
                game.initHero(name, itemIndex)
                currentState = GameState.MENU
            }
            GameState.MENU -> LocationSelectionScreen(
                hero = game.hero!!,
                round = round,
                onSelect = { path ->
                    when (path) {
                        1 -> {
                            game.prepareArena(round)
                            clearLogs("⚔️ Rozpoczynasz walkę na arenie (Runda $round)")
                            currentState = GameState.ARENA
                        }
                        2 -> {
                            game.prepareExpedition(round)
                            clearLogs("🐉 Wyruszasz na niebezpieczną wyprawę (Runda $round)")
                            currentState = GameState.EXPEDITION
                        }
                        3 -> currentState = GameState.CITY
                    }
                }
            )
            GameState.ARENA, GameState.EXPEDITION -> BattleScreen(
                game = game,
                round = round,
                logs = logs,
                onLog = ::addLog,
                onBattleEnd = { victory ->
                    val summary = game.fightmode.getBattleSummary(victory, round, game.hero!!)
                    addLog(summary)
                    if (game.hero!!.health <= 0) {
                        currentState = GameState.GAME_OVER
                    } else {
                        round++
                        currentState = GameState.MENU
                    }
                },
                onExit = {
                    currentState = GameState.MENU
                }
            )
            GameState.CITY -> CityScreen(
                game = game,
                round = round,
                onExit = { currentState = GameState.MENU }
            )
            GameState.GAME_OVER -> GameOverScreen(game.hero!!) {
                currentState = GameState.CREATION
                round = 1
                logs = listOf("Witaj w Knights Adventure!")
            }
        }
    }
}

@Composable
fun CharacterCreationScreen(onFinished: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var selectedItem by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🧙‍♂️ Stwórz swojego bohatera!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        TextField(value = name, onValueChange = { name = it }, label = { Text("Imię bohatera") })
        Spacer(Modifier.height(16.dp))
        Text("🛡️ Wybierz początkowy przedmiot:")
        val items = listOf("Hełm", "Kolczuga", "Nagolenniki", "Miecz")
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = (selectedItem == index + 1), onClick = { selectedItem = index + 1 })
                Text(item)
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { if (name.isNotBlank()) onFinished(name, selectedItem) },
            enabled = name.isNotBlank()
        ) {
            Text("Rozpocznij przygodę")
        }
    }
}

@Composable
fun LocationSelectionScreen(hero: Hero, round: Int, onSelect: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Runda: $round", fontSize = 20.sp, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Bohater: ${hero.name}", fontWeight = FontWeight.Bold)
                Text("❤️ HP: ${hero.health}")
                Text("💰 Złoto: ${hero.gold}")
                Text("🎒 Ekwipunek: ${hero.equipment.joinToString()}")
            }
        }
        Spacer(Modifier.height(32.dp))
        Text("Gdzie udajesz się teraz?", fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onSelect(1) }, modifier = Modifier.fillMaxWidth()) { Text("🏟️ Arena") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onSelect(2) }, modifier = Modifier.fillMaxWidth()) { Text("🐉 Wyprawa") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onSelect(3) }, modifier = Modifier.fillMaxWidth()) { Text("🏙️ Miasto") }
    }
}

@Composable
fun BattleScreen(
    game: Game,
    round: Int,
    logs: List<String>,
    onLog: (String) -> Unit,
    onBattleEnd: (Boolean) -> Unit,
    onExit: () -> Unit
) {
    val hero = game.hero!!
    val opponent = game.opponent!!
    var updateTrigger by remember { mutableIntStateOf(0) }
    val isBattleStarted = logs.size > 1

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("⚔️ Walka", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (!isBattleStarted) {
                TextButton(onClick = onExit) {
                    Text("🚶 Wróć na szlak", color = Color.Gray)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(hero.name, fontWeight = FontWeight.Bold)
                Text("❤️ HP: ${hero.health}")
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(opponent.name, fontWeight = FontWeight.Bold)
                Text("❤️ HP: ${opponent.health}")
            }
        }

        Spacer(Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            Text(logs.joinToString("\n"), fontSize = 14.sp)
        }

        Spacer(Modifier.height(16.dp))

        if (hero.health > 0 && opponent.health > 0) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    onLog(game.fightmode.executeHeroAttack(hero, opponent))
                    if (opponent.health > 0) {
                        onLog(game.fightmode.executeMonsterMove(opponent, hero))
                    }
                    updateTrigger++
                }) { Text("Atak") }
                
                Button(onClick = {
                    onLog(game.fightmode.executeHeroDefense(hero))
                    onLog(game.fightmode.executeMonsterMove(opponent, hero))
                    updateTrigger++
                }) { Text("Obrona") }
                
                Button(onClick = {
                    onLog(game.fightmode.executeHeroDrinkElixir(hero))
                    onLog(game.fightmode.executeMonsterMove(opponent, hero))
                    updateTrigger++
                }, enabled = !hero.elixirUsed) { Text("Eliksir") }
            }
        } else {
            Button(
                onClick = { onBattleEnd(hero.health > 0) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (hero.health > 0) "Odbierz nagrodę" else "Zakończ")
            }
        }
    }
}

@Composable
fun CityScreen(game: Game, round: Int, onExit: () -> Unit) {
    val hero = game.hero!!
    val prices = game.city.getPrices(round)
    var message by remember { mutableStateOf("Witaj w sklepie!") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🏙️ Sklep w Krakowie", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("💰 Masz: ${hero.gold} złota", fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))
        Text(message, color = Color.Blue)
        Spacer(Modifier.height(16.dp))

        prices.forEach { (name, price) ->
            Row(
                Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$name ($price złota)")
                Button(onClick = {
                    message = game.city.buyItem(hero, name, price)
                }) { Text("Kup") }
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(onClick = onExit) { Text("Wróć na szlak") }
    }
}

@Composable
fun GameOverScreen(hero: Hero, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("💀 KONIEC GRY", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Red)
        Spacer(Modifier.height(16.dp))
        Text("${hero.name} poległ w chwale...", fontSize = 20.sp)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onRestart) { Text("Zacznij od nowa") }
    }
}