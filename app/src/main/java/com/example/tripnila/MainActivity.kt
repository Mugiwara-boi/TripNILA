package com.example.tripnila

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.BookingHistoryViewModel
import com.example.tripnila.model.BusinessManagerViewModel
import com.example.tripnila.model.BusinessViewsViewModel
import com.example.tripnila.model.ChatViewModel
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.FavoriteViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.HostDashboardViewModel
import com.example.tripnila.model.HostInboxViewModel
import com.example.tripnila.model.HostTourViewModel
import com.example.tripnila.model.InboxViewModel
import com.example.tripnila.model.InsightViewModel
import com.example.tripnila.model.ItineraryViewModel
import com.example.tripnila.model.LocationViewModel
import com.example.tripnila.model.LocationViewModelFactory
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.ProfileViewModel
import com.example.tripnila.model.ReviewViewModel
import com.example.tripnila.model.SalesReportViewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.model.StaycationManagerViewModel
import com.example.tripnila.model.TourDetailsViewModel
import com.example.tripnila.model.TourManagerViewModel
import com.example.tripnila.model.VerificationViewModel
import com.example.tripnila.repository.UserRepository
import com.example.tripnila.ui.theme.MyApplicationTheme


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
                val salesReportViewModel = viewModel(modelClass = SalesReportViewModel::class.java)
                val businessViewsViewModel = viewModel(modelClass = BusinessViewsViewModel::class.java)

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
                    favoriteViewModel = favoriteViewModel,
                    salesReportViewModel = salesReportViewModel,
                    businessViewsViewModel = businessViewsViewModel
                )
            }

        }
    }
}

