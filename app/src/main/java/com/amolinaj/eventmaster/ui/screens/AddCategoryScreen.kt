package com.amolinaj.eventmaster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amolinaj.eventmaster.R
import com.amolinaj.eventmaster.ui.components.EventMasterTextField
import com.amolinaj.eventmaster.ui.state.CategoryFormValidationErrors
import com.amolinaj.eventmaster.ui.viewmodel.EventMasterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    viewModel: EventMasterViewModel,
    onBack: () -> Unit
) {
    var categoryName by rememberSaveable { mutableStateOf("") }
    var categoryDescription by rememberSaveable { mutableStateOf("") }
    var errors by remember { mutableStateOf(CategoryFormValidationErrors()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.create_category_title)) },
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
            Text(
                text = stringResource(id = R.string.create_category_description),
                style = MaterialTheme.typography.bodyMedium
            )

            EventMasterTextField(
                value = categoryName,
                onValueChange = {
                    categoryName = it
                    if (errors.nameError != null) {
                        errors = errors.copy(nameError = null)
                    }
                },
                label = stringResource(id = R.string.category_name_label),
                errorMessage = errors.nameError
            )

            EventMasterTextField(
                value = categoryDescription,
                onValueChange = {
                    categoryDescription = it
                    if (errors.descriptionError != null) {
                        errors = errors.copy(descriptionError = null)
                    }
                },
                label = stringResource(id = R.string.category_description_label),
                errorMessage = errors.descriptionError,
                maxLines = 3
            )

            Button(onClick = {
                val validation = viewModel.addCategory(
                    name = categoryName,
                    description = categoryDescription
                )
                errors = validation
                if (!validation.hasErrors) {
                    onBack()
                }
            }) {
                Text(text = stringResource(id = R.string.save_category))
            }
        }
    }
}
