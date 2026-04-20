package com.amolinaj.eventmaster.ui.model

data class EventCategory(
    val id: Int,
    val name: String,
    val description: String = ""
)

data class EventItem(
    val id: Int,
    val categoryId: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val imageResName: String? = null
)