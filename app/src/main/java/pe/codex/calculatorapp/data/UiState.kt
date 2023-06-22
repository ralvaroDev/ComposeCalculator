package pe.codex.calculatorapp.data

data class UiState(
    val operation1: String = "",
    val operation2: String = "",
    val operationType: ButtonType.Operation? = null,
    val result: String = ""
)

