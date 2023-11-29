package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.common.Orange
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
                items(messages) { message ->
                    Text(text = message.content)
                }

            }

        }
    }
}

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.White)
            .border(0.1.dp, Color.Black)
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

//@Composable
//fun UsingBottomBar(){
//    BottomAppBar(
//        actions = {
//            FloatingActionButton(
//                onClick = { /*TODO*/ },
//                shape = CircleShape,
//                containerColor = Orange,
//                contentColor = Color.White,
//                elevation = FloatingActionButtonDefaults.elevation(
//                    defaultElevation = 10.dp
//                ),
//                modifier = Modifier
//                    .padding(end = 10.dp)
//                    .size(30.dp)
//
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = "Add",
//                )
//            }
//            ChatTextField(
//                chatViewModel = chatViewModel
//            )
//        },
//
//
//    )
//}

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