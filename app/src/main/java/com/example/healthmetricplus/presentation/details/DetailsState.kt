package com.example.healthmetricplus.presentation.details

import com.example.healthmetricplus.domain.model.BodyPart
import com.example.healthmetricplus.domain.model.BodyPartValue
import com.example.healthmetricplus.domain.model.TimeRange
import java.time.LocalDate

data class DetailsState(
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentlyDeletedBodyPartValue: BodyPartValue? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST7DAYS,
    val allBodyPartValues: List<BodyPartValue> = emptyList(),
    val graphBodyPartValues: List<BodyPartValue> = emptyList()
)
