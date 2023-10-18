package com.example.tripnila.screens

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.tripnila.R
import com.example.tripnila.components.Orange

@Composable
fun AccountVerificationScreen(){


    val validIdOptions = listOf("Driver's License", "Passport", "Postal ID", "Philippine Identification (PhilID / ePhilID)")
    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val backgroundColor = Color.White

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Scaffold(
            bottomBar = {
                VerificationBottomBar()
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
                    AppDropDownMenu(options = validIdOptions)
                    ValidIDCard(modifier = Modifier.padding(vertical = 5.dp))
                    Text(
                        text = "What type of ID you want to use?",
                        fontWeight = FontWeight.Medium,
                    )
                    AppDropDownMenu(options = validIdOptions)
                    AppChoosePhoto()
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
fun AppDropDownMenu(options: List<String>) {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(vertical = 10.dp)) {
        BasicTextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
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
                        selectedText = label
                        expanded = false
                    }
                )
            }
        }
    }

}

@Composable
fun AppChoosePhoto(modifier: Modifier = Modifier) {

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
            },
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
    AccountVerificationScreen()
}