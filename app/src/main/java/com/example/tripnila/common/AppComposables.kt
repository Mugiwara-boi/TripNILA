package com.example.tripnila.common

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.data.BottomNavigationItem
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.screens.AppOutlinedButtonWithBadge
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import java.text.NumberFormat

val Orange = Color(0xfff9a664)

@Composable
fun LoadingScreen(
    isLoadingCompleted: Boolean,
    isLightModeActive: Boolean,
) {
    Box(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize()
            .shimmerLoadingAnimation(isLoadingCompleted, isLightModeActive)
    )
}

fun Modifier.shimmerLoadingAnimation(
    isLoadingCompleted: Boolean = true, // <-- New parameter for start/stop.
    isLightModeActive: Boolean = true, // <-- New parameter for display modes.
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (isLoadingCompleted) { // <-- Step 1.
        return this
    }
    else {
        return composed {

            // Step 2.
            val shimmerColors = ShimmerAnimationData(isLightMode = isLightModeActive).getColours()

            val transition = rememberInfiniteTransition(label = "")

            val translateAnimation = transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "Shimmer loading animation",
            )

            this.background(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                    end = Offset(x = translateAnimation.value, y = angleOfAxisY),
                ),
            )
        }
    }
}

data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColours(): List<Color> {
        return if (isLightMode) {
            val color = Color.White

            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        } else {
            val color = Color.Black

            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        }
    }
}

@Composable
fun TripNilaHeader(color: Color){

    val lobsterTwoFamily = FontFamily(
        Font(R.font.lobstertwo_regular, FontWeight.Normal)
    )
    val leagueGothicFamily = FontFamily(
        Font(R.font.leaguegothic_regular, FontWeight.Normal)
    )

    Row {
        Text(text = "T",
            fontFamily = lobsterTwoFamily,
            fontWeight =  FontWeight.Normal,
            fontSize = 120.sp,
            color = color
        )
        Text(text = "ripNILA",
            fontFamily = leagueGothicFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 68.sp,
            color = color,
            modifier = Modifier
                .offset(x = (-27).dp, y = 47.dp)
        )
    }

}

@Composable
fun TripNilaIcon(modifier: Modifier = Modifier){
    val lobsterTwoFamily = FontFamily(
        Font(R.font.lobstertwo_regular, FontWeight.Normal)
    )

    Box(
        modifier
            .fillMaxSize()
            .offset(x = 30.dp, y = 24.dp) // 27.dp
    ) {
        Text(
            text = "T",
            fontFamily = lobsterTwoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp
        )
    }
}


//@Composable
//fun TextFieldWithIcon(
//    labelValue: String,
//    keyboardOptions: KeyboardOptions,
//    keyboardActions: KeyboardActions,
//    painterResource: Painter
//){
//    val textValue = remember {
//        mutableStateOf("")
//    }
//
//    OutlinedTextField(
//        label = { Text(text = labelValue) },
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Orange,
//            focusedLabelColor = Orange
//        ),
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        singleLine = true,
//        maxLines = 1,
//        value = textValue.value,
//        onValueChange = {
//            textValue.value = it
//        },
//        leadingIcon = {
//            Icon(painter = painterResource, contentDescription = "")
//        }
//    )
//}

@Composable
fun TextFieldWithIcon(
    textValue: String,
    onValueChange: (String) -> Unit,
    labelValue: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    supportingText: @Composable (() -> Unit)?,
    painterResource: Painter
) {

    var isFocused by remember { mutableStateOf(false) }
    //val focusRequester = FocusRequester()

    OutlinedTextField(
        value = textValue,
        onValueChange = { onValueChange(it) },
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            focusedLabelColor = Orange,
            cursorColor = Orange,
            selectionColors = TextSelectionColors(Orange, Orange)
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        supportingText = supportingText,
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = null // Provide appropriate content description
            )
        },
        trailingIcon = {
            if (isFocused && textValue.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear text")
                }
            }
        },
        modifier = Modifier
           // .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused },
    )
