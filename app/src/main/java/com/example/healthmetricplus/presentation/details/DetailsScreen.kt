package com.example.healthmetricplus.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.healthmetricplus.domain.model.BodyPart
import com.example.healthmetricplus.domain.model.BodyPartValue
import com.example.healthmetricplus.domain.model.TimeRange
import com.example.healthmetricplus.presentation.component.LineGraph
import com.example.healthmetricplus.presentation.component.HealthMetricDatePicker
import com.example.healthmetricplus.presentation.component.HealthmetricDialogBox
import com.example.healthmetricplus.presentation.component.MeasuringUnitBottomSheet
import com.example.healthmetricplus.presentation.component.NewValueInputBar
import com.example.healthmetricplus.presentation.theme.HealthMetricTheme
import com.example.healthmetricplus.presentation.util.PastOrPresentSelectableDates
import com.example.healthmetricplus.presentation.util.UiEvent
import com.example.healthmetricplus.presentation.util.changeLocalDateToDateString
import com.example.healthmetricplus.presentation.util.roundToDecimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    snackbarHostState: SnackbarHostState,
    windowSize: WindowWidthSizeClass,
    state: DetailsState,
    onEvent: (DetailsEvent) -> Unit,
    uiEvent: Flow<UiEvent>,
    onBackIconClick: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Snackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onEvent(DetailsEvent.RestoreBodyPartValue)
                    }
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {
                    onBackIconClick()
                }
            }
        }
    }

    val focusManager = LocalFocusManager.current
    var isInputValueCardVisible by rememberSaveable { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isMeasuringUnitBottomSheetOpen by remember { mutableStateOf(false) }
    MeasuringUnitBottomSheet(
        isOpen = isMeasuringUnitBottomSheetOpen,
        sheetState = sheetState,
        onBottomSheetDismiss = { isMeasuringUnitBottomSheetOpen = false },
        onItemClicked = { measuringUnit ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isMeasuringUnitBottomSheetOpen = false
            }
            onEvent(DetailsEvent.ChangeMeasuringUnit(measuringUnit))
        }
    )

    var isDeleteBodyPartDialogOpen by rememberSaveable { mutableStateOf(false) }
    HealthmetricDialogBox(
        isOpen = isDeleteBodyPartDialogOpen,
        title = "Delete Body Part?",
        body = {
            Text(
                text = "Are you sure, you want to delete this Body Part? All related " +
                        "data will be permanently removed. \nThis action can not be undone."
            )
        },
        confirmButtonText = "Delete",
        onDialogDismiss = { isDeleteBodyPartDialogOpen = false },
        onConfirmButtonClick = {
            isDeleteBodyPartDialogOpen = false
            onEvent(DetailsEvent.DeleteBodyPart)
        }
    )

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = PastOrPresentSelectableDates
    )
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    HealthMetricDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButtonClicked = {
            isDatePickerDialogOpen = false
            onEvent(DetailsEvent.OnDateChange(millis = datePickerState.selectedDateMillis))
        }
    )

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    DetailsTopBar(
                        bodyPart = state.bodyPart,
                        onBackIconClick = onBackIconClick,
                        onDeleteIconClick = { isDeleteBodyPartDialogOpen = true },
                        onUnitIconClick = { isMeasuringUnitBottomSheetOpen = true }
                    )
                    ChartTimeRangeButtons(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        selectedTimeRange = state.timeRange,
                        onClick = { onEvent(DetailsEvent.OnTimeRangeChange(it)) }
                    )
                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 2 / 1f)
                            .padding(16.dp),
                        bodyPartValues = state.graphBodyPartValues
                    )
                    HistorySection(
                        bodyPartValues = state.allBodyPartValues,
                        measuringUnitCode = state.bodyPart?.measuringUnit,
                        onDeleteIconClick = { onEvent(DetailsEvent.DeleteBodyPartValue(it)) }
                    )
                }
                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    isInputValueCardVisible = isInputValueCardVisible,
                    date = state.date.changeLocalDateToDateString(),
                    value = state.textFieldValue,
                    onValueChange = { onEvent(DetailsEvent.OnTextFieldValueChange(it)) },
                    onDoneImeActionClick = { focusManager.clearFocus() },
                    onCalendarIconClick = { isDatePickerDialogOpen = true },
                    onDoneIconClick = {
                        focusManager.clearFocus()
                        onEvent(DetailsEvent.AddNewValue)
                    }
                )
                InputCardHideIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp),
                    isInputValueCardVisible = isInputValueCardVisible,
                    onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                )
            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                DetailsTopBar(
                    bodyPart = state.bodyPart,
                    onBackIconClick = onBackIconClick,
                    onDeleteIconClick = { isDeleteBodyPartDialogOpen = true },
                    onUnitIconClick = { isMeasuringUnitBottomSheetOpen = true }
                )
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ChartTimeRangeButtons(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            selectedTimeRange = state.timeRange,
                            onClick = { onEvent(DetailsEvent.OnTimeRangeChange(it)) }
                        )
                        LineGraph(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 2 / 1f)
                                .padding(16.dp),
                            bodyPartValues = state.graphBodyPartValues
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        HistorySection(
                            bodyPartValues = state.allBodyPartValues,
                            measuringUnitCode = state.bodyPart?.measuringUnit,
                            onDeleteIconClick = { onEvent(DetailsEvent.DeleteBodyPartValue(it)) }
                        )
                        NewValueInputBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            isInputValueCardVisible = isInputValueCardVisible,
                            date = state.date.changeLocalDateToDateString(),
                            value = state.textFieldValue,
                            onValueChange = { onEvent(DetailsEvent.OnTextFieldValueChange(it)) },
                            onDoneImeActionClick = { focusManager.clearFocus() },
                            onCalendarIconClick = { isDatePickerDialogOpen = true },
                            onDoneIconClick = {
                                focusManager.clearFocus()
                                onEvent(DetailsEvent.AddNewValue)
                            }
                        )
                        InputCardHideIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp),
                            isInputValueCardVisible = isInputValueCardVisible,
                            onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart?,
    onBackIconClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onUnitIconClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Text(
                text = bodyPart?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { onDeleteIconClick() }) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = bodyPart?.measuringUnit ?: "")
            IconButton(onClick = { onUnitIconClick() }) {
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = "Select Unit")
            }
        }
    )
}

