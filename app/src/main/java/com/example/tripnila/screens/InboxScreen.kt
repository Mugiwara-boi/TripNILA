package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripnila.R
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.Inbox

@Composable
fun InboxScreen(
    touristId: String = "",
    navController: NavHostController? = null
){

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(2)
    }

    val inboxData = listOf(
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
        Inbox(
            image = R.drawable.joshua,
            name = "Ryan Cruz",
            inboxPreview = "Sent a photo"
        ),
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(headerText = "Inbox")
            },
            bottomBar = {
                navController?.let {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
            }
        ) {
            Divider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(inboxData){ inboxItem ->
                    InboxItem(inboxItem)
                }
            }

        }
    }
}

@Composable
fun InboxItem(inbox: Inbox, modifier: Modifier = Modifier){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(50.dp))
        ) {
            Image(
                painter = painterResource(id = inbox.image),
                contentDescription = inbox.name,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = inbox.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = inbox.inboxPreview,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF999999)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Expand",
            modifier = Modifier
                .padding(vertical = 7.dp)
                .size(35.dp)
                .clickable {

                }
        )
    }
    Divider(color = Color(0xFF999999))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(headerText: String, color: Color = Color(0xFF999999), modifier: Modifier = Modifier){

    TopAppBar(
//        colors = topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
//        ),
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }

    )

}

@Preview
@Composable
private fun InboxScreenPreview(){
    InboxScreen("")

}