//    DisposableEffect(Unit) {
//        focusRequester.requestFocus()
//        onDispose {
//            focusRequester.freeFocus()
//        }
//    }
}

//@Composable
//fun TextFieldWithIcon(
//    textValue: String,
//    onValueChange: (String) -> Unit,
//    labelValue: String,
//    keyboardOptions: KeyboardOptions,
//    keyboardActions: KeyboardActions,
//    painterResource: Painter
//) {
//    var isFocused by remember { mutableStateOf(false) }
//
//    val focusRequester = FocusRequester()
//
//    OutlinedTextField(
//        value = textValue,
//        onValueChange = { newValue ->
//            onValueChange(newValue)
//        },
//        label = { Text(text = labelValue) },
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Orange,
//            focusedLabelColor = Orange
//        ),
//        keyboardOptions = keyboardOptions,
//        keyboardActions = keyboardActions,
//        singleLine = true,
//        maxLines = 1,
//        leadingIcon = {
//            Icon(
//                painter = painterResource,
//                contentDescription = null
//            )
//        },
//        trailingIcon = {
//            if (isFocused && textValue.isNotEmpty()) {
//                IconButton(onClick = { onValueChange("") }) {
//                    Icon(
//                        painter = painterResource,
//                        contentDescription = "Clear Text"
//                    )
//                }
//            }
//        },
//        modifier = Modifier.focusRequester(focusRequester),
//        onFocusChanged = { isFocused = it.isFocused }
//    )
//
//    DisposableEffect(Unit) {
//        focusRequester.requestFocus()
//        onDispose { /* cleanup */ }
//    }
//}



