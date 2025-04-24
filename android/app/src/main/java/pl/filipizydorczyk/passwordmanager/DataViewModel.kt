package pl.filipizydorczyk.passwordmanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataViewModel(private val context: Context) : ViewModel() {
    private val vaultKey = stringPreferencesKey("vault")

    private val _vaultValue = MutableStateFlow<String?>("")
    val vault: StateFlow<String?> = _vaultValue

    init {
        viewModelScope.launch {
            context.dataStore.data.map { preferences ->
                preferences[vaultKey]
            }.collect { value ->
                _vaultValue.value = value
            }
        }
    }

    fun updateVaultValue(value: String) {
        viewModelScope.launch {
            context.dataStore.updateData { preferences ->
                preferences.toMutablePreferences().apply {
                    this[vaultKey] = value
                }
            }
        }
    }
}
