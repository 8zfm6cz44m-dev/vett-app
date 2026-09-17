@file:OptIn(ExperimentalMaterial3Api::class)

package no.rusinnsikt.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.rusinnsikt.app.data.OnboardingState
import no.rusinnsikt.app.data.SubstanceRepository
import no.rusinnsikt.app.ui.screens.FaktaListScreen
import no.rusinnsikt.app.ui.screens.NodhjelpScreen
import no.rusinnsikt.app.ui.screens.OmScreen
import no.rusinnsikt.app.ui.screens.OnboardingScreen
import no.rusinnsikt.app.ui.screens.PrivacyPolicyScreen
import no.rusinnsikt.app.ui.screens.SubstanceDetailScreen
import no.rusinnsikt.app.ui.screens.SupportScreen
import no.rusinnsikt.app.ui.theme.RusinnsiktTheme

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val tabs = listOf(
    Tab("fakta", "Fakta", Icons.Filled.Book),
    Tab("nodhjelp", "Nødhjelp", Icons.Filled.LocalHospital),
    Tab("sok", "Søk", Icons.Filled.Search),
    Tab("om", "Om", Icons.Filled.Info),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // PERSONVERN: ikke vis et bilde av appens innhold (f.eks. den sist
        // åpnede stoffsiden) i «Nylige apper»-oversikten. Android 13+ kan
        // skjule bare miniatyrbildet uten å blokkere skjermbilder — brukere
        // skal fortsatt kunne ta skjermbilde av nødhjelp for å dele det.
        // FLAG_SECURE ville også blokkert skjermbilder og er derfor bevisst
        // ikke brukt. Speiler PrivacyCover i iOS-appens RusFaktaApp.swift.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setRecentsScreenshotEnabled(false)
        }
        enableEdgeToEdge()
        setContent {
            RusinnsiktTheme {
                RusinnsiktApp()
            }
        }
    }
}

@Composable
fun RusinnsiktApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    // Preload once at startup — same "crash loudly, not silently" intent as
    // the iOS DataStore, which fatalError()s on a missing/malformed bundle.
    SubstanceRepository.load(context)

    var hasSeenOnboarding by remember { mutableStateOf(OnboardingState.hasSeenOnboarding(context)) }

    Surface(color = MaterialTheme.colorScheme.background) {
        if (!hasSeenOnboarding) {
            OnboardingScreen(onContinue = {
                OnboardingState.setHasSeenOnboarding(context, true)
                hasSeenOnboarding = true
            })
        } else {
            MainNavHost(onOnboardingReset = { hasSeenOnboarding = false })
        }
    }
}

@Composable
private fun MainNavHost(onOnboardingReset: () -> Unit) {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val database = SubstanceRepository.load(context)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // Matches iOS: TabView keeps its tab bar visible even when a
    // NavigationStack has pushed a detail view within a tab. Only the
    // dedicated privacy/support screens (reached from "Om", not a tab
    // root) hide it, same as they would push modally on iOS.
    val isTabRoot = tabs.any { it.route == currentRoute }
    val showBottomBar = currentRoute != "privacy" && currentRoute != "support"

    Scaffold(
        topBar = {
            val title = when {
                currentRoute == "fakta" -> "Fakta om rusmidler"
                currentRoute == "nodhjelp" -> "Nødhjelp"
                currentRoute == "nodhjelp-detail" -> "Nødhjelp"
                currentRoute == "sok" -> "Søk"
                currentRoute == "om" -> "Om"
                currentRoute == "privacy" -> "Personvern"
                currentRoute == "support" -> "Støtt Rusinnsikt"
                currentRoute?.startsWith("detail/") == true -> {
                    val id = backStackEntry?.arguments?.getString("substanceId")
                    database.substances.firstOrNull { it.id == id }?.name ?: ""
                }
                else -> ""
            }
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (!isTabRoot) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Tilbake")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "fakta",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("fakta") {
                FaktaListScreen(autoFocusSearch = false) { id, highlight ->
                    navController.navigate("detail/$id/$highlight")
                }
            }
            composable("sok") {
                FaktaListScreen(autoFocusSearch = true) { id, highlight ->
                    navController.navigate("detail/$id/$highlight")
                }
            }
            composable("nodhjelp") { NodhjelpScreen() }
            // Reached only from the "Se full nødhjelp-guide" link on a
            // substance's overdose card — a real push (unlike the
            // "nodhjelp" tab route above), so back/gesture-back returns to
            // the substance instead of wherever the Nødhjelp *tab*'s own
            // saved back stack happens to be. Same screen content, distinct
            // route so it doesn't get folded into the bottom-nav tab logic.
            composable("nodhjelp-detail") { NodhjelpScreen() }
            composable("om") {
                OmScreen(
                    onPrivacyPolicy = { navController.navigate("privacy") },
                    onSupport = { navController.navigate("support") },
                    onOnboardingReset = onOnboardingReset
                )
            }
            composable("privacy") { PrivacyPolicyScreen() }
            composable("support") { SupportScreen() }
            composable(
                route = "detail/{substanceId}/{highlight}",
                arguments = listOf(
                    androidx.navigation.navArgument("substanceId") { type = androidx.navigation.NavType.StringType },
                    androidx.navigation.navArgument("highlight") { type = androidx.navigation.NavType.BoolType }
                )
            ) { entry ->
                val id = entry.arguments?.getString("substanceId")
                val highlight = entry.arguments?.getBoolean("highlight") ?: false
                val substance = database.substances.firstOrNull { it.id == id }
                if (substance != null) {
                    SubstanceDetailScreen(
                        substance = substance,
                        scrollToOverdose = highlight,
                        onNavigateToNodhjelp = {
                            // Plain push — deliberately not the tab-switch
                            // popUpTo/saveState/restoreState pattern used by
                            // the bottom-nav tabs, so back navigates to this
                            // substance page instead of wherever the
                            // Nødhjelp tab's saved state points.
                            navController.navigate("nodhjelp-detail")
                        }
                    )
                } else {
                    UnknownSubstanceScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

/**
 * Shown instead of a blank NavHost destination when a "detail/{id}/..."
 * route is reached with an id that isn't in the loaded database (e.g. a
 * stale deep link). iOS never hits this — it navigates with the whole
 * Substance value rather than an id looked up again on the far side.
 */
@Composable
private fun UnknownSubstanceScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Fant ikke stoffet",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            "Denne siden finnes ikke lenger, eller lenken var feil.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onBack) {
            Text("Tilbake")
        }
    }
}
