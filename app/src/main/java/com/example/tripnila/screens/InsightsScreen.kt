package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppDropDownFilterWithCallback
import com.example.tripnila.common.AppFilledButton
import com.example.tripnila.common.AppOutlinedButton1
import com.example.tripnila.common.ChartDropDownFilter
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.Tour
import com.example.tripnila.model.InsightViewModel
import com.example.tripnila.model.SalesReportViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    hostId: String,
    onBack: () -> Unit,
    insightViewModel : InsightViewModel,
    salesReportViewModel: SalesReportViewModel,
    onNavToGeneratedReport: (String) -> Unit
) {

    Log.d("INSIGHTS HOST ID:", hostId)
    insightViewModel.getHostedStaycation(hostId)
    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp
    val hostedStaycations = insightViewModel.getDocumentIds().toSet().toMutableList()
    Log.d("staycation", hostedStaycations.toString())
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())
    val selectedStaycation = insightViewModel.selectedStaycation.collectAsState().value
    val selectedTour = insightViewModel.selectedTour.collectAsState().value
    val completedBooking = insightViewModel.completedStaycationBookings.collectAsState().value
    val completedTourBooking = insightViewModel.completedTourBookings.collectAsState().value
    val completedMonthlyTourBooking = insightViewModel.completedMonthlyTourBookings.collectAsState().value
    val completedMonthlyBooking = insightViewModel.completedMonthlyStaycationBookings.collectAsState().value
    val totalRevenue = insightViewModel.revenue.collectAsState().value
    val totalMonthlyRevenue = insightViewModel.monthlyRevenue.collectAsState().value
    val monthlyViewCount = insightViewModel.monthlyViews.collectAsState().value
    val yearlyViewCount = insightViewModel.yearlyViews.collectAsState().value
    val cancelRate = insightViewModel.cancelledBooks.collectAsState().value
    val aveRating = insightViewModel.aveRating.collectAsState().value
    val reviewCount = insightViewModel.reviewCount.collectAsState().value
    var isDialogOpen by remember { mutableStateOf(false) }

    val isFetchingStaycationBookings by salesReportViewModel.isFetchingStaycationBookings.collectAsState()
    val isStaycationBookingsFetched by salesReportViewModel.isStaycationBookingsFetched.collectAsState()
    val isFetchingTourBookings by salesReportViewModel.isFetchingTourBookings.collectAsState()
    val isTourBookingsFetched by salesReportViewModel.isTourBookingsFetched.collectAsState()

    val selectedPeriod by salesReportViewModel.selectedPeriod.collectAsState()
    val selectedMonth by salesReportViewModel.selectedMonth.collectAsState()
    val selectedYear by salesReportViewModel.selectedYear.collectAsState()
    val selectedStartMonth by salesReportViewModel.selectedStartMonth.collectAsState()
    val selectedEndMonth by salesReportViewModel.selectedEndMonth.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var expandMonth by remember { mutableStateOf(false) }
    val monthIcon = if (expandMonth)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandMonthRange by remember { mutableStateOf(false) }
    //  var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val monthRangeIcon = if (expandMonthRange)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var expandYear by remember { mutableStateOf(false) }
    //  var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val yearIcon = if (expandYear)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val selectionTab = listOf(
        "Monthly", "Bi-yearly", "Yearly"
    )
    val monthOptions = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    val biyearlyOptions = listOf(
        "January - June", "July - December"
    )

    val yearOptions = (2023..LocalDate.now().year).toList().reversed()
    var showDialog by remember {
        mutableStateOf(false)
    }
    var generateExcelClicked by remember { mutableStateOf(false) }
    var isTourSelected = insightViewModel.isTourSelected.collectAsState().value
    var insightsSelectedCategory by remember { mutableStateOf("Monthly") }
    var staycationStatsSelectedCategory by remember { mutableStateOf("Monthly") }
    var tourStatsSelectedCategory by remember { mutableStateOf("Daily") }
    val backgroundColor = Color.White
    LaunchedEffect(hostId) {
        if (hostId != "") {
            insightViewModel.getStaycation(hostId)
            insightViewModel.getTours(hostId)
            Log.d("staycation-1", insightViewModel.getStaycation(hostId).toString())
        }

    }
    LaunchedEffect(generateExcelClicked) {
        if (generateExcelClicked) {
            salesReportViewModel.generateExcelFile(context)
            generateExcelClicked = false
        }
    }
    LaunchedEffect(
        isStaycationBookingsFetched,
        isTourBookingsFetched
    ) {
        if (isStaycationBookingsFetched && isTourBookingsFetched) {
            onNavToGeneratedReport("salesReport")
            showDialog = false
            salesReportViewModel.resetFetchStatus()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(headerText = "Insights")
            },
            bottomBar = {
                InsightsBottomBookingBar(
                    onBack = {
                        onBack()
                    }
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {item {

                if (selectedStaycation.staycationId == "" && selectedTour.tourId == "") {
                    ChooseStaycationTourButton(
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                            .clickable { isDialogOpen = true }
                    )
                } else if(isTourSelected){
                    SelectedTourInsightCard(
                        tour = selectedTour,
                        onChange = {
                            //   itineraryViewModel.clearSelectedStaycation()
                            isDialogOpen = true
                        }
                    )
                }else if(!isTourSelected){
                    SelectedStaycationInsightCard(
                        staycation = selectedStaycation,
                        onChange = {
                            //   itineraryViewModel.clearSelectedTour()
                            isDialogOpen = true
                        }
                    )
                }

            }

                item {
                    val monthlyBookingCount = remember { mutableStateOf(0) }
                    val yearlyBookingCount = remember { mutableStateOf(0) }
                    LaunchedEffect(selectedStaycation){
                        val selectedStaycationId = selectedStaycation.staycationId
                        if (selectedStaycationId != null) {
                            Log.d("Selected Staycation ID", selectedStaycationId)
                            insightViewModel.getCompletedStaycationBookings(selectedStaycationId)
                            insightViewModel.getCancellationRate(selectedStaycationId)
                            insightViewModel.getReviewRatings(selectedStaycationId)
                            insightViewModel.getCompletedStaycationBookingsForMonth(selectedStaycationId)
                            insightViewModel.fetchMonthlyViews(selectedStaycationId, "Staycation")
                            insightViewModel.fetchAllViews(selectedStaycationId)
                        } else {
                            Log.e("Selected Staycation ID", "Staycation ID is null or empty")
                        }

                    }
                    LaunchedEffect(selectedTour){
                        val selectedTourId = selectedTour.tourId
                        if (selectedTourId != null) {
                            Log.d("Selected Tour ID", selectedTourId)
                            insightViewModel.getCompletedTourBookings(selectedTourId)
                            insightViewModel.getCancellationRate(selectedTourId)
                            insightViewModel.getReviewRatings(selectedTourId)
                            insightViewModel.getCompletedTourBookingsForMonth(selectedTourId)
                            insightViewModel.fetchMonthlyViews(selectedTourId, "Tour")
                            insightViewModel.fetchAllViews(selectedTourId)
                        } else {
                            Log.e("Selected Tour ID", "Tour ID is null or empty")
                        }

                    }

                    LaunchedEffect(completedBooking){
                        insightViewModel.getMonthlyRevenue(completedBooking)
                        insightViewModel.getYearlyRevenue(completedBooking)
                        val numberOfMonthlyBookings = completedMonthlyBooking.size
                        val numberOfYearlyBookings = completedBooking.size
                        monthlyBookingCount.value = numberOfMonthlyBookings
                        yearlyBookingCount.value = numberOfYearlyBookings
                    }
                    LaunchedEffect(completedTourBooking){
                        insightViewModel.getTourMonthlyRevenue(completedTourBooking)
                        insightViewModel.getTourYearlyRevenue(completedTourBooking)
                        val numberOfMonthlyBookings = completedMonthlyTourBooking.size
                        val numberOfYearlyBookings = completedTourBooking.size
                        monthlyBookingCount.value = numberOfMonthlyBookings
                        yearlyBookingCount.value = numberOfYearlyBookings
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        AppDropDownFilterWithCallback(
                            options = listOf( "Monthly", "Yearly"),
                            fontSize = 10.sp,
                            selectedCategory = insightsSelectedCategory,
                            onCategorySelected = { newCategory ->
                                insightsSelectedCategory = newCategory
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 3.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            if (insightsSelectedCategory =="Monthly") {

                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Revenue",
                                    cardInfoCount = totalMonthlyRevenue
                                )
                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Bookings",
                                    cardInfoCount = monthlyBookingCount.value
                                )
                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Views",
                                    cardInfoCount = monthlyViewCount
                                )
                            } else{
                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Revenue",
                                    cardInfoCount = totalRevenue
                                )
                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Bookings",
                                    cardInfoCount = yearlyBookingCount.value
                                )
                                InsightInfoCard(
                                    modifier = Modifier.weight(.7f),
                                    cardLabel = "Views",
                                    cardInfoCount = yearlyViewCount
                                )
                            }
                        }
                    }
                }
                item {
                    if(isTourSelected){
                        TourSalesChart(
                            modifier = Modifier.padding(horizontalPaddingValue,verticalPaddingValue),
                            tourId = selectedTour.tourId,
                            insightViewModel = InsightViewModel()
                        )
                    }
                    else{
                        SalesChart(
                            modifier = Modifier.padding(horizontalPaddingValue,verticalPaddingValue),
                            staycationId = selectedStaycation.staycationId,
                            insightViewModel = InsightViewModel()
                        )
                }

                }
                item {
                    Text(
                        text = "Statistics",
                        color = Color(0xff333333),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(
                            start = horizontalPaddingValue,
                            end = horizontalPaddingValue,
                            top = verticalPaddingValue
                        )
                    )
                }
                // STAYCATION
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                        }
                        ServiceStatsCard(
                            modifier = Modifier,
                            overallRating = aveRating,
                            totalReviewsCount = reviewCount,
                            responseRate = .98,
                            cancellationRate = cancelRate,
                            earnings = 4250,
                            views = 20,
                            bookings = 1,
                            selectedCategory = staycationStatsSelectedCategory
                        )

                    }
                }
                item {
                    Text(
                        text = "Reports",
                        color = Color(0xff333333),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(
                            start = horizontalPaddingValue,
                            end = horizontalPaddingValue,
                            top = verticalPaddingValue
                        )
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        AppFilledCard(
                            cardText = "Generate Sales Report",
                            onClick = {
                                showDialog = true
                            }
                        )
                    }
                }
                /*// TOUR
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Tour",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .weight(1f)
                            )
                            AppDropDownFilterWithCallback(
                                options = listOf("Daily", "Weekly", "Monthly", "Yearly", "All"),
                                fontSize = 10.sp,
                                selectedCategory = tourStatsSelectedCategory,
                                onCategorySelected = { newCategory ->
                                    tourStatsSelectedCategory = newCategory },
                                modifier = Modifier
                                    .padding(bottom = 3.dp)
                            )

                        }
                        ServiceStatsCard(
                            modifier = Modifier,
                            overallRating = 4.7,
                            totalReviewsCount = 270,
                            responseRate = .98,
                            cancellationRate = .1,
                            earnings = 4250,
                            views = 20,
                            bookings = 1,
                            selectedCategory = tourStatsSelectedCategory
                        )
                    }
                }
                // BUSINESS
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Business",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .weight(1f)
                                    .padding(bottom = 3.dp)
                            )
                        }
                        BusinessStatsCard(
                            overallRating = 4.7,
                            totalReviewsCount = 270,
                            responseRate = .98
                        )
                    }

                }*/

            }


        }
        if (showDialog) { //showOpeningDialog &&
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size = 12.dp)
                    ),
                onDismissRequest = { showDialog = false }
            ) {
                Column(
                    modifier = Modifier
//                            .background(
//                                color = Color.LightGray.copy(alpha = 0.3f)
//                            )
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Generate Sales Reports",
                        color = Color(0xff333333),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Select time period",
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            selectionTab.forEach { tabLabel ->
                                SelectionTab(
                                    tabLabel = tabLabel,
                                    isSelected = tabLabel == selectedPeriod,
                                    onTabSelected = {
                                        salesReportViewModel.setSelectedPeriod(tabLabel)
                                    },
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (selectedPeriod == "Monthly") {
                                Column {
                                    Text(
                                        text = "Select Month",
                                        color = Color(0xff333333),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 12.dp
                                        )
                                    ) {
                                        Text(
                                            text = selectedMonth,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                        IconButton(
                                            modifier = Modifier.size(24.dp),
                                            onClick = { expandMonth = true }
                                        ) {
                                            Icon(
                                                imageVector = monthIcon,
                                                contentDescription = "",
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expandMonth,
                                            onDismissRequest = { expandMonth = false },
                                            modifier = Modifier
                                                .background(Color.White)
                                        ) {
                                            monthOptions.forEach { label ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = label,
                                                            fontSize = 12.sp
                                                        )
                                                    },
                                                    colors = MenuDefaults.itemColors(
                                                        textColor = Color(0xFF6B6B6B)
                                                    ),
                                                    onClick = {
                                                        salesReportViewModel.setSelectedMonth(label)
                                                        expandMonth = false
                                                    },
                                                    contentPadding = PaddingValues(
                                                        horizontal = 10.dp,
                                                        vertical = 0.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            } else if (selectedPeriod == "Bi-yearly") {
                                Column {
                                    Text(
                                        text = "Select Month Range",
                                        color = Color(0xff333333),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 12.dp
                                        )
                                    ) {
                                        Text(
                                            text = "$selectedStartMonth - $selectedEndMonth",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                        IconButton(
                                            modifier = Modifier.size(24.dp),
                                            onClick = { expandMonthRange = true }
                                        ) {
                                            Icon(
                                                imageVector = monthRangeIcon,
                                                contentDescription = "",
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = expandMonthRange,
                                            onDismissRequest = { expandMonthRange = false },
                                            modifier = Modifier
                                                .background(Color.White)
                                        ) {
                                            biyearlyOptions.forEachIndexed { index, label ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = label,
                                                            fontSize = 12.sp
                                                        )
                                                    },
                                                    colors = MenuDefaults.itemColors(
                                                        textColor = Color(0xFF6B6B6B)
                                                    ),
                                                    onClick = {
                                                        salesReportViewModel.setSelectedMonthRange(index)
                                                        expandMonthRange = false
                                                    },
                                                    contentPadding = PaddingValues(
                                                        horizontal = 10.dp,
                                                        vertical = 0.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Column {
                                Text(
                                    text = "Select Year",
                                    color = Color(0xff333333),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(
                                        vertical = 8.dp,
                                        horizontal = 12.dp
                                    )
                                ) {
                                    Text(
                                        text = selectedYear.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    IconButton(
                                        modifier = Modifier.size(24.dp),
                                        onClick = { expandYear = true }
                                    ) {
                                        Icon(
                                            imageVector = yearIcon,
                                            contentDescription = "",
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expandYear,
                                        onDismissRequest = { expandYear = false },
                                        modifier = Modifier
                                            .background(Color.White)
                                    ) {
                                        yearOptions.forEach { label ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = label.toString(),
                                                        fontSize = 12.sp
                                                    )
                                                },
                                                colors = MenuDefaults.itemColors(
                                                    textColor = Color(0xFF6B6B6B)
                                                ),
                                                onClick = {
                                                    salesReportViewModel.setSelectedYear(label.toString())
                                                    expandYear = false
                                                },
                                                contentPadding = PaddingValues(
                                                    horizontal = 10.dp,
                                                    vertical = 0.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AppOutlinedButton1(
                            buttonText = "Cancel",
                            onClick = { showDialog = false }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AppFilledButton(
                            buttonText = "Confirm",
                            isLoading = isFetchingStaycationBookings || isFetchingTourBookings,
                            onClick = {
                                scope.launch {
                                    val staycationBookingsDeferred = async { salesReportViewModel.fetchStaycationBookings() }
                                    val tourBookingsDeferred = async { salesReportViewModel.fetchTourBookings() }

                                    staycationBookingsDeferred.await()
                                    tourBookingsDeferred.await()

                                }
                            },
                        )
                    }

                }
            }
        }
    }
    ChooseStaycationInsightDialog(
        insightViewModel = insightViewModel,
        backgroundColor = backgroundColor,
        isDialogOpen = isDialogOpen,
        onDismissRequest = {
            isDialogOpen = false
        },
        onConfirm = {
            isDialogOpen = false
            //   onNavToPopulatedItinerary(touristId)
        }
    )
}
@Composable
fun ChooseStaycationInsightDialog(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    isDialogOpen: Boolean,
    insightViewModel: InsightViewModel,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {

    // var selectedCard by remember { mutableStateOf(-1) }

    var selectedCard by remember { mutableStateOf("") }
    var selectTour = insightViewModel.isTourSelected.collectAsState().value
    val staycations = insightViewModel.staycations.collectAsState().value
    val tours = insightViewModel.tours.collectAsState().value

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                modifier = modifier
                    //.height(500.dp)
                    .width(300.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp, // 20
                            vertical = 15.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Staycation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                    )
                    staycations.forEach { booking ->

                        val staycationId = booking.staycationId

                        ChooseStaycationInsightCard(
                            staycationName = booking.staycationTitle ?: "Greenhills Staycation",
                            staycationId = staycationId,
                            isSelected = selectedCard == booking.staycationId,
                            onSelectedChange = {
                                selectedCard = it
                                insightViewModel.setIsTourSelected(false)
                            },
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }

                    Text(
                        text = "Choose Tours",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp, top = 10.dp)
                    )
                    tours.forEach { booking ->

                        val staycationId = booking.tourId

                        ChooseStaycationInsightCard(
                            staycationName = booking.tourTitle ?: "Greenhills Staycation",
                            staycationId = staycationId,
                            isSelected = selectedCard == booking.tourId,
                            onSelectedChange = {
                                selectedCard = it
                                insightViewModel.setIsTourSelected(true)

                            },
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        BookingOutlinedButton(
                            buttonText = "Cancel",
                            onClick = {
                                selectedCard = ""
                                onDismissRequest()
                            }
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        BookingFilledButton(
                            buttonText = "Confirm",
                            onClick = {
                                if (selectedCard.isNotBlank() && selectTour) {
                                    insightViewModel.setSelectedTour(selectedCard)
                                    insightViewModel.deleteSelectedStaycation()
                                    onConfirm()
                                }
                                else if(selectedCard.isNotBlank() && !selectTour){
                                    insightViewModel.setSelectedStaycation(selectedCard)
                                    insightViewModel.deleteSelectedTour()
                                    insightViewModel.setIsTourSelected(false)
                                    onConfirm()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun InsightsBottomBookingBar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightButtonText: String = "Back",
    enableRightButton: Boolean = true,
){
    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                enabled = enableRightButton,
                buttonText = rightButtonText,
                onClick = {
                    onBack?.invoke()
                },
                modifier = Modifier.width(120.dp)
            )
        }
    }
}


@Composable
fun InsightInfoCard(
    modifier: Modifier = Modifier,
    cardLabel: String,
    cardInfoCount: Int
) {

    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)
        .format(cardInfoCount)

    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Orange),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 5.dp)
        ) {
            Text(
                text = cardLabel,
                color = Orange,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (cardLabel == "Revenue") "₱ $formattedNumber" else formattedNumber.toString(),
                color = Color(0xff333333),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SelectionTab(
    tabLabel: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onTabSelected,
        border = BorderStroke(width = 1.dp, Color(0xFFF9A664)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFF9A664) else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier.height(22.dp)
    ) {
        Text(
            text = tabLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFFF9A664),
        )
    }
}

@Composable
fun SalesChart(
    modifier: Modifier = Modifier,
    insightViewModel : InsightViewModel,
    staycationId: String
) {
    var year by remember { mutableStateOf(insightViewModel.selectedYear.value) }

    // Observe changes in aggregated sales data based on the selected year
    val aggregatedData = insightViewModel.aggregatedSalesData.collectAsState()

    // Fetch sales data whenever the year changes
    LaunchedEffect(key1 = year) {
        insightViewModel.getSales(year,staycationId)
    }
    val monthNames = mapOf(
        2 to "Jan",
        3 to "Feb",
        4 to "Mar",
        5 to "Apr",
        6 to "May",
        7 to "Jun",
        8 to "Jul",
        9 to "Aug",
        10 to "Sep",
        11 to "Oct",
        12 to "Nov",
        13 to "Dec"
    )

    // Prepare chart data
    val chartEntries = remember(aggregatedData.value) {
        aggregatedData.value.map { entry ->
            Pair(entry.month.toFloat(), entry.totalAmount.toFloat())
        }
    }

    // Pass the entries to the chart model
    val chartEntryModel = entryModelOf(*chartEntries.toTypedArray())
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val monthNumber = value.toInt()+1 // Assuming month numbers start from 1
        monthNames[monthNumber] ?: ""
    }

    //val chartEntries = aggregatedData.map { entry -> Entry(entry.month.toFloat(), entry.totalAmount.toFloat()) }
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp, 5.dp)
        ) {
            Text(
                text = "Sales for Year ${insightViewModel.selectedYear.value}",
                color = Color(0xfff9a664),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            ChartDropDownFilter(
                options = listOf("2024", "2023"),
                onItemSelected = { year ->
                    insightViewModel.setSelectedYear(year.toInt(),staycationId) // Update the selected year in ViewModel
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )

        }
        ProvideChartStyle(
            chartStyle = m3ChartStyle(
                axisLabelColor = Color.Black,
                axisGuidelineColor = Color(0xff999999),
                axisLineColor = Color.Transparent,
                entityColors = listOf(
                    Color(0xfff9a664),
                    Color(0xff9FFFB4)
                ),
                // elevationOverlayColor = Color.Transparent
            )
        ) {
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    title = "Top Values",
                    tickLength = 0.dp,
                    valueFormatter = { value, _ -> value.toInt().toString()

                    }, itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 6
                    )
                ),
                bottomAxis = rememberBottomAxis(
                    title = "Count of Values",
                    tickLength = 0.dp,
                    valueFormatter = horizontalAxisValueFormatter
                ),
                // legend = horizontalLegend(items = , iconSize = , iconPadding = ),
                modifier = Modifier.padding(start = 5.dp, end = 20.dp, bottom = 5.dp),
            )
        }
    }
}


@Composable
fun TourSalesChart(
    modifier: Modifier = Modifier,
    insightViewModel : InsightViewModel,
    tourId: String
) {
    var year by remember { mutableStateOf(insightViewModel.selectedYear.value) }

    // Observe changes in aggregated sales data based on the selected year
    val aggregatedData = insightViewModel.aggregatedSalesData.collectAsState()

    // Fetch sales data whenever the year changes
    LaunchedEffect(key1 = year) {
        insightViewModel.getTourSales(year,tourId)
    }
    val monthNames = mapOf(
        2 to "Jan",
        3 to "Feb",
        4 to "Mar",
        5 to "Apr",
        6 to "May",
        7 to "Jun",
        8 to "Jul",
        9 to "Aug",
        10 to "Sep",
        11 to "Oct",
        12 to "Nov",
        13 to "Dec"
    )

    // Prepare chart data
    val chartEntries = remember(aggregatedData.value) {
        aggregatedData.value.map { entry ->
            Pair(entry.month.toFloat(), entry.totalAmount.toFloat())
        }
    }

    // Pass the entries to the chart model
    val chartEntryModel = entryModelOf(*chartEntries.toTypedArray())
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        val monthNumber = value.toInt()+1 // Assuming month numbers start from 1
        monthNames[monthNumber] ?: ""
    }

    //val chartEntries = aggregatedData.map { entry -> Entry(entry.month.toFloat(), entry.totalAmount.toFloat()) }
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp, 5.dp)
        ) {
            Text(
                text = "Sales for Year ${insightViewModel.selectedYear.value}",
                color = Color(0xfff9a664),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            ChartDropDownFilter(
                options = listOf("2024", "2023"),
                onItemSelected = { year ->
                    insightViewModel.setTourSelectedYear(year.toInt(),tourId) // Update the selected year in ViewModel
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )

        }
        ProvideChartStyle(
            chartStyle = m3ChartStyle(
                axisLabelColor = Color.Black,
                axisGuidelineColor = Color(0xff999999),
                axisLineColor = Color.Transparent,
                entityColors = listOf(
                    Color(0xfff9a664),
                    Color(0xff9FFFB4)
                ),
                // elevationOverlayColor = Color.Transparent
            )
        ) {
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    title = "Top Values",
                    tickLength = 0.dp,
                    valueFormatter = { value, _ -> value.toInt().toString()

                    }, itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 6
                    )
                ),
                bottomAxis = rememberBottomAxis(
                    title = "Count of Values",
                    tickLength = 0.dp,
                    valueFormatter = horizontalAxisValueFormatter
                ),
                // legend = horizontalLegend(items = , iconSize = , iconPadding = ),
                modifier = Modifier.padding(start = 5.dp, end = 20.dp, bottom = 5.dp),
            )
        }
    }
}

@Composable
fun ServiceStatsCard(
    modifier: Modifier = Modifier,
    overallRating: Double,
    totalReviewsCount: Int,
    responseRate: Double,
    cancellationRate: Double,
    earnings: Int,
    views: Int,
    selectedCategory: String,
    bookings: Int
) {

    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)

    val categoryText = when(selectedCategory) {
        "Daily" -> "Today's"
        "Weekly" -> "This Week's"
        "Monthly" -> "This Month's"
        "Yearly" -> "This Year's"
        "All" -> "Lifetime"
        else -> "NONE OF THE CHOICES"
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // OVERALL RATING
                Column {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.star),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = overallRating.toString(),
                            color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Overall rating",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // REVIEWS
                Column {
                    Text(
                        text = formattedNumber.format(totalReviewsCount),
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reviews",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // CANCELLATION RATE
                Column {
                    val cancellationRateFormatted = String.format("%.1f", cancellationRate)
                    val text = "$cancellationRateFormatted"
                    Text(
                        text = "$text%",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cancellation Rate",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

        }

    }

}

@Composable
fun BusinessStatsCard(
    modifier: Modifier = Modifier,
    overallRating: Double,
    totalReviewsCount: Int,
    responseRate: Double,
) {
    val formattedNumber = NumberFormat.getNumberInstance(Locale.US)

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // OVERALL RATING
                Column {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.star),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = overallRating.toString(),
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Overall rating",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // REVIEWS
                Column {
                    Text(
                        text = formattedNumber.format(totalReviewsCount),
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reviews",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // RESPONSE RATE
                Column {
                    Text(
                        text = "${(responseRate * 100).toInt()}%",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Response Rate",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStaycationInsightCard(

    staycationName: String,
    staycationId: String,
    isSelected: Boolean,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier,
){

    // val staycationBookingId = bookingId

    Card(
        onClick = {
            onSelectedChange(staycationId)
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20),
        border = if(isSelected) BorderStroke(2.dp, Color(0xfff9a664)) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            // pressedElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Row {
                Text(
                    text = staycationName,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@Composable
fun SelectedStaycationInsightCard(

    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    onChange: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
        //.requiredHeight(height = 93.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Row {
                        Text(
                            text = staycation?.staycationTitle ?: "Modern house with 2 bedrooms",
                            color = Color(0xff333333),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .width(147.dp)
                        )
                        AppOutlinedButtonWithBadge(
                            buttonLabel = "Change",
                            onClick = {
                                onChange()
                            }
                        )


                    }
                    Text(
                        text = "by ${staycation?.host?.firstName ?: "Joshua"}",
                        color = Color(0xfff9a664),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location),
                            contentDescription = "Location",
                            tint = Color(0xff999999),
                            modifier = Modifier
                                .padding(top = 3.dp, end = 5.dp)
                        )
                        Text(
                            text = staycation?.staycationLocation ?: "North Greenhills",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xff999999),
                            modifier = Modifier
                                .width(207.dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .width(width = 90.dp)
                        .height(height = 66.dp)
                ) {
                    AsyncImage(
                        model = staycation?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl
                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        contentDescription = "Staycation"
                    )
                }

            }
        }

    }

}

@Composable
fun SelectedTourInsightCard(

    modifier: Modifier = Modifier,
    tour: Tour? = null,
    onChange: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
        //.requiredHeight(height = 93.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Row {
                        Text(
                            text = tour?.tourTitle ?: "Modern house with 2 bedrooms",
                            color = Color(0xff333333),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .width(147.dp)
                        )
                        AppOutlinedButtonWithBadge(
                            buttonLabel = "Change",
                            onClick = {
                                onChange()
                            }
                        )


                    }
                    Text(
                        text = "by ${tour?.host?.firstName ?: "Joshua"}",
                        color = Color(0xfff9a664),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location),
                            contentDescription = "Location",
                            tint = Color(0xff999999),
                            modifier = Modifier
                                .padding(top = 3.dp, end = 5.dp)
                        )
                        Text(
                            text = tour?.tourLocation ?: "North Greenhills",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xff999999),
                            modifier = Modifier
                                .width(207.dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .width(width = 90.dp)
                        .height(height = 66.dp)
                ) {
                    AsyncImage(
                        model = tour?.tourImages?.find { it.photoType == "Cover" }?.photoUrl
                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        contentDescription = "Staycation"
                    )
                }

            }
        }

    }

}

@Preview
@Composable
private fun InsightsScreenPreview() {
//    ServiceStatsCard(
//        modifier = Modifier,
//        overallRating = 4.7,
//        totalReviewsCount = 270,
//        responseRate = .98,
//        cancellationRate = .1,
//        earnings = 4250,
//        views = 20,
//        bookings = 1,
//        selectedCategory = "Monthly",
//
//    )

    //InsightsScreen()
}