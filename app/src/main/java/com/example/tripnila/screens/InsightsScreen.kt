package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.AppDropDownFilterWithCallback
import com.example.tripnila.common.Orange
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    hostId: String,
    onBack: () -> Unit,
) {

    Log.d("INSIGHTS HOST ID:", hostId)

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    var insightsSelectedCategory by remember { mutableStateOf("Monthly") }
    var staycationStatsSelectedCategory by remember { mutableStateOf("Monthly") }
    var tourStatsSelectedCategory by remember { mutableStateOf("Daily") }

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
            ) {
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontalPaddingValue, verticalPaddingValue)
                    ) {
                        AppDropDownFilterWithCallback(
                            options = listOf("Daily", "Weekly", "Monthly", "Yearly"),
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
                            InsightInfoCard(
                                modifier = Modifier.weight(.7f),
                                cardLabel = "Revenue",
                                cardInfoCount = 12000
                            )
                            InsightInfoCard(
                                modifier = Modifier.weight(.7f),
                                cardLabel = "Bookings",
                                cardInfoCount = 12
                            )
                            InsightInfoCard(
                                modifier = Modifier.weight(.7f),
                                cardLabel = "Views",
                                cardInfoCount = 12
                            )
                        }
                    }
                }
                item {
                    SalesChart(
                        year = 2023,
                        modifier = Modifier.padding(horizontalPaddingValue,verticalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Stats",
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
                            Text(
                                text = "Staycation",
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
                                selectedCategory = staycationStatsSelectedCategory,
                                onCategorySelected = { newCategory ->
                                    staycationStatsSelectedCategory = newCategory },
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
                            selectedCategory = staycationStatsSelectedCategory
                        )
                    }
                }
                // TOUR
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
fun SalesChart(modifier: Modifier = Modifier, year: Int) {

    val chartEntryModel = entryModelOf(entriesOf(4f, 12f, 8f, 16f), entriesOf(12f, 16f, 4f, 12f))


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
                text = "Sales for Year $year",
                color = Color(0xfff9a664),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            AppDropDownFilter(
                options = listOf("Sales", "...", "..."),
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
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
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
                // CANCELLATION RATE
                Column {
                    Text(
                        text = "${(cancellationRate * 100).toInt()}%",
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
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                color = Color(0xffdedede)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // EARNINGS
                Column(
                    modifier = Modifier.weight(.3f)
                ) {
                    Text(
                        text = "₱ ${formattedNumber.format(earnings)}",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$categoryText earnings",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // VIEWS
                Column(
                    modifier = Modifier.weight(.3f)
                ) {
                    Text(
                        text = formattedNumber.format(views),
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$categoryText views",
                        color = Color(0xff333333),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // BOOKINGS
                Column(
                    modifier = Modifier.weight(.3f)
                ) {
                    Text(
                        text = formattedNumber.format(bookings),
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$categoryText bookings",
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