@Composable
fun PasswordFieldWithIcon(
    password: String,
    onValueChange: (String) -> Unit,
    labelValue: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    painterResource: Painter
){
    val isHidden = remember {
        mutableStateOf(true)
    }

    OutlinedTextField(
        value = password,
        label = { Text(text = labelValue) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Orange,
            focusedLabelColor = Orange,
            cursorColor = Orange,
            selectionColors = TextSelectionColors(Orange, Orange)
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        keyboardActions = keyboardActions,
        maxLines = 1,
        onValueChange = { onValueChange(it) },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val visibilityIcon: Painter = painterResource(
                id = if (isHidden.value) R.drawable.visibility_on else R.drawable.visibility_off
            )
            IconButton(onClick = { isHidden.value = !isHidden.value }) {
                Icon(
                    painter = visibilityIcon,
                    contentDescription = if (isHidden.value) "Show Password" else "Hide Password"
                )
            }
        },
        visualTransformation = if (isHidden.value) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun CreatePasswordFieldWithIcon(labelValue: String, painterResource: Painter){
    val password = remember {
        mutableStateOf("")
    }
    val isHidden = remember {
        mutableStateOf(true)
    }
    val localFocusManager = LocalFocusManager.current


    OutlinedTextField(
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.moveFocus(FocusDirection.Down)
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {password.value = it},
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val visibilityIcon: Painter = painterResource(
                id = if (isHidden.value) R.drawable.visibility_on else R.drawable.visibility_off
            )
            IconButton(onClick = { isHidden.value = !isHidden.value }) {
                Icon(
                    painter = visibilityIcon,
                    contentDescription = if (isHidden.value) "Show Password" else "Hide Password"
                )
            }
        },
        visualTransformation = if (isHidden.value) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun UnderlinedText(
    textLabel: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    onClick: () -> Unit?
){

    val annotatedString = AnnotatedString(
        text = textLabel,
        spanStyle = SpanStyle(
            fontSize = fontSize,
            textDecoration = TextDecoration.Underline,
            //color = Color.Black.copy(alpha = 0.4f)),
            fontWeight = fontWeight,
            color = color),
    )

    Text(
        text = annotatedString,
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

//@Composable
//fun FilledButton(buttonText: String){
//    Button(
//        colors = ButtonDefaults.buttonColors(Color.Black),
//        contentPadding = PaddingValues(horizontal = 50.dp),
//        onClick = { /*TODO*/ }) {
//
//        Text(text = buttonText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//    }
//}

@Composable
fun AppFilledButton(buttonText: String, modifier: Modifier = Modifier) {

    Button(
        onClick = { },
        shape = RoundedCornerShape(10.dp),
        //colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff9a664)),
        colors = ButtonDefaults.buttonColors(containerColor = Orange),
        modifier = modifier
            .width(width = 216.dp)
            .height(height = 37.dp)
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun Staycationdetails(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 308.dp)
            .requiredHeight(height = 157.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 157.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = Color(0xff333333)))
        Image(
            painter = painterResource(id = R.drawable.edit),
            contentDescription = "material-symbols:edit",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 288.dp,
                    y = 4.dp
                )
                .requiredSize(size = 16.dp))
        Text(
            text = "Uno Staycation",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 7.dp,
                    y = 8.dp
                )
                .requiredWidth(width = 110.dp)
                .requiredHeight(height = 18.dp))
        Text(
            text = "#56 38th St. Amang Rodriguez, Pasay City",
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 7.dp,
                    y = 32.dp
                )
                .requiredWidth(width = 138.dp)
                .requiredHeight(height = 34.dp))
        Text(
            text = "Additional Remarks",
            color = Color.White,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 7.dp,
                    y = 72.dp
                )
                .requiredWidth(width = 98.dp)
                .requiredHeight(height = 11.dp))
        Text(
            text = "₱  2,150/night",
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 213.dp,
                    y = 50.dp
                )
                .requiredWidth(width = 92.dp)
                .requiredHeight(height = 16.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 6.dp,
                    y = 89.dp
                )
                .requiredWidth(width = 295.dp)
                .requiredHeight(height = 61.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = Color.White))
        Text(
            text = "In front of 7/11, beside Alfamart. Just let the concierge know that you’ll be staying at uno.",
            color = Color(0xff333333),
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 9.dp,
                    y = 93.dp
                )
                .requiredWidth(width = 289.dp)
                .requiredHeight(height = 53.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomNavigationBar(
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = R.drawable.home_filled,
            unselectedIcon = R.drawable.home_outlined,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Map",
            selectedIcon = R.drawable.map_filled,
            unselectedIcon = R.drawable.map_outlined,
            hasNews = false,
            //badgeCount = 45
        ),
        BottomNavigationItem(
            title = "Inbox",
            selectedIcon = R.drawable.inbox_filled,
            unselectedIcon = R.drawable.inbox_outlined,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = R.drawable.account_filled,
            unselectedIcon = R.drawable.account_outlined,
            hasNews = false,
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onItemSelected(index) // Notify the parent when an item is selected
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if(item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = if (index == selectedItemIndex) {
                                painterResource(id = item.selectedIcon)
                            } else painterResource(id = item.unselectedIcon),
                            contentDescription = item.title,
                            tint = if (index == selectedItemIndex) Orange else Color.Gray
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristBottomNavigationBar(
    touristId: String = "",
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavHostController
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = R.drawable.home_filled,
            unselectedIcon = R.drawable.home_outlined,
            hasNews = false,

            ),
        BottomNavigationItem(
            title = "Itinerary",
            selectedIcon = R.drawable.map_filled,
            unselectedIcon = R.drawable.map_outlined,
            hasNews = false,
            //badgeCount = 45
        ),
        BottomNavigationItem(
            title = "Inbox",
            selectedIcon = R.drawable.inbox_filled,
            unselectedIcon = R.drawable.inbox_outlined,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = R.drawable.account_filled,
            unselectedIcon = R.drawable.account_outlined,
            hasNews = false,
        )
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == "${item.title}" } == true,
                onClick = {
                    navController.navigate(route = "${item.title}/$touristId") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onItemSelected(index)

                    Log.d("Navigation", "Navigating to ${item.title} with touristId: $touristId")
//                    navController.navigate(route = "${item.title}/{touristId}") {
//                        // ... rest of your navigation code
//                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if(item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = if (index == selectedItemIndex) {
                                painterResource(id = item.selectedIcon)
                            } else painterResource(id = item.unselectedIcon),
                            contentDescription = item.title,
                            tint = if (index == selectedItemIndex) Orange else Color.Gray
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun Tag(tag: String, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .padding(top = 3.dp)
            .padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .height(height = 14.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color(0xfff9a664))
                .padding(horizontal = 4.dp)

        ) {
            Text(
                text = tag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
            )
        }
    }
}

@Composable
fun AppReviewsCard(
    totalReviews: Int = 254,
    averageRating: Double = 4.7,
    reviews: List<ReviewUiState>,
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                Icon(
                    modifier = Modifier.height(24.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star"
                )
                Text(
                    text = averageRating.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 3.dp, end = 20.dp)
                )

                Text(
                    text = "$totalReviews reviews",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                //contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
            ) {
                items(reviews) { review ->
                    ReviewCard(
                        review = review,
                        modifier = Modifier
                            .padding(top = 7.dp , end = 12.dp)
                    )
                }
            }
            AppOutlinedButton(
                buttonText = "See all",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ReviewCard(review: ReviewUiState, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .width(width = 135.dp)
            .height(height = 116.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 2.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .height(14.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star",
                    tint = Orange
                )
                Text(
                    text = review.rating.toString(),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
            }
            Row {
                Text(
                    text = review.comment,
                    fontSize = 10.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Bottom
            ) {
                Box {
                    val hostImage = review.touristImage
                    val placeholderImage = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"

                    val imageLoader = rememberImagePainter(
                        data = hostImage ?: placeholderImage,
                        builder = {
                            crossfade(true)
                        }
                    )
                    Image(
                        painter = imageLoader,
                        contentDescription = review.touristName,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape = CircleShape)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                ) {
                    Text(
                        text = review.touristName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = review.reviewDate,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999)
                    )
                }
            }

        }
    }
}

@Composable
fun AppOutlinedButton(buttonText: String, modifier: Modifier = Modifier){

    OutlinedButton(
        onClick = { },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xff999999)),
        modifier = modifier.fillMaxWidth()
    ){
        Text(
            text = buttonText,
            color = Color(0xff999999),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium),
        )

    }

}