@Composable
private fun ChartTimeRangeButtons(
    modifier: Modifier = Modifier,
    selectedTimeRange: TimeRange,
    onClick: (TimeRange) -> Unit
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        TimeRange.entries.forEach { timeRange ->
            TimeRangeSelectionButton(
                modifier = Modifier.weight(1f),
                label = timeRange.label,
                labelTextStyle = if (timeRange == selectedTimeRange) {
                    MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                } else MaterialTheme.typography.labelLarge.copy(color = Color.Gray),
                backgroundColor = if (timeRange == selectedTimeRange) {
                    MaterialTheme.colorScheme.surface
                } else Color.Transparent,
                onClick = { onClick(timeRange) }
            )
        }
    }
}

@Composable
private fun TimeRangeSelectionButton(
    modifier: Modifier = Modifier,
    label: String,
    labelTextStyle: TextStyle,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, style = labelTextStyle, maxLines = 1)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistorySection(
    modifier: Modifier = Modifier,
    bodyPartValues: List<BodyPartValue>,
    measuringUnitCode: String?,
    onDeleteIconClick: (BodyPartValue) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        val grouped = bodyPartValues.groupBy { it.date.month }
        item {
            Text(text = "History", textDecoration = TextDecoration.Underline)
            Spacer(modifier = Modifier.height(20.dp))
        }
        grouped.forEach { (month, bodyPartValues) ->
            stickyHeader {
                Text(
                    text = month.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            items(bodyPartValues) { bodyPartValue ->
                HistoryCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    bodyPartValue = bodyPartValue,
                    measuringUnitCode = measuringUnitCode,
                    onDeleteIconClick = { onDeleteIconClick(bodyPartValue) }
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartValue: BodyPartValue,
    measuringUnitCode: String?,
    onDeleteIconClick: () -> Unit
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 5.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
            Text(
                text = bodyPartValue.date.changeLocalDateToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${bodyPartValue.value.roundToDecimal(4)} ${measuringUnitCode ?: ""}",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = { onDeleteIconClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
fun InputCardHideIcon(
    modifier: Modifier = Modifier,
    isInputValueCardVisible: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = if (isInputValueCardVisible) Icons.Default.KeyboardArrowDown
            else Icons.Default.KeyboardArrowUp,
            contentDescription = "Close or Open Input Card"
        )
    }
}

@PreviewScreenSizes
@Composable
private fun DetailsScreenPrev() {
    HealthMetricTheme {
        DetailsScreen(
            windowSize = WindowWidthSizeClass.Expanded,
            onBackIconClick = {},
            state = DetailsState(),
            onEvent = {},
            uiEvent = flow {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}