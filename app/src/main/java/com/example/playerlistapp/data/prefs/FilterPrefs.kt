package com.example.playerlistapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ✅ DataStore tanımı (Context extension)
val Context.dataStore by preferencesDataStore(name = "filter_prefs")

// ✅ Filtre ayarlarını temsil eden veri sınıfı
data class FilterState(
    val query: String = "",
    val minRating: Int = 0,
    val genre: String = ""
)

// ✅ DataStore yönetim sınıfı
class FilterPrefs(private val context: Context) {

    // Anahtar tanımları (sadece bu sınıfa özel)
    private object Keys {
        val QUERY = stringPreferencesKey("query")
        val MIN_RATING = intPreferencesKey("min_rating")
        val GENRE = stringPreferencesKey("genre")
    }

    // DataStore'dan filtre ayarlarını sürekli izleyen Flow
    val stateFlow: Flow<FilterState> = context.dataStore.data.map { prefs ->
        FilterState(
            query = prefs[Keys.QUERY] ?: "",
            minRating = prefs[Keys.MIN_RATING] ?: 0,
            genre = prefs[Keys.GENRE] ?: ""
        )
    }

    // Ayarları kaydetme
    suspend fun save(state: FilterState) {
        context.dataStore.edit { prefs ->
            prefs[Keys.QUERY] = state.query
            prefs[Keys.MIN_RATING] = state.minRating
            prefs[Keys.GENRE] = state.genre
        }
    }

    // Varsayılan değer kontrolü
    fun isDefault(state: FilterState): Boolean {
        return state.query.isBlank() &&
                state.minRating == 0 &&
                state.genre.isBlank()
    }
}

