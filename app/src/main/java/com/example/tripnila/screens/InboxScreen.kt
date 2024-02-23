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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.Inbox
import com.example.tripnila.model.ChatViewModel
import com.example.tripnila.model.InboxViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    touristId: String = "",
    inboxViewModel: InboxViewModel? = null,
    onNavToChat: (String, String) -> Unit,
    navController: NavHostController? = null
){

    LaunchedEffect(touristId) {
        inboxViewModel?.setCurrentUser(touristId)
    }
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(2)
    }

    val lazyPagingItems = inboxViewModel?.inboxPagingData?.collectAsLazyPagingItems()

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
                lazyPagingItems?.let { items ->
                    items(items) { inboxItem ->
                        if (inboxItem != null) {
                            InboxItem(
                                inbox = inboxItem,
                                currentUser = touristId,
                                onClick = { receiverId ->
                                    onNavToChat(touristId, receiverId)
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun InboxItem(
    modifier: Modifier = Modifier,
    inbox: Inbox,
    currentUser: String,
    onClick: (String) -> Unit
){

    val formatterDate = remember {
        val date = Instant.ofEpochMilli(inbox.timeSent)
        val localDate = LocalDateTime.ofInstant(date, ZoneId.systemDefault()).toLocalDate()

        if (localDate == LocalDate.now()) {
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(date)
        } else {
            DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault()).format(date)
        }
    }

    val lastSender = if (currentUser != inbox.lastSender) {
        inbox.name
    } else {
        "You"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .clickable {
                onClick(inbox.receiverId)
            }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(50.dp))
        ) {
            AsyncImage(
                model = inbox.image,
                contentDescription = null,
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
            Row {
                Text(
                    text = if (inbox.inboxPreview == "") "$lastSender sent a photo" else "$lastSender: ${inbox.inboxPreview}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "  •  $formatterDate",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                    maxLines = 1,
                )
            }
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
fun AppTopBar(
    modifier: Modifier = Modifier,
    headerText: String,
    color: Color = Color(0xFF999999),
    scrollBehavior: TopAppBarScrollBehavior? = null
){

    TopAppBar(
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        scrollBehavior = scrollBehavior,
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

    val inboxViewModel = viewModel(modelClass = InboxViewModel::class.java)


    InboxScreen(
        touristId = "ITZbCFfF7Fzqf1qPBiwx",
        onNavToChat = {_, _ ->},
        inboxViewModel = inboxViewModel
    )


}
