package pe.codex.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import pe.codex.calculatorapp.ui.CalculatorApp
import pe.codex.calculatorapp.ui.CalculatorScreen
import pe.codex.calculatorapp.ui.MainViewModel
import pe.codex.calculatorapp.ui.theme.ExtendedTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MainViewModel = hiltViewModel()
            val isNightMode by viewModel.initialScheme.collectAsStateWithLifecycle()
            ExtendedTheme(
                darkTheme = isNightMode
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp(
                        modifier = Modifier.fillMaxSize(),
                        onNightModeChange = viewModel::changeScheme
                    )
                }
            }
        }
    }
}
