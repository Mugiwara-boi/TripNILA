package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Message
import com.example.tripnila.model.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    senderTouristId: String,
    receiverTouristId: String,
){

    val messages = chatViewModel.messages.collectAsState().value

    var name = "Joshua"
    var isActive = true
    var activeStatus = "Online"

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {

                ChatTopBar(name = name, isActive = isActive, activeStatus = activeStatus)

            },
            bottomBar = {
                ChatBottomBar(
                    chatViewModel = chatViewModel
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (messages.isEmpty()) {
                   // AsyncImage(model = , contentDescription = )
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
                        isIncoming = false
                    )
                }

            }

        }
    }
}

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message,
    isIncoming: Boolean

){

    val incomingShape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomEnd = 8.dp,
        bottomStart = 0.dp
    )

    val outgoingShape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomEnd = 0.dp,
        bottomStart = 8.dp
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = if (isIncoming) Orange.copy(0.3f) else Orange,
            shape = if (isIncoming) incomingShape else outgoingShape,
            modifier = Modifier
                .align(if (isIncoming) Alignment.Start else Alignment.End)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
            ) {
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = if (isIncoming) Orange else Color.White,
                   // textAlign = if (isIncoming) TextAlign.Start else TextAlign.End
                )
                Text(
                    text = message.timestamp.toString(),
                    fontSize = 8.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.End
                )

            }

        }
    }

}

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel
){


//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .background(color = Color.White)
//            .border(0.1.dp, Color.Black)
//    ) {
    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 15.dp,
//                    vertical = 25.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ){

            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = CircleShape,
                containerColor = Orange,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                )
            }
            ChatTextField(
                chatViewModel = chatViewModel
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    name: String,
    isActive: Boolean,
    activeStatus: String,
    modifier: Modifier = Modifier
){

    var color =
        if (isActive) {
            Color.Green
        } else {
            Color(0xff999999)
        }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

        },
        title = {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Row {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp, end = 5.dp)
                            .size(5.dp)
                            .clip(shape = CircleShape)
                            .background(color = color)
                    )
                    Text(
                        text = activeStatus,
                        fontSize = 10.sp,
                        color = Color.White,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.more_horizontal),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange
        ),
        modifier = modifier
    )
}

@Composable
fun ChatTextField(
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
){


    var text by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
        modifier = modifier
            .fillMaxWidth()
            //.height(40.dp)
            .border(1.dp, Orange, shape = RoundedCornerShape(10.dp))
        ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
            if (text.isNotEmpty()) {
                chatViewModel.sendMessage(text)
                text = ""
            }
        },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                   // .fillMaxHeight()
            ){
                Row(
                   // modifier =Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Type your message here...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF6B6B6B),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.emoji),
                        tint = Orange,
                        contentDescription = "contentDescription"
                    )
                }
                innerTextField()
            }
        }
    )
}


@Preview
@Composable
private fun ChatScreenPreview(){

    val chatViewModel = viewModel(modelClass = ChatViewModel::class.java)

    ChatScreen(
        chatViewModel = chatViewModel,
        senderTouristId = "ITZbCFfF7Fzqf1qPBiwx",
        receiverTouristId = "3BKN3xDmKlI4P60FW3Q9"
    )

}