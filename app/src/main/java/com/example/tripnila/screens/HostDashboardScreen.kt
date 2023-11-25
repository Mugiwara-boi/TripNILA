package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.data.HostProperty
import com.example.tripnila.data.Transaction
import com.example.tripnila.model.HostDashboardViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@Composable
fun HostDashboardScreen(
    touristId: String,
    hostDashboardViewModel: HostDashboardViewModel? = null,
    onNavToAddListing: (String, String) -> Unit,
    onNavToHostTour: (String, String) -> Unit,
    onNavToAddBusiness: (String, String) -> Unit,
    onNavToStaycationManager: (String, String) -> Unit,
    onNavToBusinessManager: (String, String) -> Unit,
    onNavToTourManager: (String, String) -> Unit,
){

    Log.d("TouristId", "$touristId")

    LaunchedEffect(touristId) {
        hostDashboardViewModel?.getHostDetailsByTouristId(touristId)
    }

    val host = hostDashboardViewModel?.host?.collectAsState()?.value

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp


    val staycationProperties = hostDashboardViewModel?.staycations?.collectAsState()?.value?.map {staycation ->
        staycation.staycationImages.find { it.photoType == "Cover" }?.photoUrl?.let {
            HostProperty(
                propertyId = staycation.staycationId,
                propertyName = staycation.staycationTitle,
                image = it, // You may need to replace this with the actual logic to get the image
                host = staycation.host.firstName, // Assuming host is a Host object with a firstName property
                location = staycation.staycationLocation
            )
        }
    }

    val businessProperties = hostDashboardViewModel?.businesses?.collectAsState()?.value?.map { business ->
        business.businessImages.find { it.photoType == "Cover" }?.photoUrl?.let {
            HostProperty(
                propertyId = business.businessId,
                propertyName = business.businessTitle,
                propertyDescription = business.businessDescription,
                image = it, // You may need to replace this with the actual logic to get the image
                host = business.host.firstName, // Assuming host is a Host object with a firstName property
                location = business.businessLocation
            )
        }
    }

    val tourProperties = hostDashboardViewModel?.tours?.collectAsState()?.value?.map { tour ->
        tour.tourImages.find { it.photoType == "Cover" }?.photoUrl?.let {
            HostProperty(
                propertyId = tour.tourId,
                propertyName = tour.tourTitle,
                propertyDescription = tour.tourDescription,
                image = it, // You may need to replace this with the actual logic to get the image
                host = tour.host.firstName, // Assuming host is a Host object with a firstName property
                location = tour.tourLocation
            )
        }
    }


    var selectedItemIndex by rememberSaveable { mutableStateOf(3) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                AppBottomNavigationBar(
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { newIndex ->
                        selectedItemIndex = newIndex
                    }
                )
            }
        ) {

            if (hostDashboardViewModel?.isStateRetrieved?.collectAsState()?.value == false) {
                LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    item {
                        AppTopBar(
                            headerText = "Dashboard",
                            color = Color.White
                        )
                    }
                    item {
                        HostWalletCard(
                            hostName = "${host?.firstName} ${host?.lastName}" ,
                            hostBalance = 7600.00,
                            modifier = Modifier
                                .padding(
                                    vertical = verticalPaddingValue,
                                    horizontal = horizontalPaddingValue
                                )
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(vertical = verticalPaddingValue)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            HostOptionButton(
                                buttonIcon = R.drawable.add_staycation,
                                buttonLabel = "Add listing",
                                onClick = {
                                    host?.hostId?.let { hostId -> onNavToAddListing(hostId, "Staycation") }
                                }
                            )
                            HostOptionButton(
                                buttonIcon = R.drawable.host_tour,
                                buttonLabel = "Host a tour",
                                onClick = {
                                    host?.hostId?.let { hostId -> onNavToHostTour(hostId, "Tour") }
                                }
                            )
                            HostOptionButton(
                                buttonIcon = R.drawable.add_business,
                                buttonLabel = "Add business",
                                onClick = {
                                    host?.hostId?.let { hostId -> onNavToAddBusiness(hostId, "Business") }
                                }
                            )
                            HostOptionButton(
                                buttonIcon = R.drawable.insights,
                                buttonLabel = "Insights",
                                onClick = {}
                            )
                        }
                    }
                    item {
                        ReservationsList(
                            hostDashboardViewModel = hostDashboardViewModel,
                            modifier = Modifier
                                .padding(
                                    vertical = verticalPaddingValue,
                                    horizontal = horizontalPaddingValue
                                )
                        )
                    }
                    item {
                        HostPropertiesList(
                            properties = staycationProperties,
                            onNavToStaycationManager = { staycationId ->
                                onNavToStaycationManager(host?.hostId ?: "", staycationId)
                            },
                            modifier = Modifier
                                .padding(
                                    vertical = verticalPaddingValue,
                                    //horizontal = horizontalPaddingValue
                                )
                        )
                    }
                    item {
                        HostBusinessesList(
                            properties = businessProperties,
                            onNavToBusinessManager = { businessId ->
                                onNavToBusinessManager(host?.hostId ?: "", businessId)
                            },
                            modifier = Modifier
                                .padding(
                                    vertical = verticalPaddingValue,
                                    //horizontal = horizontalPaddingValue
                                )
                        )
                    }
                    item {
                        HostToursList(
                            properties = tourProperties,
                            onNavToTourManager = { tourId ->
                                onNavToTourManager(host?.hostId ?: "", tourId)
                            },
                            modifier = Modifier
                                .padding(
                                    vertical = verticalPaddingValue,
                                    //horizontal = horizontalPaddingValue
                                )
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun HostWalletCard(
    hostName: String,
    hostBalance: Double,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Orange
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Text(
                        text = hostName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Host",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(34.dp)
                        .offset(x = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Expand",
                        tint = Color.White,
                        modifier = Modifier
                            .size(34.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Available balance",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "₱ ${String.format("%.2f", hostBalance)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun HostOptionButton(
    buttonIcon: Int,
    buttonLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = {
                onClick()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 10.dp
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = modifier.size(60.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(buttonIcon),
                contentDescription = "",
                tint = Orange
            )
        }
        Text(
            text = buttonLabel,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun ReservationsList(
    hostDashboardViewModel: HostDashboardViewModel?,
    modifier: Modifier = Modifier
) {


    val staycations = hostDashboardViewModel?.staycations?.collectAsState()?.value

    val staycationBookings = staycations?.flatMap { it.staycationBookings }

    val checkingOutCount = staycations?.sumOf { it.checkingOutBookingsCount }

    val currentlyHostingCount = staycations?.sumOf { it.ongoingBookingsCount }

    val upcomingCount = staycations?.sumOf { it.pendingBookingsCount }

    var selectedTab by remember { mutableStateOf(0) }
    val reservationTabs = listOf("Checking out", "Currently hosting", "Upcoming")
    val contentCounts = listOf(checkingOutCount, currentlyHostingCount, upcomingCount)

    Column(
        modifier = modifier
            .fillMaxWidth()
           // .background(Color.White)
    ) {
        Text(
            text = "Reservations",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            reservationTabs.forEachIndexed { index, tabLabel ->
                contentCounts[index]?.let { count ->
                    ReservationTab(
                        tabLabel = tabLabel,
                        contentCount = count,
                        isSelected = index == selectedTab,
                        onTabSelected = { selectedTab = index },
                    )
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White //if (contentCounts[selectedTab] == 0) Color(0xFFDFDFDF) else Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            modifier = modifier
                .fillMaxWidth()
                //.height(40.dp)
        ){
            if (contentCounts[selectedTab] == 0){
                Column(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.no_list),
                        contentDescription = "",
                        tint = Color(0xFF999999)
                    )
                    Text(
                        text = "You don’t have any guests checking out today or tomorrow.",
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF999999),
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .width(158.dp)
                    )
                }
            } else {
                staycationBookings?.filter { staycationBooking ->
                    when (selectedTab) {
                        0 -> staycationBooking.bookingStatus == "Ongoing" &&
                                (staycationBooking.checkOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() == LocalDate.now() ||
                                        staycationBooking.checkOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() == LocalDate.now().plusDays(1))
                        1 -> staycationBooking.bookingStatus == "Ongoing"
                        2 -> staycationBooking.bookingStatus == "Pending"
                        else -> staycationBooking.bookingStatus != "None"
                    }
                }?.map { staycationBooking ->
                    val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
                    val start = formatter.format(staycationBooking.checkInDate)
                    val end = formatter.format(staycationBooking.checkOutDate)
                    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(staycationBooking.checkInDate)

                    Transaction(
                        customerImage = staycationBooking.tourist?.profilePicture ?: "",
                        bookedRental = staycationBooking.staycation?.staycationTitle ?: "",
                        customerName = "${staycationBooking.tourist?.firstName} ${staycationBooking.tourist?.lastName}",
                        customerUsername = staycationBooking.tourist?.username ?: "",
                        guestsCount = staycationBooking.noOfGuests,
                        price = staycationBooking.totalAmount ?: 0.0,
                        bookedDate = "$start-$end, $year",
                        transactionDate = SimpleDateFormat("MMM dd, yyyy").format(staycationBooking.bookingDate) , //staycationBooking.bookingDate.toString(),
                        transactionStatus = staycationBooking.bookingStatus
                    )
                }?.forEach { transaction ->
                    BookingCard(transaction = transaction)
                }

            }
        }
    }
}

@Composable
fun ReservationTab(
    tabLabel: String,
    contentCount: Int,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onTabSelected,
        border = BorderStroke(width = 1.dp, Orange),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Orange else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier.height(22.dp)
    ) {
        Text(
            text = "$tabLabel ($contentCount)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Orange,
        )
    }
}

@Composable
fun HostPropertiesList(
    onNavToStaycationManager: (String) -> Unit,
    properties: List<HostProperty?>?,
    modifier: Modifier = Modifier
){
    Log.d("Properties", "$properties")
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (properties?.any { it == null } == false) {
            Text(
                text = "Properties",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            properties?.forEach { property ->
                if (property != null) {
                    HostPropertyCard(
                        hostProperty = property,
                        propertyType = "Staycation",
                        onManage = { staycationId ->
                            onNavToStaycationManager(staycationId)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

    }


}

@Composable
fun HostBusinessesList(
    onNavToBusinessManager: (String) -> Unit,
    properties: List<HostProperty?>?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (properties?.any { it == null } == false) {
            Text(
                text = "Business",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            properties?.forEach { property ->
                if (property != null) {
                    HostPropertyCard(
                        hostProperty = property,
                        propertyType = "Business",
                        onManage = { businessId ->
                            onNavToBusinessManager(businessId)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HostToursList(
    onNavToTourManager: (String) -> Unit,
    properties: List<HostProperty?>?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (properties?.any { it == null } == false) {
            Text(
                text = "Tours",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            properties?.forEach { property ->
                if (property != null) {
                    HostPropertyCard(
                        hostProperty = property,
                        propertyType = "Tours",
                        onManage = { tourId ->
                            onNavToTourManager(tourId)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HostPropertyCard(
    hostProperty: HostProperty,
    propertyType: String,
    onManage: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val propertyDescription =
        if (propertyType == "Staycation") {
            "by ${hostProperty.host}"
        }
        else {
            hostProperty.propertyDescription
        }

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
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp, //25
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Column {
                Row {
                    Text(
                        text = hostProperty.propertyName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Manage",
                        onClick = {
                            onManage(hostProperty.propertyId)
                        },
                       // badgeCount = 8
                    )
                }
                Text(
                    text = propertyDescription,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Orange
                )
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.location),
                        contentDescription = "Location",
                        tint = Color(0xFF999999),
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = if (hostProperty.location == "") "Somewhere in Metro Manila" else hostProperty.location,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ElevatedCard(
                modifier = Modifier
                    .height(70.dp) //.height(90.dp)
                    .width(90.dp) // .width(150.dp)
                ,
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                )
            ){

                AsyncImage(
                    model = hostProperty.image,//imageLoader,
                    contentDescription = "",
                    contentScale = ContentScale.Crop

                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOutlinedButtonWithBadge(
    buttonLabel: String,
    badgeCount: Int? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
){
    BadgedBox(
        badge = {
            if (badgeCount != null) {
                Badge(
                    containerColor = Color(0xFFF96E00),
                    contentColor = Color.White,
                    modifier = Modifier.offset(x = (-5).dp, y = 5.dp)
                ) {
                    Text(badgeCount.toString())
                }
            }
        }
    ) {
        OutlinedButton(
            onClick = {
                if (onClick != null) {
                    onClick()
                }
            },
            border = BorderStroke(width = 1.dp, Orange),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = modifier.height(22.dp)
        ) {
            Text(
                text = buttonLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Orange,
            )
        }

    }
}

@Composable
fun BookingCard(transaction: Transaction, modifier: Modifier = Modifier){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(24.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Expand",
                    tint = Color(0xFF999999),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    horizontal = 5.dp,
                    //vertical = 10.dp // 12
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                    ) {
                        AsyncImage(
                            model = if (transaction.customerImage == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else transaction.customerImage,//imageLoader,
                            contentDescription = "",
                            contentScale = ContentScale.Crop

                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = transaction.customerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = transaction.customerUsername,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                    }

                    Text(
                        text = transaction.transactionDate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Orange,
                        modifier = Modifier.offset(x = (-10).dp)
                    )

                }
                Text(
                    text = "${transaction.guestsCount} guests • ₱ ${"%.2f".format(transaction.price)} • ${transaction.bookedDate} • ${transaction.bookedRental}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                )
            }

        }

        Divider(modifier = Modifier.align(Alignment.BottomCenter))
    }


}


@Preview
@Composable
private fun HostDashboardItemPreview(){

//    val transaction = Transaction(
//        customerImage = R.drawable.joshua,
//        bookedRental = "Staycation in Pasig",
//        customerName = "Juan Cruz",
//        customerUsername = "@jcruz",
//        guestsCount = 4,
//        price = 2500.00,
//        bookedDate = "Sep 3-6, 2023",
//        transactionDate = "Aug 24, 2023",
//        transactionStatus = "Completed"
//    )

  //  BookingCard(transaction)

}

@Preview
@Composable
private fun HostDashboardScreenPreview(){

   // ReservationsList(hostDashboardViewModel = null)

    val hostDashboardViewModel = viewModel(modelClass = HostDashboardViewModel::class.java)

//    HostDashboardScreen(
//        touristId ="ITZbCFfF7Fzqf1qPBiwx",
//        onNavToAddListing = { hostId,listingType ->
//           // navigateToAddListing1(navController, hostId, listingType)
//        },
//        onNavToHostTour = { hostId,listingType ->
//            //navigateToAddListing1(navController, hostId, listingType)
//        },
//        onNavToAddBusiness = { hostId,listingType ->
//           // navigateToAddListing1(navController, hostId, listingType)
//        },
//        hostDashboardViewModel = hostDashboardViewModel,
//    )

}