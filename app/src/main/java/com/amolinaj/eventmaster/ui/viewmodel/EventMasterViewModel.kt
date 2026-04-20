package com.amolinaj.eventmaster.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amolinaj.eventmaster.ui.model.EventCategory
import com.amolinaj.eventmaster.ui.model.EventItem
import com.amolinaj.eventmaster.ui.state.CategoryFormValidationErrors
import com.amolinaj.eventmaster.ui.state.EventFormValidationErrors
import com.amolinaj.eventmaster.ui.state.EventMasterUiState

class EventMasterViewModel : ViewModel() {

    var uiState by mutableStateOf(initialUiState())
        private set

    private var nextCategoryId = uiState.categories.maxOfOrNull { it.id }?.plus(1) ?: 1
    private var nextEventId = uiState.events.maxOfOrNull { it.id }?.plus(1) ?: 1

    fun validateCategory(name: String, description: String): CategoryFormValidationErrors {
        val trimmedName = name.trim()
        val trimmedDescription = description.trim()

        val nameError = when {
            trimmedName.isBlank() -> "La categoría es obligatoria"
            trimmedName.length < 3 -> "Debe tener al menos 3 caracteres"
            uiState.categories.any { it.name.equals(trimmedName, ignoreCase = true) } -> "La categoría ya existe"
            else -> null
        }

        val descriptionError = when {
            trimmedDescription.length > 120 -> "Máximo 120 caracteres"
            else -> null
        }

        return CategoryFormValidationErrors(
            nameError = nameError,
            descriptionError = descriptionError
        )
    }

    fun addCategory(name: String, description: String): CategoryFormValidationErrors {
        val errors = validateCategory(name = name, description = description)
        if (errors.hasErrors) return errors

        val newCategory = EventCategory(
            id = nextCategoryId++,
            name = name.trim(),
            description = description.trim()
        )

        uiState = uiState.copy(categories = uiState.categories + newCategory)
        return CategoryFormValidationErrors()
    }

    fun validateEvent(
        title: String,
        description: String,
        date: String,
        location: String,
        categoryId: Int?
    ): EventFormValidationErrors {
        val titleError = when {
            title.isBlank() -> "El título es obligatorio"
            title.trim().length < 4 -> "Debe tener al menos 4 caracteres"
            else -> null
        }

        val descriptionError = when {
            description.isBlank() -> "La descripción es obligatoria"
            description.trim().length < 10 -> "Debe tener al menos 10 caracteres"
            else -> null
        }

        val dateError = when {
            date.isBlank() -> "La fecha es obligatoria"
            else -> null
        }

        val locationError = when {
            location.isBlank() -> "La ubicación es obligatoria"
            else -> null
        }

        val categoryError = when {
            categoryId == null -> "Selecciona una categoría"
            uiState.categories.none { it.id == categoryId } -> "Categoría inválida"
            else -> null
        }

        return EventFormValidationErrors(
            titleError = titleError,
            descriptionError = descriptionError,
            dateError = dateError,
            locationError = locationError,
            categoryError = categoryError
        )
    }

    fun addEvent(
        title: String,
        description: String,
        date: String,
        location: String,
        categoryId: Int?,
        imageResName: String
    ): EventFormValidationErrors {
        val errors = validateEvent(
            title = title,
            description = description,
            date = date,
            location = location,
            categoryId = categoryId
        )

        if (errors.hasErrors) return errors

        val newEvent = EventItem(
            id = nextEventId++,
            categoryId = categoryId!!,
            title = title.trim(),
            description = description.trim(),
            date = date.trim(),
            location = location.trim(),
            imageResName = imageResName.trim().takeIf { it.isNotBlank() }
        )

        uiState = uiState.copy(events = uiState.events + newEvent)
        return EventFormValidationErrors()
    }

    fun getEventsByCategory(categoryId: Int): List<EventItem> {
        return uiState.events.filter { it.categoryId == categoryId }
    }

    fun getEventById(eventId: Int): EventItem? {
        return uiState.events.find { it.id == eventId }
    }

    fun getCategoryById(categoryId: Int): EventCategory? {
        return uiState.categories.find { it.id == categoryId }
    }

    private fun initialUiState(): EventMasterUiState {
        val defaultCategories = listOf(
            EventCategory(id = 1, name = "Música", description = "Conciertos y festivales"),
            EventCategory(id = 2, name = "Tecnología", description = "Conferencias y meetups"),
            EventCategory(id = 3, name = "Deportes", description = "Eventos deportivos y competencias")
        )

        val defaultEvents = listOf(
            EventItem(
                id = 1,
                categoryId = 1,
                title = "Festival Indie Night",
                description = "Una noche con bandas emergentes y experiencias en vivo.",
                date = "12/06/2026",
                location = "Centro Cultural Andino",
                imageResName = "event_music"
            ),
            EventItem(
                id = 2,
                categoryId = 2,
                title = "Android Dev Summit Local",
                description = "Charlas técnicas sobre Jetpack Compose, arquitectura y pruebas.",
                date = "28/07/2026",
                location = "Hub Tecnológico Central",
                imageResName = "event_tech"
            ),
            EventItem(
                id = 3,
                categoryId = 3,
                title = "Carrera Urbana 10K",
                description = "Competencia abierta para corredores amateurs y profesionales.",
                date = "05/09/2026",
                location = "Parque Metropolitano",
                imageResName = "event_sports"
            )
        )

        return EventMasterUiState(categories = defaultCategories, events = defaultEvents)
    }
}
