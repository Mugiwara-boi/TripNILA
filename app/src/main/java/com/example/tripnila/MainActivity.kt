package com.example.tripnila

import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils.formatDateRange
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.tripnila.data.BookingHistory
import com.example.tripnila.data.Filter
import com.example.tripnila.data.ForInsertUsingCSV
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tag
import com.example.tripnila.data.Tourist
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.BookingHistoryViewModel
import com.example.tripnila.model.BusinessManagerViewModel
import com.example.tripnila.model.ChatViewModel
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.FavoriteViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.HostDashboardViewModel
import com.example.tripnila.model.HostInboxViewModel
import com.example.tripnila.model.HostTourViewModel
import com.example.tripnila.model.InboxViewModel
import com.example.tripnila.model.ItineraryViewModel
import com.example.tripnila.model.InsightViewModel
import com.example.tripnila.model.LocationViewModel
import com.example.tripnila.model.LocationViewModelFactory
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.Person
import com.example.tripnila.model.PersonViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.ProfileViewModel
import com.example.tripnila.model.ReviewViewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.model.StaycationManagerViewModel
import com.example.tripnila.model.StaycationViewModel
import com.example.tripnila.model.TourDetailsViewModel
import com.example.tripnila.model.TourManagerViewModel
import com.example.tripnila.model.VerificationViewModel
import com.example.tripnila.repository.UserRepository
import com.example.tripnila.screens.BookingHistoryCard
import com.example.tripnila.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class MainActivity : ComponentActivity() {

    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                val locationViewModelFactory = LocationViewModelFactory(context = this)
                val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
                val signupViewModel = viewModel(modelClass = SignupViewModel::class.java)
                val preferenceViewModel = viewModel(modelClass = PreferenceViewModel::class.java)
                val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
                val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
                val profileViewModel = viewModel(modelClass = ProfileViewModel::class.java)
                val bookingHistoryViewModel = viewModel(modelClass = BookingHistoryViewModel::class.java)
                val hostDashboardViewModel = viewModel(modelClass = HostDashboardViewModel::class.java)
                val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)
                val locationViewModel = viewModel(
                    factory = locationViewModelFactory,
                    modelClass = LocationViewModel::class.java
                )
                val hostTourViewModel = viewModel(modelClass = HostTourViewModel::class.java)
                val addBusinessViewModel = viewModel(modelClass = AddBusinessViewModel::class.java)
                val staycationManagerViewModel = viewModel(modelClass = StaycationManagerViewModel::class.java)
                val tourManagerViewModel = viewModel(modelClass = TourManagerViewModel::class.java)
                val businessManagerViewModel = viewModel(modelClass = BusinessManagerViewModel::class.java)
                val itineraryViewModel = viewModel(modelClass = ItineraryViewModel::class.java)

                val inboxViewModel = viewModel(modelClass = InboxViewModel::class.java)
                val chatViewModel = viewModel(modelClass = ChatViewModel::class.java)

                val insightViewModel = viewModel(modelClass = InsightViewModel::class.java)
                val tourDetailsViewModel = viewModel(modelClass = TourDetailsViewModel::class.java)
                val reviewViewModel = viewModel(modelClass = ReviewViewModel::class.java)
                val hostInboxViewModel = viewModel(modelClass = HostInboxViewModel::class.java)
                val verificationViewModel = viewModel(modelClass = VerificationViewModel::class.java)
                val favoriteViewModel = viewModel(modelClass = FavoriteViewModel::class.java)

                Navigation(
                    loginViewModel = loginViewModel,
                    signupViewModel = signupViewModel,
                    preferenceViewModel = preferenceViewModel,
                    homeViewModel = homeViewModel,
                    detailViewModel = detailViewModel,
                    profileViewModel = profileViewModel,
                    bookingHistoryViewModel = bookingHistoryViewModel,
                    hostDashboardViewModel = hostDashboardViewModel,
                    addListingViewModel = addListingViewModel,
                    locationViewModelFactory = locationViewModelFactory,
                    hostTourViewModel = hostTourViewModel,
                    addBusinessViewModel = addBusinessViewModel,
                    staycationManagerViewModel = staycationManagerViewModel,
                    tourManagerViewModel = tourManagerViewModel,
                    businessManagerViewModel = businessManagerViewModel,
                    itineraryViewModel = itineraryViewModel,
                    inboxViewModel = inboxViewModel,
                    chatViewModel = chatViewModel,
                    insightViewModel = insightViewModel,
                    tourDetailsViewModel = tourDetailsViewModel,
                    reviewViewModel = reviewViewModel,
                    hostInboxViewModel = hostInboxViewModel,
                    verificationViewModel = verificationViewModel,
                    favoriteViewModel = favoriteViewModel
                )
            }

        }
    }
}

