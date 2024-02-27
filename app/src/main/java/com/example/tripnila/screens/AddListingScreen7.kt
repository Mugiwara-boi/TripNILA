package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.PropertyDescription
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen7(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    hostTourViewModel: HostTourViewModel? = null,
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val header = if (listingType == "Staycation") {
        "Tell us what your staycation has to offer"
    } else if (listingType == "Business"){
        "Tell us what your business has to offer"
    }
    else {
        "Tell us about your tour has to offer"
    }

    var selectedAmenities by when (listingType) {
        "Staycation" -> remember { mutableStateOf(addListingViewModel?.staycation?.value?.amenities?.map { it.amenityName }) }
        "Tour" -> remember { mutableStateOf(hostTourViewModel?.tour?.value?.offers?.map { it.typeOfOffer }) }
        "Business" -> remember { mutableStateOf(addBusinessViewModel?.business?.value?.amenities?.map { it.amenityName }) }
        else -> throw IllegalStateException("Unknown")
    }

//    var selectedAmenities by remember {
//        mutableStateOf(addListingViewModel?.staycation?.value?.amenities?.map { it.amenityName })
//    }


    val offers = if (listingType == "Staycation") {
        listOf(
            PropertyDescription(
                icon = R.drawable.wifi,
                label = "Wifi"
            ),
            PropertyDescription(
                icon = R.drawable.tv,
                label = "TV"
            ),
            PropertyDescription(
                icon = R.drawable.kitchen,
                label = "Kitchen"
            ),
            PropertyDescription(
                icon = R.drawable.washing_machine,
                label = "Washing machine"
            ),
            PropertyDescription(
                icon = R.drawable.workspace,
                label = "Dedicated workspace"
            )
        )
    }
    else if (listingType == "Business") {
        listOf(
            PropertyDescription(
                icon = R.drawable.wifi,
                label = "Wifi"
            ),
            PropertyDescription(
                icon = R.drawable.tv,
                label = "Food"
            ),
            PropertyDescription(
                icon = R.drawable.kitchen,
                label = "Drinks"
            ),
            PropertyDescription(
                icon = R.drawable.washing_machine,
                label = "Nature trip"
            ),
            PropertyDescription(
                icon = R.drawable.workspace,
                label = "Dedicated workspace"
            )
        )
    }
    else {
        listOf(
            PropertyDescription(
                icon = R.drawable.house,
                label = "Food"
            ),
            PropertyDescription(
                icon = R.drawable.house,
                label = "Souvenir"
            ),
            PropertyDescription(
                icon = R.drawable.house,
                label = "Transportation"
            ),
            PropertyDescription(
                icon = R.drawable.house,
                label = "Drink"
            ),
        )
    }

    val amenities = listOf(
        PropertyDescription(
            icon = R.drawable.bigger_pool,
            label = "Pool"
        ),
        PropertyDescription(
            icon = R.drawable.gym,
            label = "Gym equipment"
        ),
        PropertyDescription(
            icon = R.drawable.hot_tub,
            label = "Hot tub"
        ),
    )


    val views = listOf(
        PropertyDescription(
            icon = R.drawable.condominium,
            label = "City view"
        ),
    )

    val numOffersRows = (offers.size + 1) / 2
    val numAmenitiesRows = (amenities.size + 1) / 2
    val numViewsRows = (views.size + 1) / 2
    var lastIndex: Int

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
                    enableRightButton =  when (listingType) {
                        "Staycation" -> addListingViewModel?.staycation?.collectAsState()?.value?.amenities?.isNotEmpty() == true
                        "Tour" -> hostTourViewModel?.tour?.collectAsState()?.value?.offers?.isNotEmpty() == true
                        "Business" -> addBusinessViewModel?.business?.collectAsState()?.value?.amenities?.isNotEmpty() == true
                        else -> throw IllegalStateException("Unknown")
                    }

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
                 //   contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    item {
                        Text(
                            text = header,
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)

                        )
                    }

                    if (listingType != "Tour") {
                        items(numOffersRows) { row ->
                            lastIndex = 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = minOf(startIndex + 2, offers.size)

                                for (i in startIndex until endIndex) {
                                    val offer = offers[i]
                                    PropertyTypeCard(
                                        icon = offer.icon,
                                        label = offer.label,
                                        selected = selectedAmenities?.contains(offer.label) == true  ,
                                        onSelectedChange = { isSelected ->
                                            if (isSelected) {
                                                selectedAmenities = selectedAmenities?.plus(offer.label)

                                                when (listingType) {
                                                    "Staycation" -> {
                                                        addListingViewModel?.addStaycationAmenity(offer.label)

                                                    }
                                                    "Business" -> {
                                                        addBusinessViewModel?.addBusinessAmenity(offer.label)
                                                    }
                                                    else -> throw IllegalStateException("Unknown")
                                                }

                                            } else {
                                                selectedAmenities = selectedAmenities?.minus(offer.label)

                                                when (listingType) {
                                                    "Staycation" -> addListingViewModel?.removeStaycationAmenity(offer.label)
                                                    "Business" -> addBusinessViewModel?.removeBusinessAmenity(offer.label)
                                                    else -> throw IllegalStateException("Unknown")
                                                }
                                            }

                                        }
                                    )
                                    lastIndex = i
                                }
                                if (lastIndex == offers.size - 1 && !(offers.size % 2 == 0)) {
                                    AddMoreAmenity()
                                }
                            }
                        }

                        if (offers.size % 2 == 0) {
                            item {
                                AddMoreAmenity()
                            }
                        }
                    }

                    if (listingType == "Staycation") {
                        item {
                            Text(
                                text = "Do you have any standout amenities?",
                                color = Color(0xff333333),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    // .width(300.dp)
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)

                            )
                        }

                        items(numAmenitiesRows) { row ->
                            lastIndex = 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = minOf(startIndex + 2, amenities.size)

                                for (i in startIndex until endIndex) {
                                    val amenity = amenities[i]
                                    PropertyTypeCard(
                                        icon = amenity.icon,
                                        label = amenity.label,
                                        selected = selectedAmenities?.contains(amenity.label) == true,
                                        onSelectedChange = { isSelected ->
                                            if (isSelected) {
                                                selectedAmenities = selectedAmenities?.plus(amenity.label)

                                                when (listingType) {
                                                    "Staycation" -> addListingViewModel?.addStaycationAmenity(amenity.label)
                                                    "Business" -> addBusinessViewModel?.addBusinessAmenity(amenity.label)
                                                    else -> throw IllegalStateException("Unknown")
                                                }

                                            } else {
                                                selectedAmenities = selectedAmenities?.minus(amenity.label)

                                                when (listingType) {
                                                    "Staycation" -> addListingViewModel?.removeStaycationAmenity(amenity.label)
                                                    "Business" -> addBusinessViewModel?.removeBusinessAmenity(amenity.label)
                                                    else -> throw IllegalStateException("Unknown")
                                                }

                                            }
                                         //   addListingViewModel?.setStaycationAmenities(selectedAmenities.toList())
                                        }
                                    )
                                    lastIndex = i
                                }
                                if (lastIndex == amenities.size - 1 && !(amenities.size % 2 == 0)) {
                                    AddMoreAmenity()
                                }
                            }
                        }

                        if (amenities.size % 2 == 0) {
                            item {
                                AddMoreAmenity()
                            }
                        }
                    }

                    if (listingType != "Tour") {
                        item {
                            Text(
                                text = "Do you have any scenic view?",
                                color = Color(0xff333333),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    // .width(300.dp)
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)

                            )
                        }
                        items(numViewsRows) { row ->
                            lastIndex = 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = minOf(startIndex + 2, views.size)

                                for (i in startIndex until endIndex) {
                                    val view = views[i]
                                    PropertyTypeCard(
                                        icon = view.icon,
                                        label = view.label,
                                        selected = selectedAmenities?.contains(view.label) == true,
                                        onSelectedChange = { isSelected ->
                                            if (isSelected) {
                                                selectedAmenities = selectedAmenities?.plus(view.label)

                                                when (listingType) {
                                                    "Staycation" -> addListingViewModel?.addStaycationAmenity(view.label)
                                                    "Business" -> addBusinessViewModel?.addBusinessAmenity(view.label)
                                                    else -> throw IllegalStateException("Unknown")
                                                }
                                            } else {
                                                selectedAmenities = selectedAmenities?.minus(view.label)

                                                when (listingType) {
                                                    "Staycation" -> addListingViewModel?.removeStaycationAmenity(view.label)
                                                    "Business" -> addBusinessViewModel?.removeBusinessAmenity(view.label)
                                                    else -> throw IllegalStateException("Unknown")
                                                }
                                            }
                                        }
                                    )
                                    lastIndex = i
                                }
                                if (lastIndex == views.size - 1 && !(views.size % 2 == 0)) {
                                    AddMoreAmenity()
                                }
                            }
                        }

                        if (views.size % 2 == 0) {
                            item {
                                AddMoreAmenity()
                            }
                        }
                    }

                    if (listingType == "Tour") {
                        items(numOffersRows) { row ->
                            lastIndex = 0
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = minOf(startIndex + 2, offers.size)

                                for (i in startIndex until endIndex) {
                                    val offer = offers[i]
                                    TourOfferCard(
                                        inputText = remember {
                                            mutableStateOf(hostTourViewModel?.tour?.value?.offers?.find { it.typeOfOffer == offer.label }?.offer ?: "")
                                        },
                                        icon = offer.icon,
                                        label = offer.label,
                                        selected = selectedAmenities?.contains(offer.label) == true,
                                        onSelectedChange = { isSelected ->
                                            if (isSelected) {
                                                selectedAmenities = selectedAmenities?.plus(offer.label)

                                            } else {
                                                selectedAmenities = selectedAmenities?.minus(offer.label)
                                                hostTourViewModel?.removeOffer(offer.label)
                                            }
                                        },
                                        onValueChange = { offerText ->
                                            if (selectedAmenities?.contains(offer.label) == true ) {
                                                hostTourViewModel?.addOffer(offer.label, offerText)
                                            }
                                        }
                                    )
                                    lastIndex = i
                                }
                                if (lastIndex == offers.size - 1 && !(offers.size % 2 == 0)) {
                                    AddMoreAmenity(modifier = Modifier.height(100.dp))
                                }
                            }
                        }

                        if (offers.size % 2 == 0) {
                            item {
                                AddMoreAmenity(modifier = Modifier.height(100.dp))
                            }
                        }
                    }


                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
            }
        }
    }
}

