package com.example.healthmetricplus.data.mapper

import com.example.healthmetricplus.domain.model.User

data class UserDto(
    val name: String = "Anonymous",
    val email: String = "anonymous@measuremate.io",
    val profilePictureUrl: String = "",
    val anonymous: Boolean = true,
    val userId: String? = null
)

fun UserDto.toUser(): User {
    return User(
        name = name,
        email = email,
        profilePictureUrl = profilePictureUrl,
        isAnonymous = anonymous,
        userId = userId
    )
}
