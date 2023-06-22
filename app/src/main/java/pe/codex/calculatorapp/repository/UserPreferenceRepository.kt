package pe.codex.calculatorapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Este object almacena el valor de las llaves de DataStore
     */
    private object PreferencesKeys {
        val IS_NIGHT_MODE = booleanPreferencesKey("isNightMode")
    }

    suspend fun initialState(isNightMode: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_NIGHT_MODE] = isNightMode
        }
    }
    val initialState: Flow<Boolean> =
        dataStore.data.map { it[PreferencesKeys.IS_NIGHT_MODE] ?: false }

}