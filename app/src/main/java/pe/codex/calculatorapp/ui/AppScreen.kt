package pe.codex.calculatorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.codex.calculatorapp.R
import pe.codex.calculatorapp.data.ButtonType
import pe.codex.calculatorapp.data.OperationType
import pe.codex.calculatorapp.data.UiState
import pe.codex.calculatorapp.data.digitList
import pe.codex.calculatorapp.data.operationList
import pe.codex.calculatorapp.ui.theme.Cyan
import pe.codex.calculatorapp.ui.theme.ExtendedTheme
import pe.codex.calculatorapp.ui.theme.Red

@Composable
fun CalculatorApp(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = hiltViewModel(),
    onNightModeChange: () -> Unit
) {
    val uiState: UiState by viewModel.uiState.collectAsStateWithLifecycle()
    CalculatorScreen(
        modifier = modifier,
        uiState = uiState,
        onDigitAdded = viewModel::digitAdded,
        onOperationClicked = viewModel::doOperation,
        onReset = viewModel::resetOperation,
        onEraseLast = viewModel::eraseLast,
        onInvert = viewModel::invertPart,
        onNightModeChange = onNightModeChange
    )
}

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onDigitAdded: (String) -> Unit,
    onOperationClicked: (ButtonType.Operation) -> Unit,
    onReset: () -> Unit,
    onEraseLast: () -> Unit,
    onInvert: () -> Unit,
    onNightModeChange: () -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .background(color = ExtendedTheme.colors.background)
    ) {
        ThemeModeCard(
            Modifier
                .align(CenterHorizontally)
                .padding(top = 16.dp),
            onClick = onNightModeChange
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            OperationTable(
                modifier = Modifier,
                firstPart = uiState.operation1,
                secondPart = uiState.operation2,
                operation = uiState.operationType?.symbol
            )
            Text(
                text = uiState.result.take(8),
                fontSize = 54.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
        ButtonContainer(
            onDigitAdded = onDigitAdded,
            onOperationClicked = onOperationClicked,
            onReset = onReset,
            onEraseLast = onEraseLast,
            onInvert = onInvert
        )
    }
}

@Composable
fun OperationTable(
    modifier: Modifier = Modifier,
    firstPart: String,
    secondPart: String,
    operation: String?
) {
    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle()) {
            append(firstPart)
        }
        append(" ")
        withStyle(style = SpanStyle(color = Red)) {
            append(operation ?: "")
        }
        append(" ")
        withStyle(style = SpanStyle()) {
            append(secondPart)
        }
    }, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, modifier = modifier)
}

@Composable
fun ButtonContainer(
    modifier: Modifier = Modifier,
    onDigitAdded: (String) -> Unit,
    onOperationClicked: (ButtonType.Operation) -> Unit,
    onReset: () -> Unit,
    onInvert: () -> Unit,
    onEraseLast: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ExtendedTheme.colors.backgroundVariant),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Row(Modifier.padding(16.dp)) {
            //Digitos y reset
            Column(modifier = Modifier.weight(0.75f)) {
                Row(Modifier.fillMaxWidth()) {
                    RectangleDigitButton(
                        modifier = Modifier.weight(2f),
                        onClick = onReset
                    )
                    DigitButton(
                        buttonType = ButtonType.Invert,
                        modifier = Modifier.weight(1f),
                        onClick = onInvert
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 3),
                    reverseLayout = true,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    item {
                        DigitButton(buttonType = ButtonType.EraseLast, onClick = onEraseLast)
                    }
                    item {
                        DigitButton(
                            buttonType = ButtonType.Number("0"),
                            onClick = { onDigitAdded("0") })
                    }
                    item {
                        DigitButton(
                            buttonType = ButtonType.Dot,
                            onClick = { onDigitAdded(".") }
                        )
                    }
                    items(items = digitList, key = { item -> item.value }) {
                        DigitButton(
                            buttonType = it,
                            modifier = Modifier.weight(1f),
                            onClick = { onDigitAdded(it.value) }
                        )
                    }
                }
            }
            //Operaciones
            LazyColumn(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = CenterHorizontally
            ) {
                items(
                    items = operationList,
                    //key = { item -> item.symbol }
                ) {
                    DigitButton(buttonType = it, onClick = { onOperationClicked(it) })
                }
            }

        }
    }
}

@Composable
fun RectangleDigitButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ExtendedTheme.colors.background),
        modifier = modifier
            .aspectRatio(2f)
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = "AC",
            color = Cyan,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

@Composable
fun DigitButton(modifier: Modifier = Modifier, buttonType: ButtonType, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ExtendedTheme.colors.background),
        modifier = modifier
            .aspectRatio(1f)
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        val fontSizeee = 22.sp
        when (buttonType) {
            ButtonType.EraseLast -> Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                tint = ExtendedTheme.colors.onBackground
            )

            is ButtonType.Number -> Text(
                text = buttonType.value,
                color = ExtendedTheme.colors.onBackground,
                fontWeight = FontWeight.Bold, fontSize = fontSizeee
            )

            is ButtonType.Operation -> Text(
                text = buttonType.symbol,
                color = Red,
                fontWeight = FontWeight.Bold, fontSize = fontSizeee
            )

            ButtonType.Dot -> Text(
                text = ".",
                color = ExtendedTheme.colors.onBackground,
                fontWeight = FontWeight.Bold, fontSize = fontSizeee
            )

            else -> Icon(
                painter = painterResource(id = R.drawable.ic_plus_minus),
                contentDescription = "",
                tint = Cyan
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonContainerPreview() {
    ExtendedTheme {
        ButtonContainer(
            onDigitAdded = {},
            onOperationClicked = {},
            onReset = {},
            onEraseLast = {}, onInvert = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeModeCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = ExtendedTheme.colors.backgroundVariant
        ),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(6.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Icon(
                imageVector = Icons.Outlined.LightMode,
                contentDescription = "",
                modifier = Modifier.padding(4.dp),
                tint = ExtendedTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Outlined.ModeNight,
                contentDescription = "",
                modifier = Modifier.padding(4.dp),
                tint = ExtendedTheme.colors.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeModeCardPreview() {
    ExtendedTheme { ThemeModeCard(onClick = {}) }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    ExtendedTheme(darkTheme = true) {
        CalculatorScreen(
            uiState = UiState(
                operation1 = "24",
                operation2 = "255",
                operationType = ButtonType.Operation(OperationType.Add, "+"),
                result = "32435"
            ),
            onDigitAdded = {},
            onOperationClicked = {},
            onReset = {},
            onEraseLast = {},
            onInvert = {},
            onNightModeChange = {}
        )
    }
}