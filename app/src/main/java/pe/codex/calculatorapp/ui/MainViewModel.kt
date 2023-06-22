package pe.codex.calculatorapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.codex.calculatorapp.repository.UserPreferenceRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    val initialScheme = userPreferenceRepository.initialState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun changeScheme() {
        viewModelScope.launch {
            userPreferenceRepository.initialState(!initialScheme.value)
        }
    }

}