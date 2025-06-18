package com.example.healthmetricplus.presentation.add_item

import com.example.healthmetricplus.domain.model.BodyPart

data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyParts: List<BodyPart> = emptyList()
)
