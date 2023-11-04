package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.AdditionalInformationRow
import com.example.tripnila.components.AppOutlinedButton
import com.example.tripnila.components.AppReviewsCard
import com.example.tripnila.components.Orange
import com.example.tripnila.components.Tag
import com.example.tripnila.components.UnderlinedText
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Attraction
import com.example.tripnila.data.Review


@Composable
fun StaycationDetailsScreen(){

    val amenities = listOf(
        Amenity(
            image = R.drawable.person,
            count = 4,
            name = "person"
        ),
        Amenity(
            image = R.drawable.pool,
            count = 1,
            name = "swimming pool"
        ),
        Amenity(
            image = R.drawable.bedroom,
            count = 2,
            name = "bedroom"
        ),
        Amenity(
            image = R.drawable.bathroom,
            count = 2,
            name = "bathroom"
        ),
        Amenity(
            image = R.drawable.kitchen,
            count = 1,
            name = "kitchen"
        )
    )
    val attractions = listOf(
        Attraction(
            image = R.drawable.map_image,
            name = "Rainforest Park",
            tag = "Nature",
            distance = 500,
            price = 1000.00,
            openingTime = "7:30"
        ),
        Attraction(
            image = R.drawable.map_image,
            name = "Pasig Museum",
            tag = "History",
            distance = 2700,
            price = 1000.00,
            openingTime = "9:00"
        ),
    )
    val reviews = listOf(
        Review(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
            touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
    )


    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.staycation1),
                        contentDescription = "h5 1",
                        contentScale = ContentScale.FillWidth
                    )
                    TopBarIcons()
                }
            }
            item {
                StaycationDescriptionCard1(
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                StaycationDescriptionCard2(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationDescriptionCard3(
                    amenities = amenities,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                AttractionsNearbyCard(
                    attractions = attractions,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                AppReviewsCard(
                    reviews = reviews,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationAmenitiesCard(
                    amenities = amenities,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationAdditionalInformationCard(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 7.dp)
                )
            }
            item {
                StaycationBottomBookingBar()
            }
        }
    }
}

@Composable
fun TopBarIcons(
    forStaycationManager: Boolean = false,
    modifier: Modifier = Modifier
) {

    val iconColor = Color.White  //Color(0xFFC0C0C0)
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 50.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp, end = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = iconColor
                //modifier = Modifier.padding(start = 8.dp, top = 15.dp)
            )
            if (!forStaycationManager){
                Spacer(
                    modifier = Modifier
                        .width(265.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = iconColor
                    //modifier = Modifier.padding(start = 8.dp, top = 15.dp)
                )
                Spacer(
                    modifier = Modifier
                        .width(15.dp)
                )

                IconToggleButton(
                    modifier = Modifier.offset(y = (-7).dp),
                    checked = isFavorite,
                    onCheckedChange = {
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        imageVector = if(isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if(isFavorite) Color.Red else iconColor

                    )
                }
            }
        }
    }
}

@Composable
fun StaycationDescriptionCard1(withEditButton: Boolean = false, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Modern House with 2 bedrooms",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.align(Alignment.Start)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .offset(y = 3.dp)
                            .width(40.dp)
                    )
                }

            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                Icon(
                    modifier = Modifier.height(18.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star"
                )
                Text(
                    text = "4.7",
                    //fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(15.dp))
                UnderlinedText(
                    textLabel = "254 reviews",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(5.dp))
                Tag(tag = "Nature")
                Tag(tag = "Movies")
                Tag(tag = "History")
                Tag(tag = "+5")
            }
            Text(
                text = "Rainforest, Pasig City",
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.Start)
            )
        }

    }
}

