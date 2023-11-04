package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.Orange
import com.example.tripnila.R

@Composable
fun ItineraryScreen(){

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(1)
    }

    var isDialogOpen by remember { mutableStateOf(false) }

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
                AppBottomNavigationBar(
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { newIndex ->
                        selectedItemIndex = newIndex
                    }
                )
            }
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    AppTopBar(
                        headerText = "Itinerary Planner",
                        color = Color.White
                    )
                }
                item {
                    ChooseStaycationButton(
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                            .clickable { isDialogOpen = true }
                    )
                }
                item {
                    BudgetCard(
                        modifier = Modifier
                            .padding(vertical = verticalPaddingValue)
                    )
                }
                item {
                    ActivitiesCard(
                        modifier = Modifier
                            .padding(vertical = verticalPaddingValue)
                    )
                }
                item {
                    YourTripCard(
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                    )
                }
            }
        }
    }

    ChooseStaycationDialog(
        backgroundColor = backgroundColor,
        isDialogOpen = isDialogOpen,
        onDismissRequest = {
            isDialogOpen = false
        },
        onConfirm = {
            isDialogOpen = false
        }
    )
}


@Composable
fun ChooseStaycationButton(modifier: Modifier = Modifier) {

    val stroke = Stroke(width = 10f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(height = 90.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color(0xff7d7d7d),
                    style = stroke,
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Choose Staycation",
                color = Color(0xff7d7d7d),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BudgetCard(modifier: Modifier = Modifier){

    var checkedState by remember {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp
                ),
        ) {
            Text(
                text = "Budget",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp)
            )
            BudgetTextField()
            Checkbox(
                checked = checkedState,
                onCheckedChange = { checkedState = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange
                ),
                modifier = Modifier.offset(y = -5.dp)
            )
            Text(
                text = "No entrance fee",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

    }
}

@Composable
fun ActivitiesCard(modifier: Modifier = Modifier){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.height(200.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 25.dp,
                    vertical = 15.dp
                ),
        ) {
            Text(
                text = "Activities",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                //modifier = Modifier.padding(top = 6.dp)
            )
            ActivitiesTabRow()

        }
    }
}

@Composable
fun YourTripCard(modifier: Modifier = Modifier){
    Text(
        text = "Your trip",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .padding(horizontal = 9.dp)

    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.no_activities),
            contentDescription = "No activities",
            tint = Orange
        )
        Text(
            text = "No activities yet",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999),
            modifier = modifier
                .padding(horizontal = 9.dp)

        )
    }
}

@Composable
fun ChooseStaycationDialog(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    if (isDialogOpen) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                modifier = modifier
                    //.height(500.dp)
                    .width(300.dp),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp, // 20
                            vertical = 15.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Staycation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                    )
                    ChooseStaycationCard(
                        staycationName = "Greenhills Staycation",
                        staycationStatus = "Booked",
                        staycationDate = "Sep 7-9",
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                    ChooseStaycationCard(
                        staycationName = "Greenhills Staycation",
                        staycationStatus = "Booked",
                        staycationDate = "Sep 7-9",
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                    Row(
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        BookingOutlinedButton(buttonText = "Cancel", onClick = onDismissRequest)
                        Spacer(modifier = Modifier.width(40.dp))
                        BookingFilledButton(buttonText = "Confirm", onClick = onConfirm)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStaycationCard(
    staycationName: String,
    staycationStatus: String,
    staycationDate: String,
    modifier: Modifier = Modifier
){

    var isClicked by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = {
            isClicked = !isClicked
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20),
        border = if(isClicked) BorderStroke(2.dp, Color(0xfff9a664)) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
           // pressedElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Row {
                Text(
                    text = staycationName,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = staycationStatus,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Orange)
                        .padding(vertical = 2.dp, horizontal = 4.dp)

                )
            }
            Text(
                text = staycationDate,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF999999)
            )
        }
    }
}

@Composable
fun ActivitiesTabRow(){

    val preferences = listOf("Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")
    val selectedTab = remember { mutableIntStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTab.intValue,
        //contentColor = Color.Gray,
        containerColor = Color.White,
        edgePadding = 3.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Orange,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.intValue])
            )
        },
        divider = { Divider(color = Color.Transparent) }


    ) {
        preferences.forEachIndexed { index, text ->
            val isSelected = selectedTab.intValue == index
            Tab(
                selected = isSelected,
                onClick = {
                    selectedTab.intValue = index
                },
                modifier = Modifier.padding(horizontal = 3.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Orange else Color.Gray,
                    modifier = Modifier.padding(top = 8.dp, bottom = 5.dp, start = 3.dp, end = 3.dp),
                )

            }
        }
    }

}

@Composable
fun BudgetTextField(modifier: Modifier = Modifier) {
    var text by remember {
        mutableStateOf("")
    }

    var isFocused by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        textStyle = TextStyle(
            //fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp) // margin left and right
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Orange else Color(0xFFC2C2C2),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "₱ ",
                        //fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC2C2C2),

                        )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}


//@Preview
//@Composable
//private fun Group24Preview() {
//    ActivitiesCard()
//}

@Preview
@Composable
fun ItineraryScreenPreview(){
    ItineraryScreen()


}