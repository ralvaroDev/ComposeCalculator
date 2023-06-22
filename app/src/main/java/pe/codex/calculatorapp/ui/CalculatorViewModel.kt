package pe.codex.calculatorapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.codex.calculatorapp.data.ButtonType
import pe.codex.calculatorapp.data.OperationType
import pe.codex.calculatorapp.data.UiState
import pe.codex.calculatorapp.repository.UserPreferenceRepository
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    init {

    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun digitAdded(input: String) {
        if (_uiState.value.operation1.isNotEmpty() && _uiState.value.operation2.isNotEmpty()) {
            resetOperation()
            digitAdded(input)
        } else if (_uiState.value.operation1.isNotEmpty())
            _uiState.update {
                it.copy(result = it.result + input)
            }
        else
            _uiState.update {
                UiState(result = it.result + input)
            }
    }

    fun doOperation(operationType: ButtonType.Operation) {
        //primer caso es cuando ponemos el primer sumando y le damos a operation
        if (_uiState.value.operation1.isEmpty()) {
            if (operationType.operationType != OperationType.Equal) {
                _uiState.update {
                    UiState(operation1 = it.result, operationType = operationType)
                }
            }
        } else {
            //Aqui el operation 1 ya tiene valor por lo que lo que estamos colocando es el operation 2 y debemos hacer la operacion
            when (operationType.operationType) {
                OperationType.Equal -> {
                    if (_uiState.value.operation2.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                operation2 = it.result,
                                operationType = it.operationType,
                                result = operationTask(
                                    it.operation1,
                                    it.result,
                                    it.operationType?.operationType
                                )
                            )
                        }
                    } else {
                        _uiState.update {
                            UiState(
                                operation1 = it.result,
                                operation2 = it.operation2,
                                operationType = it.operationType,
                                result = operationTask(
                                    it.result,
                                    it.operation2,
                                    it.operationType?.operationType
                                )
                            )
                        }
                    }
                }

                else -> {
                    if (_uiState.value.operation2.isNotEmpty()) {
                        _uiState.update {
                            UiState(
                                operation1 = it.result,
                                operationType = operationType
                            )
                        }
                    } else {
                        _uiState.update {
                            UiState(
                                operation1 = operationTask(
                                    it.operation1,
                                    it.result,
                                    it.operationType?.operationType
                                ),
                                operationType = operationType,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun operationTask(first: String, second: String, operation: OperationType?): String {
        return when (operation) {
            OperationType.Add -> first.toDouble().plus(second.toDouble())
            OperationType.Divider -> first.toDouble() / second.toDouble()
            OperationType.For -> first.toDouble().times(second.toDouble())
            OperationType.Minus -> first.toDouble().minus(second.toDouble())
            else -> -1
        }.toString()
    }

    fun resetOperation() {
        _uiState.update {
            UiState()
        }
    }

    fun eraseLast() {
        if (_uiState.value.operation2.isEmpty()) {
            _uiState.update {
                it.copy(
                    result = it.result.dropLast(1)
                )
            }
        } else {
            resetOperation()
        }
    }

    fun invertPart() {
        if (_uiState.value.operation2.isEmpty()) {
            _uiState.update {
                it.copy(
                    result = "-${it.result}"
                )
            }
        }
    }


}