@Composable
fun AddMoreAmenity(modifier: Modifier = Modifier){

    val stroke = Stroke(
        width = 8f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = modifier
            .width(165.dp)
            .height(height = 86.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .drawBehind {
                drawRoundRect(
                    color = Orange,
                    style = stroke,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .clickable { /*TODO*/ }
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Orange,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 3.dp)
            )
            Text(
                text = "Add more",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Orange
            )

        }
    }
}

@Composable
fun TourOfferCard(
    inputText: MutableState<String>,
   //isEnable: Boolean,
    icon: Int,
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White
    val localFocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .width(width = 165.dp)
            .height(height = 100.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(10.dp)
            )
            .background(containerColor)
            .clickable {
                onSelectedChange(!selected)
                if (!selected) {
                    focusRequester.requestFocus()
                } else {
                    localFocusManager.clearFocus()
                }

            } // Toggle the selection state
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 6.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = label,
                tint = Color(0xff333333)
            )
            Text(
                text = label,
                color = Color(0xff333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )

            Column {
                BasicTextField(
                    value = inputText.value,
                    onValueChange = {
                        inputText.value = it

                    },
                    textStyle = TextStyle(fontSize = 12.sp, color =  if (selected) Color(0xFF333333) else Color(0xFF6B6B6B)),
                    keyboardOptions =  KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        localFocusManager.clearFocus()
                        onValueChange(inputText.value)
                    },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 0.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row {
                                if (inputText.value.isEmpty()) {
                                    Text(
                                        text = "What type of ...",
                                        fontSize = 12.sp,
                                        color = Color(0xFF6B6B6B),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            innerTextField()
                        }
                    },
                   // enabled = isEnable,
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .focusRequester(focusRequester)

                )
                Divider(
                    color = if (selected) Color(0xFF333333) else Color(0xFF6B6B6B),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-5).dp)
                )
            }

        }
    }
}


