package com.example.tripnila.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddListingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen14(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){


    val context = LocalContext.current

    var ppHasError = remember { mutableStateOf(false) }
    var npHasError = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val hasSecurityCamera = addListingViewModel?.staycation?.collectAsState()?.value?.hasSecurityCamera
    val hasWeapon = addListingViewModel?.staycation?.collectAsState()?.value?.hasWeapon
    val hasDangerousAnimal = addListingViewModel?.staycation?.collectAsState()?.value?.hasDangerousAnimal
    val staycationAdditionalInformation = remember { mutableStateOf(addListingViewModel?.staycation?.value?.additionalInfo) }
    val staycation = addListingViewModel?.staycation?.collectAsState()?.value
    val hasFirstAid = addListingViewModel?.staycation?.collectAsState()?.value?.hasFirstAid
    val hasFireExit = addListingViewModel?.staycation?.collectAsState()?.value?.hasFireExit
    val hasFireExtinguisher = addListingViewModel?.staycation?.collectAsState()?.value?.hasFireExtinguisher
    val allowPets = addListingViewModel?.staycation?.collectAsState()?.value?.allowPets
    val allowSmoking = addListingViewModel?.staycation?.collectAsState()?.value?.allowSmoking
    val noisePolicy = addListingViewModel?.staycation?.collectAsState()?.value?.noisePolicy
    val isEcoFriendly = addListingViewModel?.staycation?.collectAsState()?.value?.isEcoFriendly
    val alreadySubmitted = addListingViewModel?.isSuccessAddListing?.collectAsState()?.value

    val isLoading = addListingViewModel?.isLoadingAddListing?.collectAsState()?.value

    LaunchedEffect(isLoading){
        if (isLoading != null) {
            if (isLoading == false && alreadySubmitted == true) {
                onNavToNext(listingType)
            }
        }
    }

  //  val selectedCheckboxIndices = remember { mutableStateListOf<Int>() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    onNext = {
                        if (addListingViewModel?.validatePP() != true) {
                            ppHasError.value = true
                        } else if (addListingViewModel?.validateNP() != true) {
                            npHasError.value = true
                        } else if (alreadySubmitted == true) {
                            onNavToNext(listingType)
                        } else {
                            coroutineScope.launch {
                                addListingViewModel.addNewListing(context)
//                                if (alreadySubmitted == true) {
//
//                                }

                            }
                        }
                    },
                    onCancel = {
                        onNavToBack()
                    },
                    isRightButtonLoading = addListingViewModel?.isLoadingAddListing?.collectAsState()?.value == true,
                    enableRightButton = addListingViewModel?.isAgreeToPrivacyPolicy?.collectAsState()?.value == true && addListingViewModel?.isAgreeToNonDiscrimination?.collectAsState()?.value == true
                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        /*SaveAndExitButton(
                            onClick = { *//*TODO*//* }
                        )*/
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
                    item {
                        if (hasSecurityCamera != null) {
                            CheckboxRow(
                                rowLabel = "Security camera(s)",
                                selected = hasSecurityCamera,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setHasSecurityCamera(true)
                                    } else {
                                        addListingViewModel?.setHasSecurityCamera(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (hasWeapon != null) {
                            CheckboxRow(
                                rowLabel = "Weapons",
                                selected = hasWeapon,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setHasWeapon(true)
                                    } else {
                                        addListingViewModel?.setHasWeapon(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (hasDangerousAnimal != null) {
                            CheckboxRow(
                                rowLabel = "Dangerous animals",
                                selected = hasDangerousAnimal,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setHasDangerousAnimal(true)
                                    } else {
                                        addListingViewModel?.setHasDangerousAnimal(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (hasFireExit != null) {
                            CheckboxRow(
                                rowLabel = "Fire exit plan",
                                selected = hasFireExit,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.sethasFireExit(true)
                                    } else {
                                        addListingViewModel?.sethasFireExit(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (hasFireExtinguisher != null) {
                            CheckboxRow(
                                rowLabel = "Fire extinguisher",
                                selected = hasFireExtinguisher,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.sethasFireExtinguisher(true)
                                    } else {
                                        addListingViewModel?.sethasFireExtinguisher(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (hasFirstAid != null) {
                            CheckboxRow(
                                rowLabel = "First aid kit",
                                selected = hasFirstAid,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.sethasFirstAidKit(true)
                                    } else {
                                        addListingViewModel?.sethasFirstAidKit(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        Text(
                            text = "Does your place allow any of these?",
                            color = Color(0xff333333),
                            fontSize = 12.sp
                        )
                    }
                    item {
                        if (allowSmoking != null) {
                            CheckboxRow(
                                rowLabel = "Smoking",
                                selected = allowSmoking,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setAllowSmoking(true)
                                    } else {
                                        addListingViewModel?.setAllowSmoking(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (allowPets != null) {
                            CheckboxRow(
                                rowLabel = "Pets",
                                selected = allowPets,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setAllowPets(true)
                                    } else {
                                        addListingViewModel?.setAllowPets(false)
                                    }
                                }
                            )
                        }
                    }
                    item {
                        if (noisePolicy != null) {
                            CheckboxRow(
                                rowLabel = "Late night noise",
                                selected = noisePolicy,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        addListingViewModel?.setNoisePolicy(true)
                                    } else {
                                        addListingViewModel?.setNoisePolicy(false)
                                    }
                                }
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Does your place have any eco-friendly policies or practices?",
                                color = Color(0xff333333),
                                fontSize = 12.sp,
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth(.7f)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (isEcoFriendly != null) {
                                Checkbox(
                                    checked = isEcoFriendly,
                                    onCheckedChange = { isSelected ->
                                        if (isSelected) {
                                            addListingViewModel.setEcoFriendly(true)
                                        } else {
                                            addListingViewModel.setEcoFriendly(false)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Orange
                                    ),
                                    modifier = Modifier.offset(x = 13.dp)
                                )
                            }
                        }



//                            Spacer(modifier = Modifier.weight(1f))
//                            Checkbox(
//                                checked = selected,
//                                onCheckedChange = { onSelectedChange(it) },
//                                colors = CheckboxDefaults.colors(
//                                    checkedColor = Orange
//                                ),
//                                modifier = Modifier.offset(x = 13.dp)
//                            )
//                        }

                    }
                    item {
                        LongBasicTextFieldWithCharacterLimit(
                            inputText = staycationAdditionalInformation,
                            maxCharacterLimit = 500,
                            onTextChanged = { newText ->
                                addListingViewModel?.setStaycationAdditionalInfo(newText)
                            }
                        )
                    }

//                    items(checkboxLabels) { label ->
//                        CheckboxRow(
//                            rowLabel = label,
//                            selected = checkboxLabels.indexOf(label) in selectedCheckboxIndices,
//                            onSelectedChange = { isSelected ->
//                                if (isSelected) {
//                                    selectedCheckboxIndices.add(checkboxLabels.indexOf(label))
//                                } else {
//                                    selectedCheckboxIndices.remove(checkboxLabels.indexOf(label))
//                                }
//                            },
//                        )
//                    }
                    item {
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    item {
                        addListingViewModel?.isAgreeToPrivacyPolicy?.collectAsState()?.value?.let { isSelected ->
                            AddListingAgreement(
                                clickableText1 = "privacy policy",
                                clickableText2 = "rebooking and cancellation policy",
                                isSelected = isSelected,
                                onCheckedChange = {
                                    addListingViewModel?.onAgreeToPP(it)
                                },
                                hasError = ppHasError.value
                            )
                        }
                    }
                    item {
                        addListingViewModel?.isAgreeToNonDiscrimination?.collectAsState()?.value?.let { isSelected ->
                            AddListingAgreement(
                                clickableText1 = "nondiscrimination policy",
                                clickableText2 = "guest and host fees",
                                isSelected = isSelected,
                                onCheckedChange = {
                                    addListingViewModel?.onAgreeToNP(it)
                                }
                                ,hasError = npHasError.value
                            )
                        }
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
                checkedColor = Orange
            ),
            modifier = Modifier.offset(x = 13.dp)
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddListingAgreement(
    clickableText1: String,
    clickableText2: String,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    hasError: Boolean,
    modifier: Modifier = Modifier
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Orange,
                uncheckedColor = if (hasError) Color.Red else Color(0xFF666666)
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
private fun AddListingScreen14Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen14(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}