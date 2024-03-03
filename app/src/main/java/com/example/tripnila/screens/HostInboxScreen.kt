package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.tripnila.common.HostBottomNavigationBar
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.model.HostInboxViewModel
import com.example.tripnila.model.InboxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostInboxScreen(
    hostId: String = "",
    hostInboxViewModel: HostInboxViewModel,
    onNavToChat: (String, String) -> Unit,
    navController: NavHostController? = null
){


    LaunchedEffect(hostId) {
        hostInboxViewModel.setCurrentUser(hostId.substring(0, 5))



        Log.d("HostId" , hostId)
    }
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }

    val lazyPagingItems = hostInboxViewModel.inboxPagingData.collectAsLazyPagingItems()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(headerText = "Inbox")
            },
            bottomBar = {
                navController?.let {
                    HostBottomNavigationBar(
                        hostId = hostId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
            }
        ) {
            Divider()

            if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    lazyPagingItems.let { items ->
                        items(items) { inboxItem ->
                            if (inboxItem != null) {
                                InboxItem(
                                    inbox = inboxItem,
                                    currentUser = hostId.substring(0, 5),
                                    onClick = { receiverId ->
                                        onNavToChat(hostId.substring(0, 5), receiverId)
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}