@Composable
fun AppExpandingText(
    modifier: Modifier = Modifier,
    longText: String,
    minimizedMaxLines: Int = 4,
    textAlign: TextAlign = TextAlign.Start,
    expandHint: String = "Read more",
    shrinkHint: String = "Read less",
    clickColor: Color = Color(0xFF333333)
) {
    var isExpanded by remember { mutableStateOf(value = false) }
    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(value = null) }
    var adjustedText by remember { mutableStateOf(value = longText) }
    val overflow = textLayoutResultState?.hasVisualOverflow ?: false
    val showOverflow = remember { mutableStateOf(value = false) }
    val readMore = " $expandHint"
    val readLess = " $shrinkHint"

    LaunchedEffect(textLayoutResultState) {
        if (textLayoutResultState == null) return@LaunchedEffect
        if (!isExpanded && overflow) {
            showOverflow.value = true
            val lastCharIndex = textLayoutResultState!!.getLineEnd(lineIndex = minimizedMaxLines - 1)
            adjustedText = longText
                .substring(startIndex = 0, endIndex = lastCharIndex)
                .dropLast(readMore.length)
                .dropLastWhile { it == ' ' || it == '.' }
                .plus("...")
        }
    }
    val annotatedText = buildAnnotatedString {
        if (isExpanded) {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            ) {
                append(longText)
            }
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF666666),
                    // color = Color.Red,
                    fontSize = 14.sp
                )
            ) {
                pushStringAnnotation(tag = "showLess", annotation = "showLess")
                append(readLess)
                addStyle(
                    style = SpanStyle(
                        color = clickColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = longText.length,
                    end = longText.length + readMore.length
                )
                pop()
            }
        } else {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            ) {
                append(adjustedText)
            }
            withStyle(
                style = SpanStyle(
                    // color = MaterialTheme.colors.onSurface,
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            ) {
                if (showOverflow.value) {
                    pushStringAnnotation(tag = "showMore", annotation = "showMore")
                    append(readMore)
                    addStyle(
                        style = SpanStyle(
                            color = clickColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = adjustedText.length,
                        end = adjustedText.length + readMore.length
                    )
                    pop()
                }
            }
        }

    }
    Box(modifier = modifier) {
        ClickableText(
            text = annotatedText,
            style = (
                    MaterialTheme.typography.labelLarge.copy(textAlign = textAlign)
                    ),
            maxLines = if (isExpanded) Int.MAX_VALUE else 4,
            onTextLayout = { textLayoutResultState = it },
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = "showLess",
                    start = offset,
                    end = offset + readLess.length
                ).firstOrNull()?.let {
                    isExpanded = !isExpanded
                }
                annotatedText.getStringAnnotations(
                    tag = "showMore",
                    start = offset,
                    end = offset + readMore.length
                ).firstOrNull()?.let {
                    isExpanded = !isExpanded
                }
            }
        )
    }
}

