package com.example.healthmetricplus.presentation.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object SignInScreen : Routes()
    @Serializable
    data object DashboardScreen : Routes()
    @Serializable
    data object AddItemScreen : Routes()
    @SuppressLint("UnsafeOptInUsageError")
    @Serializable
    data class DetailsScreen(val bodyPartId: String) : Routes()
}