@Composable
fun StaycationDescriptionCard2(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 81.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This staycation is eco-friendly. Amenities used and attractions are eco-friendly. ",
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    //.width(280.dp)
                    .weight(1f)
            )
            //Spacer(modifier = Modifier.width(14.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.leaf),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun StaycationDescriptionCard3(
    amenities: List<Amenity>,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 139.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.joshua),
                    contentDescription = "Host",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Hosted by Joshua",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 6.dp, bottom = 6.dp)
                    )
                    if (withEditButton) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AppOutlinedButtonWithBadge(
                            buttonLabel = "Edit",
                            modifier = Modifier
                                .offset(y = 9.dp)
                                .width(40.dp)
                        )
                    }

                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val numRows = (amenities.size + 1) / 2

                    for (row in 0 until numRows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            //horizontalArrangement = Arrangement.,
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, amenities.size)

                            for (i in startIndex until endIndex) {
                                StaycationAmenityDetail(amenity = amenities[i])
                            }
                        }
                    }
                }

            }

        }

    }
}

@Composable
fun AttractionsNearbyCard(attractions: List<Attraction>, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 139.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Attractions nearby",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                attractions.forEach { attraction ->
                    AttractionRow(attraction)
                }
            }
            AppOutlinedButton(
                buttonText = "See itinerary",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun StaycationAmenitiesCard(
    amenities: List<Amenity>,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Amenities and offers",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                val numRows = (amenities.size + 1) / 2

                for (row in 0 until numRows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        val startIndex = row * 2
                        val endIndex = minOf(startIndex + 2, amenities.size)

                        for (i in startIndex until endIndex) {
                            StaycationAmenityDetail(amenity = amenities[i])
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            AppOutlinedButton(
                buttonText = "See all amenities",
                modifier = Modifier
                    .padding(top = 12.dp)
            )

        }
    }
}

@Composable
fun StaycationAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Additional information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            AdditionalInformationRow("House Rules")
            AdditionalInformationRow("Health & safety")
            AdditionalInformationRow("Cancellation & reschedule policy")
            AdditionalInformationRow("Business Information")

        }
    }
}

@Composable
fun StaycationBottomBookingBar(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .border(0.1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 12.dp
                ),
            //horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("₱ 2500")
                            }
                            append(" / night")
                        }
                    )
                    Text(
                        text = "for 3 nights",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xff999999),
                        textDecoration = TextDecoration.Underline
                    )
                }
                BookingOutlinedButton(
                    buttonText = "Chat host",
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                BookingFilledButton(buttonText = "Book", onClick = {})
            }

        }

    }
}

@Composable
fun BookingOutlinedButton(
    buttonText: String,
    containerColor: Color = Color.White,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    borderStroke: BorderStroke = BorderStroke(1.dp, Orange),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Orange,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedButton(
        onClick = onClick,
        border = borderStroke,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = contentColor
        )
    }
}

@Composable
fun BookingFilledButton(
    buttonText: String,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClick: () -> Unit,
    containerColor: Color = Orange,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier
){

    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = contentColor
        )
    }

}





@Composable
fun AttractionRow(attraction: Attraction, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ){
        Column {
            Row {
                Text(
                    text = attraction.name,
                    fontWeight = FontWeight.Medium,
                )
                Tag(
                    tag = attraction.tag,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            Row {
                Text(
                    text = attraction.distance.toString() +
                            " meters • ₱ " + String.format("%.2f", attraction.price) +
                            " • Opens at " + attraction.openingTime,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999)
                )
            }
            Divider(modifier = Modifier.padding(top = 20.dp))
        }
        Image(
            painter = painterResource(id = attraction.image),
            contentDescription = "Map",
            modifier = Modifier
                .size(55.dp)
                .align(alignment = Alignment.TopEnd)
        )

    }
}

@Composable
fun StaycationAmenityDetail(amenity: Amenity, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .width(148.dp)
            .padding(
                //horizontal = 5.dp,
                vertical = 3.dp
            )
    ) {
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = amenity.image),
                contentDescription = amenity.name
            )
            Text(
                text = amenity.count.toString() + " " + amenity.name,
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(
                        horizontal = 3.dp,
                        //vertical = 5.dp
                    )
            )
        }
    }
}



@Preview
@Composable
private fun StaycationDetailsPreview() {


    StaycationDetailsScreen()

}
