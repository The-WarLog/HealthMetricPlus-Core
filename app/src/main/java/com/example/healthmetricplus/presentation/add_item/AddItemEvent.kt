package com.example.healthmetricplus.presentation.add_item

import com.example.healthmetricplus.domain.model.BodyPart

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value: String): AddItemEvent()
    data class OnItemClick(val bodyPart: BodyPart): AddItemEvent()
    data class OnItemIsActiveChange(val bodyPart: BodyPart): AddItemEvent()
    data object OnAddItemDialogDismiss: AddItemEvent()
    data object UpsertItem: AddItemEvent()
}