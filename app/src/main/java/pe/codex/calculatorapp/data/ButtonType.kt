package pe.codex.calculatorapp.data

sealed class ButtonType {
    data class Operation(val operationType: OperationType, val symbol: String) : ButtonType()
    data class Number(val value: String): ButtonType()
    object EraseLast: ButtonType()
    object Reset: ButtonType()
    object Dot: ButtonType()
}

sealed class OperationType {
    object Add : OperationType()
    object Minus : OperationType()
    object Divider : OperationType()
    object Equal : OperationType()
    object For : OperationType()
}

val digitList = listOf<ButtonType.Number>(
    ButtonType.Number("1"),
    ButtonType.Number("2"),
    ButtonType.Number("3"),
    ButtonType.Number("4"),
    ButtonType.Number("5"),
    ButtonType.Number("6"),
    ButtonType.Number("7"),
    ButtonType.Number("8"),
    ButtonType.Number("9")
)

val operationList = listOf(
    ButtonType.Operation(operationType = OperationType.Add, symbol = "+"),
    ButtonType.Operation(operationType = OperationType.Minus, symbol = "-"),
    ButtonType.Operation(operationType = OperationType.Divider, symbol = "/"),
    ButtonType.Operation(operationType = OperationType.Equal, symbol = "="),
    ButtonType.Operation(operationType = OperationType.For, symbol = "x"),
)