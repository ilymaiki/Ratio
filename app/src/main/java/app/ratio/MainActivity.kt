package app.ratio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.ratio.ui.expense.NewExpenseScreen
import app.ratio.ui.home.HomeScreen
import app.ratio.ui.home.HomeViewModel
import app.ratio.ui.nav.Routes
import app.ratio.ui.theme.RatioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RatioTheme {
                val app = application as RatioApp
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = Routes.HOME) {
                    composable(Routes.HOME) {
                        val vm = remember {
                            HomeViewModel(app.expenseRepo, app.categoryRepo)
                        }
                        HomeScreen(viewModel = vm) { nav.navigate(Routes.NEW) }
                    }
                    composable(Routes.NEW) { NewExpenseScreen() }
                }
            }
        }
    }
}