@Composable
fun AppLocationCard(
    header: String = "Location",
    location: String,
    locationImage: Int,
    locationDescription: String,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = header,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(140.dp)

            ){
                Image(
                    painter = painterResource(id = locationImage),
                    contentDescription = "Location",
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .wrapContentWidth(Alignment.Start)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.location),
                    contentDescription = "Location",
                    tint = Color(0xFF333333),
                    modifier = Modifier.padding(top = 3.dp, end = 10.dp)
                )
                Text(
                    text = location,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    //color = Color(0xFF666666),
                    modifier = Modifier
                )
            }
            Text(
                text = locationDescription,
                fontSize = 10.sp,
                color = Color(0xFF333333),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
            )

        }
    }
}

@Composable
fun AdditionalInformationRow(textInfo: String, onClick: () -> Unit, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() }
    ){
        Row {
            Text(
                text = textInfo,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier
                    .size(19.dp)

            )
        }
    }
}

@Composable
fun AppConfirmAndPayDivider(
    itinerary: String,
    image: Int,
    price: Double,
    unit: String,
    modifier: Modifier = Modifier
) {
    val formattedNumber = NumberFormat.getNumberInstance().format(price)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)

    ){
        ElevatedCard(
            modifier = Modifier
                .height(90.dp)
                .width(150.dp)
                .padding(end = 15.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 10.dp
            )
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                contentScale = ContentScale.FillBounds
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            Text(
                text = itinerary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)) {
                       // append("₱ ${"%.2f".format(price)}")
                        append("₱ $formattedNumber")
                    }
                    withStyle(style = SpanStyle(fontSize = 12.sp)){
                        append(" / $unit")
                    }
                }
            )
        }
    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 10.dp) // 5.dp
    )
}

@Composable
fun AppYourTripRow(
    forCancelBooking: Boolean = false,
    rowLabel: String,
    rowText: String,
    fontColor: Color = Color(0xFF999999),
    onClickEdit: (() -> Unit?)? = null,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = rowLabel,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = rowText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = fontColor
            )
        }
        if (!forCancelBooking) {
            Text(
                text = "Edit",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        if (onClickEdit != null) {
                            onClickEdit()
                        }
                    }
            )
        }
    }
}

@Composable
fun AppDropDownFilter(options: List<String>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = selectedText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
//            modifier = Modifier.padding(top = 15.dp)
        )
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
//                modifier = Modifier
//                    .offset(
//                        x = (-9).dp,
////                        y = (-17).dp
//                    )
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
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


@Preview
@Composable
fun AppComposablesPreview(){

    LoadingScreen(isLoadingCompleted = false, true)

//    val filter = listOf("Relevance", "Recent", "Top rated")
//
//    AppDropDownFilter(options = filter)
}
