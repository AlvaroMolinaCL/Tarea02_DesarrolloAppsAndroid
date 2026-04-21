package com.amolinaj.eventmaster.ui.state

import com.amolinaj.eventmaster.ui.model.EventCategory
import com.amolinaj.eventmaster.ui.model.EventItem

data class EventMasterUiState(
    val categories: List<EventCategory> = emptyList(),
    val events: List<EventItem> = emptyList()
)

data class CategoryFormValidationErrors(
    val nameError: String? = null,
    val descriptionError: String? = null
) {
    val hasErrors: Boolean
        get() = nameError != null || descriptionError != null
}

data class EventFormValidationErrors(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dateError: String? = null,
    val locationError: String? = null,
    val categoryError: String? = null,
    val imageError: String? = null
) {
    val hasErrors: Boolean
        get() = titleError != null ||
            descriptionError != null ||
            dateError != null ||
            locationError != null ||
            categoryError != null ||
            imageError != null
}
