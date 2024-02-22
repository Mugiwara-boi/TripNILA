package com.example.tripnila.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.Glide
import com.example.tripnila.R
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Message
import com.example.tripnila.model.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Objects
import java.util.UUID

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    chatId: String = "",
    senderTouristId: String,
    receiverTouristId: String,
    onBack: () -> Unit,
){

    val messages = chatViewModel.messages.collectAsState().value.sortedBy { it.timestamp }
    val currentUser = chatViewModel.currentUser.collectAsState().value
    val otherUser = chatViewModel.otherUser.collectAsState().value
    val isLoading = chatViewModel.isLoading.collectAsState().value

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val (clickedImageUrl, setClickedImageUrl) = remember { mutableStateOf<String?>(null) }

    Log.d("Message Size", messages.size.toString())

    val listState = rememberLazyListState()

    LaunchedEffect(senderTouristId) {
        chatViewModel.setCurrentUser(senderTouristId)
        chatViewModel.setReceiverInfo(receiverTouristId)
    }

    LaunchedEffect(otherUser) {
        if(otherUser.touristId != "") {
            chatViewModel.getChatByUserIds()
        }
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
                    if (messages.isEmpty()) {
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


@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message,
    isIncoming: Boolean,
    onClick: (String) -> Unit
){
    val formatterTimestamp = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(message.timestamp))
    val formatterDate = DateTimeFormatter.ofPattern("MMMM dd, yyyy").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(message.timestamp))

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

    var isDateVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
    ) {
        if (isDateVisible) {
            Text(
                text = formatterDate,
                fontSize = 9.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(if (isIncoming) Alignment.Start else Alignment.End)
                    .padding(horizontal = 5.dp)
            )
        }
        Surface(
            color = if (isIncoming) Orange.copy(0.3f) else Orange,
            shape = if (isIncoming) incomingShape else outgoingShape,
            modifier = Modifier
                .clickable { isDateVisible = !isDateVisible }
                .fillMaxWidth(.5f)
                .align(if (isIncoming) Alignment.Start else Alignment.End)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp)
            ) {
                if (message.images?.isNotEmpty() == true) {

                    val numColumns = 2
                    val numRows = (message.images.size + numColumns - 1) / numColumns

                    for (row in 0 until numRows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val startIndex = row * numColumns
                            val endIndex = minOf(startIndex + numColumns, message.images.size)

                            for (i in startIndex until endIndex) {
                                val image = message.images[i]
                                AsyncImage(
                                    model = image.photoUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            image.photoUrl?.let { onClick(it) }
                                        }
                                )
                            }

                        }
                    }

                } else {
                    message.content?.let { content ->
                        Text(
                            text = content,
                            fontSize = 14.sp,
                            color = if (isIncoming) Color.Black else Color.White,
                        )
                    }
                }
                Text(
                    text = formatterTimestamp,
                    fontSize = 9.sp,
                    color = if (isIncoming) Color.Gray else Color.White.copy(0.8f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

    }
}

@Composable
fun FullScreenImage(
    imageUrl: String,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Display the image using Coil or any other image loading library
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )

        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.TopEnd)
                .background(color = Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        IconButton(
            onClick = onSave,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd)
                .background(color = Color.Transparent)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.download),
                contentDescription = "Save",
                tint = Color.White
            )
        }
    }
}


@Composable
fun ImageSelectionScreen() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { imageUri ->
                try {
                    // Open an input stream from the content resolver
                    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                        // Decode the input stream into a bitmap
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        // Call the function to save the image to the gallery
                        saveImageToGallery(context, bitmap, "Image", "Description")
                        // Optionally, show a toast message or perform other actions
                        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    // Handle any errors that occur during image saving
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    Button(onClick = { launcher.launch("image/*") }) {
        Text(text = "Select Image")
    }
}

suspend fun urlToBitmap(context: Context, url: String): Bitmap {
    return withContext(Dispatchers.IO) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }
}


