package com.example.tripnila.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.model.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun HostChatScreen(
    chatViewModel: ChatViewModel,
    chatId: String = "",
    senderTouristId: String,
    receiverTouristId: String,
    onBack: () -> Unit,
){
    Log.d("ChatId", chatId)

    val messages = chatViewModel.messages.collectAsState().value.sortedBy { it.timestamp }
    val currentUser = chatViewModel.currentUser.collectAsState().value
    val otherUser = chatViewModel.otherUser.collectAsState().value
    val isLoading = chatViewModel.isLoading.collectAsState().value

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val (clickedImageUrl, setClickedImageUrl) = remember { mutableStateOf<String?>(null) }

    Log.d("Message Size", messages.size.toString())

    val listState = rememberLazyListState()


    LaunchedEffect(receiverTouristId) {
        chatViewModel.setUsers(senderTouristId, receiverTouristId)
    }


    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            delay(200)
            listState.scrollToItem(messages.size - 1)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {

                ChatTopBar(
                    name =  "${otherUser.firstName} ${otherUser.lastName}",
                    isActive = false,
                    activeStatus = "Offline",
                    onBack = {
                        onBack()
                    }
                )

            },
            bottomBar = {
                ChatBottomBar(
                    chatViewModel = chatViewModel,
                    context = context
                )
            }
        ) {
            if (isLoading) {
                LoadingScreen(isLoadingCompleted = false, isLightModeActive = false)
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    if (messages.isEmpty() && !isLoading) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 280.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(painter = painterResource(id = R.drawable.chat_placeholder), contentDescription = null)
                                Text(
                                    text = "Start a conversation",
                                    color = Color(0xff999999),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium),
                                )
                            }
                        }
                    }
                    items(messages) { message ->
                        ChatBubble(
                            message = message,
                            isIncoming = currentUser.touristId != message.senderId,
                            onClick = { clickedImage ->
                                setClickedImageUrl(clickedImage)
                            }
                        )
                    }

                }
            }
        }
        if (clickedImageUrl != null) {
            FullScreenImage(
                imageUrl = clickedImageUrl,
                onClose = { setClickedImageUrl(null) },
                onSave = {
                    scope.launch {
                        val bitmap = urlToBitmap(context, clickedImageUrl)
                        if (saveImageToGallery(context, bitmap, UUID.randomUUID().toString(), "New image")) {
                            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}