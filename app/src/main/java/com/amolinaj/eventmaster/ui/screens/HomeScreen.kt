package com.amolinaj.eventmaster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amolinaj.eventmaster.R
import com.amolinaj.eventmaster.ui.components.CategorySection
import com.amolinaj.eventmaster.ui.state.EventMasterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: EventMasterUiState,
    onCreateCategory: () -> Unit,
    onCreateEvent: (Int?) -> Unit,
    onOpenEventDetail: (Int) -> Unit,
    getEventsByCategory: (Int) -> List<com.amolinaj.eventmaster.ui.model.EventItem>,
    resolveImageResId: (String?) -> Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExtendedFloatingActionButton(
                    onClick = onCreateCategory,
                    icon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                    text = { Text(stringResource(id = R.string.create_category)) }
                )
                ExtendedFloatingActionButton(
                    onClick = { onCreateEvent(null) },
                    icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                    text = { Text(stringResource(id = R.string.add_event)) }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.categories.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_categories_message),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(uiState.categories) { category ->
                    CategorySection(
                        category = category,
                        events = getEventsByCategory(category.id),
                        onCreateEventClick = onCreateEvent,
                        onEventClick = onOpenEventDetail,
                        getImageResId = resolveImageResId
                    )
                }
            }
        }
    }
}
