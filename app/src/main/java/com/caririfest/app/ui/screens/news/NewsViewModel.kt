package com.caririfest.app.ui.screens.news

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caririfest.app.R
import com.caririfest.data.datasource.model.EventEntity
import com.caririfest.data.datasource.repository.EventsRepository
import com.caririfest.network.model.Document
import com.caririfest.network.model.EventFields
import com.caririfest.network.model.FirestoreBoolean
import com.caririfest.network.model.FirestoreString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: EventsRepository,
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Categories>>(emptyList())
    val categories: StateFlow<List<Categories>> = _categories.asStateFlow()

    private val _recentEvents = MutableStateFlow<List<EventEntity>>(emptyList())
    val recentEvents: MutableStateFlow<List<EventEntity>> = _recentEvents

    init {
        loadCategories()
    }

    val events: StateFlow<FetchEventsUiState> =
        repository.getEventsFlow()
            .map { list ->
                FetchEventsUiState(
                    isLoading = false,
                    events = list.map { it.toDocument() },
                    error = null
                )
            }.catch { e ->
                emit(
                    FetchEventsUiState(
                        isLoading = false,
                        error = "Sem conexão com a Internet! Vamos tentar novamente?"
                    )
                )
                Log.e("NewsViewModel", "Error collecting events: ${e.message}")
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                FetchEventsUiState(isLoading = true)
            )

    private fun loadCategories() {
        _categories.value = listOf(
            Categories(
                id = 1,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Cultura & Tradição"
            ),
            Categories(
                id = 2,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Música & Entretenimento"
            ),
            Categories(
                id = 3,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Gastronomia"
            ),
            Categories(
                id = 4,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Esporte & Bem-Estar"
            ),
            Categories(
                id = 5,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Educação & Negócios"
            ),
            Categories(
                id = 6,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Família & Comunidade"
            ),
            Categories(
                id = 7,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Cinema & Arte"
            ),
            Categories(
                id = 8,
                image = R.drawable.caririfestlogo1,
                nameCategories = "Turismo & Natureza"
            )
        )
    }

    fun loadEvents() {
        viewModelScope.launch {
            _recentEvents.value = repository.getRecentlyViewedEvents()
        }
    }

    fun onEventOpened(event: EventEntity) {
        viewModelScope.launch {
            repository.markAsViewed(event)
        }
    }
}


private fun EventEntity.toDocument(): Document {
    return Document(
        name = this.id,
        fields = EventFields(
            title = FirestoreString(this.title),
            desc = FirestoreString(this.desc),
            date = FirestoreString(this.date),
            img = FirestoreString(this.img),
            location = FirestoreString(this.location),
            place = FirestoreString(this.place),
            time = FirestoreString(this.time),
            favorite = FirestoreBoolean(this.favorite),
            hot = FirestoreBoolean(this.hot),
            link = FirestoreString(this.link)
        )
    )
}

data class FetchEventsUiState(
    val isLoading: Boolean = false,
    val events: List<Document> = emptyList(),
    val error: String? = null
)

data class Categories(
    val id: Int,
    @param:DrawableRes val image: Int,
    val nameCategories: String = ""
)