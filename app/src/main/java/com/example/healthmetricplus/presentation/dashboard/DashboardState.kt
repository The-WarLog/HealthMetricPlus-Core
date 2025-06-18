package com.example.healthmetricplus.presentation.dashboard

import com.example.healthmetricplus.domain.model.BodyPart
import com.example.healthmetricplus.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyParts: List<BodyPart> = emptyList(),
    val isSignOutButtonLoading: Boolean = false,
    val isSignInButtonLoading: Boolean = false,
)
