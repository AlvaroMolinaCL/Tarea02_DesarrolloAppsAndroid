package com.amolinaj.eventmaster.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amolinaj.eventmaster.R
import com.amolinaj.eventmaster.ui.components.EventMasterTextField
import com.amolinaj.eventmaster.ui.model.EventCategory
import com.amolinaj.eventmaster.ui.state.EventFormValidationErrors
import com.amolinaj.eventmaster.ui.viewmodel.EventMasterViewModel

@SuppressLint("DiscouragedApi")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    viewModel: EventMasterViewModel,
    categories: List<EventCategory>,
    preselectedCategoryId: Int?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var imageResName by rememberSaveable { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable { mutableStateOf(preselectedCategoryId) }
    var showCategoryMenu by rememberSaveable { mutableStateOf(false) }
    var errors by remember { mutableStateOf(EventFormValidationErrors()) }

    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name
        ?: stringResource(id = R.string.select_category_placeholder)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.add_event_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = stringResource(id = R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (categories.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.need_category_for_event),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onBack) {
                    Text(text = stringResource(id = R.string.back))
                }
                return@Column
            }

            Text(
                text = stringResource(id = R.string.create_event_description),
                style = MaterialTheme.typography.bodyMedium
            )
            EventMasterTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (errors.titleError != null) {
                        errors = errors.copy(titleError = null)
                    }
                },
                label = stringResource(id = R.string.event_title_label),
                errorMessage = errors.titleError
            )

            EventMasterTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (errors.descriptionError != null) {
                        errors = errors.copy(descriptionError = null)
                    }
                },
                label = stringResource(id = R.string.event_description_label),
                errorMessage = errors.descriptionError,
                maxLines = 4
            )

            EventMasterTextField(
                value = date,
                onValueChange = {
                    date = it
                    if (errors.dateError != null) {
                        errors = errors.copy(dateError = null)
                    }
                },
                label = stringResource(id = R.string.event_date_label),
                errorMessage = errors.dateError
            )

            EventMasterTextField(
                value = location,
                onValueChange = {
                    location = it
                    if (errors.locationError != null) {
                        errors = errors.copy(locationError = null)
                    }
                },
                label = stringResource(id = R.string.event_location_label),
                errorMessage = errors.locationError
            )

            EventMasterTextField(
                value = imageResName,
                onValueChange = {
                    imageResName = it
                    if (errors.imageError != null) {
                        errors = errors.copy(imageError = null)
                    }
                },
                label = stringResource(id = R.string.event_image_label),
                errorMessage = errors.imageError
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showCategoryMenu = true }
                ) {
                    Text(text = selectedCategoryName)
                }
                DropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(text = category.name) },
                            onClick = {
                                selectedCategoryId = category.id
                                showCategoryMenu = false
                                if (errors.categoryError != null) {
                                    errors = errors.copy(categoryError = null)
                                }
                            }
                        )
                    }
                }
                if (errors.categoryError != null) {
                    Text(
                        text = errors.categoryError.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(onClick = {
                val normalizedImageName = imageResName.trim()
                val imageExists = normalizedImageName.isBlank() ||
                    context.resources.getIdentifier(
                        normalizedImageName,
                        "drawable",
                        context.packageName
                    ) != 0

                val validation = viewModel.addEvent(
                    title = title,
                    description = description,
                    date = date,
                    location = location,
                    categoryId = selectedCategoryId,
                    imageResName = imageResName,
                    imageExists = imageExists
                )
                errors = validation
                if (!validation.hasErrors) {
                    onBack()
                }
            }) {
                Text(text = stringResource(id = R.string.save_event))
            }
        }
    }
}