fun saveImageToGallery(context: Context, bitmap: Bitmap, displayName: String, description: String): Boolean {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.DESCRIPTION, description)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    }

    val contentResolver = context.contentResolver
    var imageUri: Uri? = null

    return try {
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IOException("Failed to create new MediaStore record")

        // Write the bitmap data to the output stream
        contentResolver.openOutputStream(imageUri).use { outputStream ->
            if (!outputStream?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }!!) {
                throw IOException("Failed to save bitmap")
            }
        }
        true // Return true indicating success
    } catch (e: IOException) {
        // Handle any errors that occur during image saving
        e.printStackTrace()
        imageUri?.let { uri ->
            // Delete the incomplete record if saving failed
            contentResolver.delete(uri, null, null)
        }
        false // Return false indicating failure
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"

    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

}

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    context: Context
){

    var text by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }


    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    Log.d("URI", capturedImageUri.toString())

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
            if (selectedImageUris.isNotEmpty()) {
                text = ""
                chatViewModel.sendMessage(text, selectedImageUris)
                capturedImageUri = Uri.EMPTY
                selectedImageUris = emptyList()
                localFocusManager.clearFocus()
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        capturedImageUri = if(it) {
            uri
        } else {
            Uri.EMPTY
        }

        if (capturedImageUri != Uri.EMPTY) {
            text = ""
            chatViewModel.sendMessage(text, listOf(capturedImageUri))
            selectedImageUris = emptyList()
            capturedImageUri = Uri.EMPTY
            localFocusManager.clearFocus()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            // If permission is denied, show a toast message
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }


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
                ),

            verticalAlignment = Alignment.CenterVertically
        ){


            if (isTextFieldFocused) {
                IconButton(
                    onClick = {
                        isTextFieldFocused = false
                        localFocusManager.clearFocus()
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "",
                        tint = Orange,
                        modifier = Modifier.size(30.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)

                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.camera_launcher),
                        contentDescription = "",
                        tint = Orange,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.gallery_launcher),
                        contentDescription = "",
                        tint = Orange,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
                modifier = if (isTextFieldFocused) {
                    Modifier
                        .weight(1f)
                        .border(1.dp, Orange, shape = RoundedCornerShape(10.dp))
                        .onFocusChanged { isTextFieldFocused = it.isFocused }
                } else {
                    Modifier
                        .weight(1f)
                        .border(1.dp, Orange, shape = RoundedCornerShape(10.dp))
                        .onFocusChanged { isTextFieldFocused = it.isFocused }
                },
                singleLine = !isTextFieldFocused,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(10.dp) //15
                            .fillMaxWidth()
                        // .fillMaxHeight()
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = if(isTextFieldFocused) "Type your message here..." else "Message",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF6B6B6B),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            //Spacer(modifier = Modifier.weight(1f))
                        }
                        innerTextField()
                    }
                }

            )
            FloatingActionButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        chatViewModel.sendMessage(text, selectedImageUris)
                        text = ""
                        selectedImageUris = emptyList()
                        capturedImageUri = Uri.EMPTY
                        localFocusManager.clearFocus()
                    }

                },
                shape = CircleShape,
                containerColor = if(text.isEmpty()) Orange.copy(0.4f)  else Orange,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp),
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    name: String,
    isActive: Boolean,
    activeStatus: String,
    onBack: () -> Unit,
){

    val color =
        if (isActive) {
            Color.Green
        } else {
            Color(0xff999999)
        }

    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
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



@Preview
@Composable
private fun ChatScreenPreview(){

    val chatViewModel = viewModel(modelClass = ChatViewModel::class.java)

    ChatScreen(
        chatViewModel = chatViewModel,
        senderTouristId = "ITZbCFfF7Fzqf1qPBiwx",
        receiverTouristId = "3BKN3xDmKlI4P60FW3Q9",
        onBack = {

        }
    )
   // ChatBottomBar(chatViewModel = chatViewModel)

//    ImageSelectionScreen()

}