//            BasicTextFieldWithUnderline(
//                placeholder = "What type of ...", //"What type of ${label.lowercase()}?"
//                isEnable = selected,

//                modifier = Modifier.focusRequester(focusRequester),
//               // color = containerColor,
//            )

@Composable
fun BasicTextFieldWithUnderline(
    placeholder: String,
    isEnable: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    //color: Color,
    modifier: Modifier = Modifier
){
    var inputText by remember {
        mutableStateOf("")
    }
    var isFocused by remember { mutableStateOf(false) }

    Column {
        BasicTextField(
            value = inputText,
            onValueChange = {
                inputText = it
            },
            textStyle = TextStyle(fontSize = 12.sp, color =  if (isFocused) Color(0xFF333333) else Color(0xFF6B6B6B)),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 0.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row {
                        if (inputText.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 12.sp,
                                color = Color(0xFF6B6B6B),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    innerTextField()
                }
            },
            enabled = isEnable,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .height(30.dp)
//                .onFocusChanged {
//                    isFocused = it.isFocused
//                }
        )
        Divider(
            color = if (isFocused) Color(0xFF333333) else Color(0xFF6B6B6B),
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-5).dp)
        )
    }
}


@Preview
@Composable
private fun AddListingScreen7Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)
    val hostTourViewModel = viewModel(modelClass = HostTourViewModel::class.java)

    AddListingScreen7(
        listingType = "Tour",
        hostTourViewModel = hostTourViewModel,
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}