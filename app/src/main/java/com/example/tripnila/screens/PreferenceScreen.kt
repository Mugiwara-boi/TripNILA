package com.example.tripnila.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.PreferenceCard
import com.example.tripnila.common.TripNilaIcon


@Composable
fun PreferenceScreen(){

    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        TripNilaIcon()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 110.dp)
        ) {
            Text(
                text = "Choose your interests",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
            Row(
            ) {
                PreferenceCard(cardLabel = "Sports",
                    painterResource = painterResource(id = R.drawable.sports),
                    modifier = Modifier

                )
                PreferenceCard(cardLabel = "Food Trip",
                    painterResource = painterResource(id = R.drawable.food_trip),
                    modifier = Modifier

                )
            }
            Row(
            ) {
                PreferenceCard(cardLabel = "Shopping",
                    painterResource = painterResource(id = R.drawable.shopping),
                    modifier = Modifier

                )
                PreferenceCard(cardLabel = "Nature",
                    painterResource = painterResource(id = R.drawable.nature),
                    modifier = Modifier

                )
            }
            Row(
            ) {
                PreferenceCard(cardLabel = "Gaming",
                    painterResource = painterResource(id = R.drawable.gaming),
                    modifier = Modifier
                )
                PreferenceCard(cardLabel = "Karaoke",
                    painterResource = painterResource(id = R.drawable.karaoke),
                    modifier = Modifier
                )
            }
            Row(
            ) {
                PreferenceCard(cardLabel = "History",
                    painterResource = painterResource(id = R.drawable.history),
                    modifier = Modifier
                )
                PreferenceCard(cardLabel = "Clubs",
                    painterResource = painterResource(id = R.drawable.club),
                    modifier = Modifier
                )
            }
            Row(
            ) {
                PreferenceCard(cardLabel = "Sightseeing",
                    painterResource = painterResource(id = R.drawable.sightseeing),
                    modifier = Modifier
                )
                PreferenceCard(cardLabel = "Swimming",
                    painterResource = painterResource(id = R.drawable.swimming),
                    modifier = Modifier
                )
            }
            Spacer(
                modifier = Modifier
                    .height(35.dp)
            )
            BookingFilledButton(
                buttonText = "Confirm",
                onClick = {},
                modifier = Modifier.width(width = 216.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreferenceScreenPreview(){
    PreferenceScreen()
}

