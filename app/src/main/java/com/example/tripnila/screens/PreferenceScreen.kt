package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.common.TripNilaIcon
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.SignupViewModel
import kotlinx.coroutines.launch


@Composable
fun PreferenceScreen(
    touristId: String,
    preferenceViewModel: PreferenceViewModel? = null,
    onNavToHomeScreen: (String) -> Unit
){
    val preferenceUiState = preferenceViewModel?.preferenceUiState?.collectAsState()
    var isError = preferenceUiState?.value?.preferenceError?.isNotEmpty()


    val headerText = if (isError == true) preferenceUiState?.value?.preferenceError  else "Choose you interests"
    val textColor = if (isError == true) Color.Red  else Color.Black

    val preferences = listOf(
        "Sports", "Food Trip", "Shopping", "Nature", "Gaming",
        "Karaoke", "History", "Clubs", "Sightseeing", "Swimming"
    )

    var selectedItems by remember { mutableStateOf(mutableSetOf<String>()) }

    Surface(modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
           modifier = Modifier.fillMaxSize()
        ) {
            TripNilaIcon()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 90.dp)
                    .padding(it)
            ) {

                Text(
                    text = "Choose you interests",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 24.dp)
                        .align(Alignment.Start),
                )
                if (isError == true) {
                    Text(
                        text = preferenceUiState?.value?.preferenceError.toString(), //"Choose atleast 1 preference *"
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.Start),
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        //.padding(top = 24.dp)
                ) {



                    items(preferences) { preferenceLabel ->

                        PreferenceCard(
                            cardLabel = preferenceLabel,
                            painterResource = painterResource(id = getDrawableResourceId(preferenceLabel)),
                            selected = preferenceLabel in selectedItems,
                            onSelectedChange = { isSelected ->
                                if (isSelected) {
                                    selectedItems.add(preferenceLabel)
                                } else {
                                    selectedItems.remove(preferenceLabel)
                                }
                                // Update ViewModel with selected preferences
                                preferenceViewModel?.setSelectedPreference(selectedItems.toList())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(35.dp)
                )

                BookingFilledButton(
                    buttonText = "Confirm",
                    onClick = {
                        preferenceViewModel?.savePreferences(touristId)
                    },
                    isLoading = preferenceUiState?.value?.isLoading ?: false,
                    modifier = Modifier.width(width = 216.dp)
                )

                LaunchedEffect(preferenceUiState?.value?.isPreferenceAdded) {
                    if (preferenceUiState?.value?.isPreferenceAdded == true) {
                        onNavToHomeScreen.invoke(touristId)
                    }
                }
            }
        }
    }
}



//                LaunchedEffect(preferenceUiState?.value?.isPreferenceAdded) {
//                    val isSuccess = preferenceUiState?.value?.isPreferenceAdded
//
//                    isSuccess?.let { success ->
//                        val snackbarMessage = if (success) {
//                            "Preference added successfully"
//                        } else {
//                            "Not added"
//                        }
//
//                        if (success) {
//
//                            preferenceViewModel.currentUser.value?.let { currentUser -> onNavToHomeScreen.invoke(currentUser.touristId) }
//
//                        }
////                        coroutineScope.launch {
////                            scaffoldState.snackbarHostState.showSnackbar(snackbarMessage)
////                        }
//                    }
//                }


fun getDrawableResourceId(preferenceLabel: String): Int {
    // Map preference labels to corresponding drawable resource IDs
    return when (preferenceLabel) {
        "Sports" -> R.drawable.sports
        "Food Trip" -> R.drawable.food_trip
        "Shopping" -> R.drawable.shopping
        "Nature" -> R.drawable.nature
        "Gaming" -> R.drawable.gaming
        "Karaoke" -> R.drawable.karaoke
        "History" -> R.drawable.history
        "Clubs" -> R.drawable.club
        "Sightseeing" -> R.drawable.sightseeing
        "Swimming" -> R.drawable.swimming
        else -> R.drawable.image_placeholder // Add a default drawable resource or handle unknown labels
    }
}

@Composable
fun PreferenceCard(
    cardLabel: String,
    painterResource: Painter,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier
) {

    val borderColor = if (selected) Orange else Color(0xff999999)

    Card(
        modifier
            .padding(horizontal = 8.dp, vertical = 7.dp)
            .height(86.dp)
            .width(154.dp)
            .clickable {
                onSelectedChange(!selected)
            }
        ,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, borderColor)

    ) {
        Box {
            Image(
                painter = painterResource,
                contentDescription = cardLabel,
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
            )
            Box(modifier = Modifier.fillMaxSize()){
                CircularCheckbox(
                    modifier = Modifier
                        .size(13.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-6).dp, y = 6.dp),
                    checked = selected,
                    onCheckedChange = { onSelectedChange(!selected) }
                )
            }
            Text(
                text = cardLabel,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


//@Composable
//fun PreferenceCard(
//    cardLabel: String,
//    painterResource: Painter,
//    selected: Boolean,
//    onSelectedChange: (Boolean) -> Unit,
//    onCheckedChange: (Boolean) -> Unit,
//    modifier: Modifier
//) {
//
//    val borderColor = if (selected) Orange else Color(0xff999999)
//    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White
//
//    Card(
//        modifier
//            .padding(horizontal = 8.dp, vertical = 7.dp)
//            .height(86.dp)
//            .width(154.dp)
//            .clickable {
//                onSelectedChange(!selected)
//            }
//        ,
//        shape = RoundedCornerShape(10.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = containerColor
//        ),
//        border = BorderStroke(1.dp, borderColor)
//
//    ) {
//        Box {
//            Image(
//                painter = painterResource,
//                contentDescription = cardLabel,
//                contentScale = ContentScale.Crop
//            )
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5f))
//            )
//            Box(modifier = Modifier.fillMaxSize()){
//                CircularCheckbox(
//                    modifier = Modifier
//                        .size(13.dp)
//                        .align(Alignment.TopEnd)
//                        .offset(x = (-6).dp, y = 6.dp),
//                    checked = selected,
//                    onCheckedChange = { onCheckedChange(!selected) }
//                )
//            }
//            Text(
//                text = cardLabel,
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp,
//                color = Color.White,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//    }
//}


@Composable
fun CircularCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (checked) Color(0xfff9a664) else Color.White,
                shape = CircleShape,
            )
            .border(width = 1.dp, color = Color(0xFF838383), shape = CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
//        if (checked) {
//            Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(16.dp)
//            )
//        }
    }
}



@Preview
@Composable
fun PreferenceScreenPreview(){
    PreferenceScreen( "", onNavToHomeScreen = {})
}

