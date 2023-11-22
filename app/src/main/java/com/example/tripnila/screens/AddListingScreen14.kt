package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.AddListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen14(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val checkboxLabels = listOf(
        "Security camera(s)",
        "Weapons",
        "Dangerous animals"
    )
    val selectedCheckboxIndices = remember { mutableStateListOf<Int>() }

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
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Just one last step!",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                    item {
                        Text(
                            text = "Does your place have any of these?",
                            color = Color(0xff333333),
                            fontSize = 12.sp
                        )
                    }
                    items(checkboxLabels) { label ->
                        CheckboxRow(
                            rowLabel = label,
                            selected = checkboxLabels.indexOf(label) in selectedCheckboxIndices,
                            onSelectedChange = { isSelected ->
                                if (isSelected) {
                                    selectedCheckboxIndices.add(checkboxLabels.indexOf(label))
                                } else {
                                    selectedCheckboxIndices.remove(checkboxLabels.indexOf(label))
                                }
                            },
                        )
                    }
                    item {
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    item {
                        AddListingAgreement(
                            clickableText1 = "privacy policy",
                            clickableText2 = "rebooking and cancellation policy",
                        )
                    }
                    item {
                        AddListingAgreement(
                            clickableText1 = "nondiscrimination policy",
                            clickableText2 = "guest and host fees",
                        )
                    }



                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)
            }
        }
    }
}

@Composable
fun CheckboxRow(
    rowLabel: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rowLabel,
            color = Color(0xff666666),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = selected,
            onCheckedChange = { onSelectedChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF333333)
            ),
            modifier = Modifier.offset(x = 13.dp)
        )
    }
}

@Composable
fun AddListingAgreement(
    clickableText1: String,
    clickableText2: String,
    modifier: Modifier = Modifier
){

    var isSelected by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { isSelected = it},
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF333333)
            ),
            modifier = Modifier.offset(x = (-13).dp)
        )
        Text(
            text = buildAnnotatedString {
                append("Do you agree with TripNILA’s ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(clickableText1)
                }
                append(" and ")
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(clickableText2)
                }
                append("?")

            },
            fontSize = 12.sp,
            color = Color(0xff666666),
            modifier = Modifier.offset(x = (-13).dp)
        )


    }
}

@Preview
@Composable
private fun AddListingPreview(){
    AddListingAgreement(
        clickableText1 = "privacy policy",
        clickableText2 = "rebooking and cancellation policy",
    )


}

@Preview
@Composable
private fun AddListingScreen14Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen14(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}