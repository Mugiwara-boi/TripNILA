package com.example.tripnila.screens

import android.net.Uri
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.VerificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountVerificationScreen(
    touristId: String = "",
    verificationViewModel: VerificationViewModel,
    onBack: () -> Unit,
    onNavToProfile: (String) -> Unit
){


    val context = LocalContext.current

    val validIdOptions = listOf(
        "DRIVER'S LICENSE",
        "PASSPORT",
        "PHIL NATIONAL ID (PHILSYS)",
        "PHILHEALTH",
        "POSTAL ID",
        "PRC ID",
        "SCHOOL ID",
        "SENIOR CITIZEN ID",
        "SSS",
        "TAX IDENTIFICATION NUMBER",
        "UMID",
        "VOTER'S / COMELEC ID"
    )

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val backgroundColor = Color.White

    val firstValidIdType by verificationViewModel.firstValidIdType.collectAsState()
    val secondValidIdType by verificationViewModel.secondValidIdType.collectAsState()
    val firstValidId by verificationViewModel.firstValidId.collectAsState()
    val secondValidId by verificationViewModel.secondValidId.collectAsState()
    val isUploadSuccessful by verificationViewModel.isUploadSuccessful.collectAsState()
    val isLoading by verificationViewModel.isLoading.collectAsState()

    val openAlertDialog = remember { mutableStateOf(false) }

    val firstValidIdPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> verificationViewModel.setFirstValidId(uri) }
    )

    val secondValidIdPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> verificationViewModel.setSecondValidId(uri) }
    )

    LaunchedEffect(isUploadSuccessful) {
        if (isUploadSuccessful != null) {
            if (isUploadSuccessful == true) {
                Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                delay(500)
                onNavToProfile(touristId)
            } else {
                Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        }

    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    rightButtonText = "Confirm",
                    isRightButtonLoading = isLoading,
                    onCancel = {
                        onBack()
                    },
                    onNext = {
                        verificationViewModel.uploadIds(touristId)
                    }
                )
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                AppTopBar(
                    headerText = "Verify your account",
                    color = Color.White
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = horizontalPaddingValue
                        )
                ) {
                    Text(
                        text = "Please upload 2 valid IDs",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                            )
                    )

                    Text(
                        text = "What type of ID you want to use?",
                        fontWeight = FontWeight.Medium,
                    )
                    AppDropDownMenu(
                        options = validIdOptions,
                        selectedText = firstValidIdType,
                        onSelect = { verificationViewModel.setFirstValidIdType(it) }
                    )
                    if (firstValidId == null) {
                        AppChoosePhoto(
                            onClick = {
                                firstValidIdPhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } else {
                        Box {
                            AsyncImage(
                                model = firstValidId,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            IconButton(
                                onClick = { verificationViewModel.clearFirstValidId() },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Image")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "What type of ID you want to use?",
                        fontWeight = FontWeight.Medium,
                    )
                    AppDropDownMenu(
                        options = validIdOptions,
                        selectedText = secondValidIdType,
                        onSelect = { verificationViewModel.setSecondValidIdType(it) }
                    )
                    if (secondValidId == null) {
                        AppChoosePhoto(
                            onClick = {
                                secondValidIdPhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } else {
                        Box {
                            AsyncImage(
                                model = secondValidId,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )

                            IconButton(
                                onClick = { verificationViewModel.clearSecondValidId() },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Image")
                            }


                        }
                    }

                }

            }

        }
    }
}

@Composable
fun VerificationBottomBar(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(0.1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
        ) {
            BookingOutlinedButton(
                buttonText = "Cancel",
                onClick = {},
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                buttonText = "Confirm",
                onClick = {},
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
fun AppDropDownMenu(options: List<String>, selectedText:String, onSelect: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(vertical = 10.dp)) {
        BasicTextField(
            value = selectedText,
            onValueChange = {
                onSelect(it)
            },
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            enabled = false,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp))
                        .background(Color.White))
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.valid_id),
                                contentDescription = "Valid Id",
                                tint = Color(0xFF999999),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                //modifier = Modifier.size(24.dp)
                            )
                            if (selectedText.isEmpty()) {
                                Text(
                                    text = "Valid ID",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF6B6B6B)
                                )
                            }
                            innerTextField()
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector =  icon,
                                tint = Color(0xFF999999),
                                contentDescription = "contentDescription",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(end = 3.dp)
                                    .clickable { expanded = !expanded }
                            )
                        }
                    }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                .background(Color.White)
        ) {
            options.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF6B6B6B)
                    ),
                    onClick = {
                        onSelect(label)
                        expanded = false
                    }
                )
            }
        }
    }

}

@Composable
fun AppChoosePhoto(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = modifier
            .width(width = 153.dp)
            .height(height = 114.dp)
            .drawBehind {
                drawRoundRect(color = Orange, style = stroke)
            }
            .clickable { onClick() },
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Orange
            )
            Text(
                text = "Choose photo",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Orange
            )
        }
    }
}

@Composable
fun ValidIDCard(image: Int = R.drawable.image_placeholder, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .height(165.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFF666666))
    ){
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
    }
}



@Preview
@Composable
private fun AccountVerificationItemPreview(){

    ValidIDCard()
}

@Preview
@Composable
private fun AccountVerificationScreenPreview(){

    val verificationViewModel = viewModel(modelClass = VerificationViewModel::class.java)
    
//    AccountVerificationScreen(
//        touristId = "",
//        verificationViewModel = verificationViewModel,
//        onBack = {
//
//        }
//
//    )
}