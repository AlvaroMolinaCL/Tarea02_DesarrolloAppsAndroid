package com.amolinaj.eventmaster.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amolinaj.eventmaster.ui.screens.AddCategoryScreen
import com.amolinaj.eventmaster.ui.screens.AddEventScreen
import com.amolinaj.eventmaster.ui.screens.EventDetailScreen
import com.amolinaj.eventmaster.ui.screens.HomeScreen
import com.amolinaj.eventmaster.ui.viewmodel.EventMasterViewModel

private object Destinations {
    const val HOME = "home"
    const val ADD_CATEGORY = "add_category"
    const val ADD_EVENT = "add_event"
    const val ADD_EVENT_WITH_ARG = "add_event?categoryId={categoryId}"
    const val DETAIL = "detail/{eventId}"
}

private fun buildAddEventRoute(categoryId: Int?): String {
    return if (categoryId == null) {
        Destinations.ADD_EVENT
    } else {
        "${Destinations.ADD_EVENT}?categoryId=$categoryId"
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val eventMasterViewModel = remember { EventMasterViewModel() }
    val context = LocalContext.current

    val resolveImageResId: (String?) -> Int = { imageResName ->
        if (imageResName.isNullOrBlank()) {
            0
        } else {
            context.resources.getIdentifier(imageResName, "drawable", context.packageName)
        }
    }

    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) {
            HomeScreen(
                uiState = eventMasterViewModel.uiState,
                onCreateCategory = { navController.navigate(Destinations.ADD_CATEGORY) },
                onCreateEvent = { categoryId ->
                    navController.navigate(buildAddEventRoute(categoryId))
                },
                onOpenEventDetail = { eventId ->
                    navController.navigate("detail/$eventId")
                },
                getEventsByCategory = eventMasterViewModel::getEventsByCategory,
                resolveImageResId = resolveImageResId
            )
        }

        composable(Destinations.ADD_CATEGORY) {
            AddCategoryScreen(
                viewModel = eventMasterViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Destinations.ADD_EVENT_WITH_ARG,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")?.takeIf { it != -1 }
            AddEventScreen(
                viewModel = eventMasterViewModel,
                categories = eventMasterViewModel.uiState.categories,
                preselectedCategoryId = categoryId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Destinations.DETAIL,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: return@composable
            val event = eventMasterViewModel.getEventById(eventId)
            val category = event?.categoryId?.let(eventMasterViewModel::getCategoryById)
            EventDetailScreen(
                event = event,
                category = category,
                imageResId = resolveImageResId(event?.imageResName),
                onBack = { navController.popBackStack() }
            )
        }
    }
}