package com.example.tripnila.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddBusinessViewModel
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen8(
    listingType: String = "",
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val selectedMenuImageUris = remember {
        mutableStateOf(addBusinessViewModel?.business?.value?.businessMenu
            ?.filter { it.photoType == "Others" }
            ?.map { it.photoUri }) }


    val selectedCoverImageUri = remember {
        mutableStateOf(addBusinessViewModel?.business?.value?.businessMenu
            ?.firstOrNull { it.photoType == "Cover" }
            ?.photoUri)
    }

    val singleMenuPhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedCoverImageUri.value = uri
            addBusinessViewModel?.setMenuCoverPhoto(uri)
        }
    }


    val multipleMenuPhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        selectedMenuImageUris.value = selectedMenuImageUris.value?.plus(uris)
        addBusinessViewModel?.setMenuSelectedImageUris(uris)
    }

    val mutableAdditionInfo = remember { mutableStateOf( addBusinessViewModel?.business?.value?.additionalInfo) }
    val price = remember { mutableStateOf( addBusinessViewModel?.business?.value?.minSpend?.toInt()) }
    val entranceFee = remember { mutableStateOf( addBusinessViewModel?.business?.value?.entranceFee?.toInt()) }
    var hasEntranceFee by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    onNext = {
                        onNavToNext(listingType)
                    },
                    onCancel = {
                        onNavToBack()
                    },
                    enableRightButton = addBusinessViewModel?.business?.collectAsState()?.value?.businessMenu?.any { it.photoType == "Cover" } == true && addBusinessViewModel.business.collectAsState().value.businessMenu.any { it.photoType == "Others" }

                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        SaveAndExitButton(
                            onClick = { /*TODO*/ }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ){
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 20.dp)
                    .padding(it)
            ) {

                LazyColumn(
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    item {
                        Text(
                            text = "Upload your menu!",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }

                    item {
                        val stroke = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .drawBehind {
                                    drawRoundRect(
                                        color = if (selectedCoverImageUri.value != null) Color.Transparent else Orange,
                                        style = stroke
                                    )
                                }
                        ){

                            AsyncImage(
                                model = selectedCoverImageUri.value,
                                contentDescription = "Cover photo",
                                contentScale = ContentScale.FillWidth,
                            )
                            Button(
                                onClick = {
                                    singleMenuPhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                enabled = true,
                                shape = RoundedCornerShape(3.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Orange,
                                    disabledContainerColor = Orange
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .height(16.dp)
                                    .width(54.dp)
                            ) {
                                Text(
                                    text = "Cover photo",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    modifier = Modifier.wrapContentSize(Alignment.Center)
                                )
                            }
                        }
                    }

                    val numRows = (selectedMenuImageUris.value?.size?.plus(1))?.div(2)
                    if (numRows != null) {
                        items(numRows) { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                //horizontalArrangement = Arrangement.spacedBy(15.dp)
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = selectedMenuImageUris.value?.size?.let { size ->
                                    minOf(startIndex + 2,
                                        size
                                    )
                                }

                                for (i in startIndex until endIndex!!) {
                                    val uri = selectedMenuImageUris.value?.get(i)
                                    Box {
                                        AsyncImage(
                                            model = uri,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(165.dp)
                                                .height(110.dp)
                                                .padding(vertical = 5.dp),
                                            contentScale = ContentScale.FillWidth
                                        )
                                        IconButton(
                                            onClick = {
                                                selectedMenuImageUris.value = selectedMenuImageUris.value?.filter { it != uri }
                                                uri?.let { uri -> addBusinessViewModel?.removeMenuSelectedImage(uri = uri) }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .offset(x = 8.dp, y = (-5).dp)
                                        ) {
                                            Icon(Icons.Sharp.Clear, contentDescription = "Delete", modifier = Modifier.size(16.dp), tint = Color(0xFF999999))
                                        }
                                    }
                                }
                                if (endIndex - startIndex == 1) {
                                    AddMorePhoto(
                                        onClick = {
                                            multipleMenuPhotoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if ((selectedMenuImageUris.value?.size?.rem(2) ?: 0) == 0) {
                        item {
                            AddMorePhoto(
                                onClick = {
                                    multipleMenuPhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )

                                }
                            )
                        }
                    }


                    item {
                            Text(
                                text = "What is the minimum spending on your business?",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                            )

                            BasicTextField(
                                value = formatter.format(price.value),
                                onValueChange = {
                                    price.value = it.replace(",", "").toIntOrNull() ?: 0

                                    addBusinessViewModel?.setMinSpend(price?.value!!.toDouble())
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(fontSize = 18.sp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Row(
                                        modifier = Modifier
                                            .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                                            .border(
                                                width = 2.dp,
                                                color = if (isFocused) Orange else Color(0xFFC2C2C2),
                                                shape = RoundedCornerShape(size = 10.dp)
                                            )
                                            .padding(all = 8.dp), // inner padding
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "₱ ",
                                            fontWeight = FontWeight.Medium,
                                        )

                                        innerTextField()
                                    }
                                }
                            )

                            /*LongBasicTextFieldWithCharacterLimit(
                                inputText = mutableAdditionInfo,
                                maxCharacterLimit = 10,
                                onTextChanged = { newText ->
                                    addBusinessViewModel?.setAdditionalInfo(newText)
                                }
                            )*/
//                        LongBasicTextFieldWithCharacterLimit(
//                            inputText = ,
//                            maxCharacterLimit = 500
//                        )

                    }
                    item{

                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Do you have any entrance fee?",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = hasEntranceFee,
                                onCheckedChange = { isChecked ->
                                    hasEntranceFee = isChecked
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Orange
                                ),
                                modifier = Modifier.offset(x = 13.dp)

                            )

                        }

                            if (hasEntranceFee) {
                                BasicTextField(

                                    value = formatter.format(entranceFee.value),
                                    onValueChange = {
                                        entranceFee.value = it.replace(",", "").toIntOrNull() ?: 0

                                        addBusinessViewModel?.setEntranceFee(entranceFee?.value!!.toDouble())
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(fontSize = 18.sp),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(size = 10.dp)
                                                )
                                                .border(
                                                    width = 2.dp,
                                                    color = if (isFocused) Orange else Color(
                                                        0xFFC2C2C2
                                                    ),
                                                    shape = RoundedCornerShape(size = 10.dp)
                                                )
                                                .padding(all = 8.dp), // inner padding
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "₱ ",
                                                fontWeight = FontWeight.Medium,
                                            )

                                            innerTextField()
                                        }
                                    }
                                )
                            }
                        }


                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
            }
        }
    }
}

@Preview
@Composable
private fun AddBusiness8Preview(){
}

@Preview
@Composable
private fun AddBusinessScreen8Preview(){
   // AddBusinessScreen8()
}