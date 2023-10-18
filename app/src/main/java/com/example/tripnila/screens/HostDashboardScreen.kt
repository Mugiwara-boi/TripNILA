package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.AppBottomNavigationBar
import com.example.tripnila.components.Orange
import com.example.tripnila.data.HostProperty

@Composable
fun HostDashboardScreen(){

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val staycationProperties = listOf(
        HostProperty(
            propertyName = "Modern house with 2 bedrooms",
            image = R.drawable.staycation1,
            host = "Joshua",
            location = "North Greenhills"
        ),
        HostProperty(
            propertyName = "Modern house with 2 bedrooms",
            image = R.drawable.staycation1,
            host = "Joshua",
            location = "North Greenhills"
        ),
    )
    val businessProperties = listOf(
        HostProperty(
            propertyName = "Josh's Bbq Grill",
            propertyDescription = "Bar & Grill",
            image = R.drawable.business1,
            host = "Joshua",
            location = "12th St. Sitio Santo, Quezon, City",

        )
    )
    
    val tourProperties = listOf(
        HostProperty(
            propertyName = "Cubao Night Tour",
            propertyDescription = "Food Trip",
            image = R.drawable.tour1,
            host = "Joshua",
            location = "12th St. Sitio Santo, Quezon, City"
        )
    )

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
                        hostName = "Joshua Araneta",
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
                            onClick = {}
                        )
                        HostOptionButton(
                            buttonIcon = R.drawable.host_tour,
                            buttonLabel = "Host a tour",
                            onClick = {}
                        )
                        HostOptionButton(
                            buttonIcon = R.drawable.add_business,
                            buttonLabel = "Add business",
                            onClick = {}
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Available balance",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text =  "₱ ${String.format("%.2f", hostBalance)}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
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
            onClick = onClick,
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
fun ReservationsList(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val reservationTabs = listOf("Checking out", "Currently hosting", "Upcoming")
    val contentCounts = listOf(0, 15, 5)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
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
                ReservationTab(
                    tabLabel = tabLabel,
                    contentCount = contentCounts[index],
                    isSelected = index == selectedTab,
                    onTabSelected = { selectedTab = index },
                )
            }
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (contentCounts[selectedTab] == 0) Color(0xFFDFDFDF) else Color.White
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
fun HostPropertiesList(properties: List<HostProperty>, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Properties",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        properties.forEach { property ->
            HostPropertyCard(
                hostProperty = property,
                propertyType = "Staycation",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun HostBusinessesList(properties: List<HostProperty>, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Business",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        properties.forEach { property ->
            HostPropertyCard(
                hostProperty = property,
                propertyType = "Business",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun HostToursList(properties: List<HostProperty>, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tours",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        properties.forEach { property ->
            HostPropertyCard(
                hostProperty = property,
                propertyType = "Tours",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun HostPropertyCard(
    hostProperty: HostProperty,
    propertyType: String,
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
                        badgeCount = 8
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
                        text = hostProperty.location,
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
                Image(
                    painter = painterResource(id = hostProperty.image),
                    contentDescription = "Image",
                    contentScale = ContentScale.FillBounds
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
            onClick = {  },
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

@Preview
@Composable
private fun HostDashboardItemPreview(){
    //HostPropertiesList()

    val property = HostProperty(
        propertyName = "Josh's Bbq Grill",
        image = R.drawable.business1,
        host = "Joshua",
        location = "12th St. Sitio Santo, Quezon, City",
        propertyDescription = "Bar & Grill"
    )

    HostPropertyCard(property, "Business")
}

@Preview
@Composable
private fun HostDashboardScreenPreview(){
    HostDashboardScreen()
}