package com.example.tripnila

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tripnila.common.Orange
import com.example.tripnila.data.BottomNavigationItem
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.BookingHistoryViewModel
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.HostDashboardViewModel
import com.example.tripnila.model.HostTourViewModel
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.ProfileViewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.screens.AccountVerificationScreen
import com.example.tripnila.screens.AddBusinessScreen10
import com.example.tripnila.screens.AddBusinessScreen4
import com.example.tripnila.screens.AddBusinessScreen8
import com.example.tripnila.screens.AddListingScreen1
import com.example.tripnila.screens.AddListingScreen10
import com.example.tripnila.screens.AddListingScreen11
import com.example.tripnila.screens.AddListingScreen12
import com.example.tripnila.screens.AddListingScreen13
import com.example.tripnila.screens.AddListingScreen14
import com.example.tripnila.screens.AddListingScreen15
import com.example.tripnila.screens.AddListingScreen2
import com.example.tripnila.screens.AddListingScreen3
import com.example.tripnila.screens.AddListingScreen4
import com.example.tripnila.screens.AddListingScreen5
import com.example.tripnila.screens.AddListingScreen6
import com.example.tripnila.screens.AddListingScreen7
import com.example.tripnila.screens.AddListingScreen8
import com.example.tripnila.screens.AddListingScreen9
import com.example.tripnila.screens.AddTourScreen3
import com.example.tripnila.screens.AddTourScreen9
import com.example.tripnila.screens.BookingHistoryScreen
import com.example.tripnila.screens.HomeScreen
import com.example.tripnila.screens.HostDashboardScreen
import com.example.tripnila.screens.InboxScreen
import com.example.tripnila.screens.ItineraryScreen
import com.example.tripnila.screens.LoginScreen
import com.example.tripnila.screens.PreferenceScreen
import com.example.tripnila.screens.ProfileScreen
import com.example.tripnila.screens.SignupScreen
import com.example.tripnila.screens.StaycationBookingScreen
import com.example.tripnila.screens.StaycationDetailsScreen
import com.example.tripnila.screens.TourDatesScreen

enum class LoginRoutes {
    Signup,
    SignIn,
    Preference
}

enum class HomeRoutes {
    Home,
    Detail,
    Booking,
    Itinerary,
    Profile,
    Inbox,
    BookingHistory,
    EditProfile,
    AccountVerification,
    Preference
}

enum class HostRoutes {
    Dashboard,
    AddListing,
    AddListing1,
    AddListing2,
    AddListing3,
    AddTour3,
    AddListing4,
    AddBusiness4,
    AddListing5,
    AddListing6,
    AddListing7,
    AddListing8,
    AddBusiness8,
    AddListing9,
    AddTour9,
    AddListing10,
    AddBusiness10,
    AddListing11,
    AddListing12,
    AddListing13,
    AddListing14,
    AddListing15,

}

enum class NestedRoutes {
    Main,
    Login,
    Host
}


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    preferenceViewModel: PreferenceViewModel,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    profileViewModel: ProfileViewModel,
    bookingHistoryViewModel: BookingHistoryViewModel,
    hostDashboardViewModel: HostDashboardViewModel,
    addListingViewModel: AddListingViewModel,
    hostTourViewModel: HostTourViewModel,
    addBusinessViewModel: AddBusinessViewModel,
) {

    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Login.name,
    ) {
        authGraph(navController = navController, loginViewModel = loginViewModel, signupViewModel = signupViewModel, preferenceViewModel = preferenceViewModel)
        homeGraph(
            navController = navController, homeViewModel = homeViewModel, detailViewModel = detailViewModel,
            profileViewModel = profileViewModel, loginViewModel = loginViewModel, bookingHistoryViewModel = bookingHistoryViewModel
        )
        hostGraph(navController = navController, hostDashboardViewModel = hostDashboardViewModel, addListingViewModel = addListingViewModel, hostTourViewModel = hostTourViewModel, addBusinessViewModel = addBusinessViewModel)
    }
}



fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    preferenceViewModel: PreferenceViewModel
) {
    navigation(startDestination = LoginRoutes.SignIn.name, route = NestedRoutes.Login.name) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(
                onNavToHomeScreen = { touristId -> navigateToHome(navController, touristId) },
                onNavToPreferenceScreen = { touristId -> navigateToPreference(navController, touristId) },
                onNavToSignupScreen = { navigateToSignup(navController) },
                loginViewModel = loginViewModel
            )

        }

        composable(route = LoginRoutes.Signup.name) {
            SignupScreen(
                signupViewModel = signupViewModel,
                onNavToLoginScreen = { navigateToSignIn(navController) }
            )
        }

        composable(
            route = LoginRoutes.Preference.name + "/{touristId}",
            arguments = listOf(navArgument("touristId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {entry ->
            PreferenceScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                preferenceViewModel = preferenceViewModel,
                onNavToHomeScreen = { touristId -> navigateToHome(navController, touristId) }
            )
        }
    }
}

fun NavGraphBuilder.hostGraph(
    navController: NavHostController,
    hostDashboardViewModel: HostDashboardViewModel,
    addListingViewModel: AddListingViewModel,
    hostTourViewModel: HostTourViewModel,
    addBusinessViewModel: AddBusinessViewModel,
) {
    navigation(startDestination = HostRoutes.Dashboard.name, route = NestedRoutes.Host.name) {
        composable(
            route = HostRoutes.Dashboard.name + "/{touristId}",
            arguments = listOf(navArgument("touristId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {entry ->
            HostDashboardScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                onNavToAddListing = { hostId,listingType ->
                    navigateToAddListing1(navController, hostId, listingType)
                },
                onNavToHostTour = { hostId,listingType ->
                    navigateToAddListing1(navController, hostId, listingType)
                },
                onNavToAddBusiness = { hostId,listingType ->
                    navigateToAddListing1(navController, hostId, listingType)
                },
                hostDashboardViewModel = hostDashboardViewModel,
            )
        }
        composable(
            route = HostRoutes.AddListing1.name + "/{hostId}/{listingType}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
                navArgument("listingType") { type = NavType.StringType }
            )
        ) {entry ->
            AddListingScreen1(
                hostId = entry.arguments?.getString("hostId") ?: "",
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
                onNavToCancel = { touristId -> navigateToHost(navController, touristId) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 2, listingType) }
            )
        }
        composable(
            route = HostRoutes.AddListing2.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen2(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
               // onNavToNext = { listingType -> navigateToAddListing3(navController, listingType) },
                onNavToNext = { listingType ->
                    when(listingType) {
                        "Staycation" -> navigateToAddListingNext(navController, 3, listingType)
                        "Tour" -> navigateToAddTour3(navController, listingType)
                        "Business" -> navigateToAddListingNext(navController, 4, listingType)
                    }
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing3.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen3(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
               // onNavToNext = { listingType -> navigateToAddListing4(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 4, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing4.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen4(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
            //    onNavToNext = { listingType -> navigateToAddListing5(navController, listingType) },
                onNavToNext = { listingType ->
                    when(listingType) {
                        "Staycation" -> navigateToAddListingNext(navController, 5, listingType)
                        "Tour" -> navigateToAddListingNext(navController, 6, listingType)
                        "Business" -> navigateToAddBusiness4(navController, listingType)

                    }
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing5.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen5(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
           //     onNavToNext = { listingType -> navigateToAddListing6(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 6, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing6.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen6(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
            //    onNavToNext = { listingType -> navigateToAddListing7(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 7, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing7.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen7(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
            //    onNavToNext = { listingType -> navigateToAddListing8(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 8, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing8.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen8(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
             //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    when (listingType) {
                        "Staycation" -> navigateToAddListingNext(navController, 9, listingType)
                        "Tour" -> navigateToAddListingNext(navController, 10, listingType)
                        "Business" -> navigateToAddBusiness8(navController, listingType)
                    }
                },
                onNavToBack = { navController.popBackStack()
                }
            )
        }
        composable(
            route = HostRoutes.AddListing9.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen9(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 10, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing10.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen10(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    when (listingType) {
                        "Staycation" ->  navigateToAddListingNext(navController, 11, listingType)
                        "Tour" ->  navigateToAddTour9(navController, listingType)
                        "Business" -> navigateToAddBusiness10(navController, listingType)

                    }
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing11.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen11(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    when (listingType) {
                        "Staycation" -> navigateToAddListingNext(navController, 12, listingType)
                        "Tour" -> navigateToAddListingNext(navController, 15, listingType)
                    }
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing12.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen12(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 13, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing13.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen13(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 14, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing14.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen14(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                onNavToNext = { listingType -> navigateToAddListingNext(navController, 15, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddListing15.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddListingScreen15(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addListingViewModel = addListingViewModel,
                hostTourViewModel = hostTourViewModel,
                addBusinessViewModel = addBusinessViewModel,
                onNavToDashboard = { touristId -> navigateToHost(navController, touristId) },
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
              //  onNavToNext = { listingType -> navigateToAddListingNext(navController, 15, listingType) },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddTour3.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddTourScreen3(
                listingType = entry.arguments?.getString("listingType") ?: "",
                hostTourViewModel = hostTourViewModel,
                // onNavToNext = { listingType -> navigateToAddListing3(navController, listingType) },
                onNavToNext = { listingType ->
                    navigateToAddListingNext(navController, 4, listingType)

                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddBusiness4.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddBusinessScreen4(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addBusinessViewModel = addBusinessViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    navigateToAddListingNext(navController, 6, listingType)
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddBusiness8.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddBusinessScreen8(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addBusinessViewModel = addBusinessViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    navigateToAddListingNext(navController, 10, listingType)
                },
                onNavToBack = { navController.popBackStack() }
            )
        }
        composable(
            route = HostRoutes.AddBusiness10.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddBusinessScreen10(
                listingType = entry.arguments?.getString("listingType") ?: "",
                addBusinessViewModel = addBusinessViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    navigateToAddListingNext(navController, 15, listingType)
                },
                onNavToBack = { navController.popBackStack() }
            )
        }

        composable(
            route = HostRoutes.AddTour9.name + "/{listingType}",
            arguments = listOf(navArgument("listingType") { type = NavType.StringType })
        ) {entry ->
            AddTourScreen9(
                listingType = entry.arguments?.getString("listingType") ?: "",
                hostTourViewModel = hostTourViewModel,
                //   onNavToNext = { listingType -> navigateToAddListing9(navController, listingType) },
                onNavToNext = { listingType ->
                    navigateToAddListingNext(navController, 11, listingType)
                },
                onNavToBack = { navController.popBackStack() }
            )
        }


    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    bookingHistoryViewModel: BookingHistoryViewModel,
) {
    navigation(startDestination = HomeRoutes.Home.name, route = NestedRoutes.Main.name) {
        composable(
            route = HomeRoutes.Home.name + "/{touristId}",
            arguments = listOf(navArgument("touristId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {entry ->
            HomeScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                homeViewModel = homeViewModel,
                onNavToDetailScreen = { touristId, staycationId ->
                    navigateToDetail(navController, touristId, staycationId)
                },
                navController = navController,
            )
        }

        composable(
            route = HomeRoutes.Detail.name + "/{touristId}/{staycationId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("staycationId") { type = NavType.StringType }
            )
        ) {entry ->
            StaycationDetailsScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                staycationId = entry.arguments?.getString("staycationId") ?: "",
                detailViewModel = detailViewModel,
                onNavToBooking = { touristId, staycationId ->
                    navigateToBooking(navController, touristId, staycationId)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = HomeRoutes.Booking.name + "/{touristId}/{staycationId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("staycationId") { type = NavType.StringType }
            )
        ) {entry ->
            StaycationBookingScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                staycationId = entry.arguments?.getString("staycationId") ?: "",
                detailViewModel = detailViewModel,
            )
        }

        composable(
            route = HomeRoutes.Inbox.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            InboxScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                navController = navController
            )
        }

        composable(
            route = HomeRoutes.Itinerary.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            ItineraryScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                navController = navController
            )
        }

        composable(
            route = HomeRoutes.BookingHistory.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            BookingHistoryScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                bookingHistoryViewModel = bookingHistoryViewModel,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = HomeRoutes.AccountVerification.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            AccountVerificationScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                //  navController = navController
            )
        }

        composable(
            route = HomeRoutes.Profile.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            ProfileScreen(
                profileViewModel = profileViewModel,
                loginViewModel = loginViewModel,
                touristId = entry.arguments?.getString("touristId") ?: "",
                navController = navController,
                onNavToBookingHistory = { touristId -> navigateToBookingHistory(navController, touristId) },
                onLogout = { navigateToLogin(navController) },
                onNavToEditProfile = { touristId -> navigateToEditProfile(navController, touristId) },
                onNavToPreference = { touristId -> navigateToEditPreference(navController, touristId) },
                onNavToVerifyAccount = { touristId -> navigateToVerifyAccount(navController, touristId) },
                onNavToHostDashboard = { touristId -> navigateToHost(navController, touristId = touristId)}
            )
        }

    }
}

// Navigation functions
private fun navigateToBooking(navController: NavHostController, touristId: String, staycationId: String) {
    navController.navigate("${HomeRoutes.Booking.name}/$touristId/$staycationId") {
        launchSingleTop = true
    }
}

private fun navigateToAddListing1(navController: NavHostController, hostId: String, listingType: String) {
    navController.navigate("${HostRoutes.AddListing1.name}/$hostId/$listingType") {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToAddListingNext(navController: NavHostController, page: Int, listingType: String) {
    navController.navigate("${HostRoutes.AddListing.name}$page/$listingType") {
        launchSingleTop = true
    }
}


private fun navigateToAddBusiness4(navController: NavHostController, listingType: String) {
    navController.navigate("${HostRoutes.AddBusiness4.name}/$listingType") {
        launchSingleTop = true
    }
}
private fun navigateToAddBusiness8(navController: NavHostController, listingType: String) {
    navController.navigate("${HostRoutes.AddBusiness8.name}/$listingType") {
        launchSingleTop = true
    }
}

private fun navigateToAddBusiness10(navController: NavHostController, listingType: String) {
    navController.navigate("${HostRoutes.AddBusiness10.name}/$listingType") {
        launchSingleTop = true
    }
}


private fun navigateToAddTour3(navController: NavHostController, listingType: String) {
    navController.navigate("${HostRoutes.AddTour3.name}/$listingType") {
        launchSingleTop = true
    }
}

private fun navigateToAddTour9(navController: NavHostController, listingType: String) {
    navController.navigate("${HostRoutes.AddTour9.name}/$listingType") {
        launchSingleTop = true
    }
}

private fun navigateToBookingHistory(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.BookingHistory.name}/$touristId") {
        launchSingleTop = true
        //popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}

private fun navigateToEditPreference(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.Preference.name}/$touristId") {
        launchSingleTop = true
        //popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}

private fun navigateToEditProfile(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.EditProfile.name}/$touristId") {
        launchSingleTop = true
        //popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}

private fun navigateToVerifyAccount(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.AccountVerification.name}/$touristId") {
        launchSingleTop = true
        //popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}


private fun navigateToSignup(navController: NavHostController) {
    navController.navigate(LoginRoutes.Signup.name) {
        launchSingleTop = true
        popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}

private fun navigateToSignIn(navController: NavHostController) {
    navController.navigate(LoginRoutes.SignIn.name) {
        launchSingleTop = true
        popUpTo(LoginRoutes.Signup.name) { inclusive = true }
    }
}

private fun navigateToHome(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.Home.name}/$touristId") {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToHost(navController: NavHostController, touristId: String) {
    navController.navigate("${HostRoutes.Dashboard.name}/$touristId") {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToPreference(navController: NavHostController, touristId: String) {
    navController.navigate("${LoginRoutes.Preference.name}/$touristId") {
        launchSingleTop = true
        popUpTo(LoginRoutes.SignIn.name) { inclusive = true }
    }
}


private fun navigateToDetail(navController: NavHostController, touristId: String, staycationId: String) {
    navController.navigate("${HomeRoutes.Detail.name}/$touristId/$staycationId") {
        launchSingleTop = true
    }
}



private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(NestedRoutes.Login.name) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

//            val currentUserState: State<Tourist?>? = loginViewModel?.currentUser?.collectAsState()
//            val currentUser: Tourist? = currentUserState?.value

//                onNavToHomeScreen = { userId ->
//                    if (currentUser?.preferences?.isNotEmpty() == true) {
//                        // Navigate to HomeScreen if user has preferences
//                        navigateToHome(navController, userId)
//                    } else {
//                        // Navigate to Preference screen if user has no preferences
//                        navigateToPreference(navController, userId)
//                    } },


//enum class LoginRoutes {
//    Signup,
//    Signin
//}
//
//enum class HomeRoutes {
//    Home,
//    Detail
//}
//
//enum class NestedRoutes {
//    Main,
//    Login
//}
//
//
//@Composable
//fun AppNavigation(
//    navController: NavHostController = rememberNavController(),
//    loginViewModel: LoginViewModel,
//    signupViewModel: SignupViewModel
//) {
//
//    NavHost(
//        navController = navController,
//        startDestination = NestedRoutes.Main.name
//    ) {
//        authGraph(navController, loginViewModel)
//    }
//}


//fun NavGraphBuilder.authGraph(
//    navController: NavHostController,
//    loginViewModel: LoginViewModel,
//    signupViewModel: SignupViewModel
//) {
//    navigation(startDestination = LoginRoutes.SignIn.name, route = NestedRoutes.Login.name) {
//        composable(route = LoginRoutes.SignIn.name) {
//            LoginScreen(
//                onNavToHomeScreen = { navigateToHome(navController) },
//                onNavToSignupScreen = { navigateToSignup(navController) },
//                loginViewModel = loginViewModel
//            )
//        }
//
//        composable(route = LoginRoutes.Signup.name) {
//            SignupScreen(
//                signupViewModel = signupViewModel,
//                onNavToLoginScreen = { navigateToSignIn(navController) }
//            )
//        }
//    }
//}

//fun NavGraphBuilder.homeGraph(
//    navController: NavHostController,
//    repository: UserRepository
//) {
//    navigation(startDestination = HomeRoutes.Home.name, route = NestedRoutes.Main.name) {
//        composable(HomeRoutes.Home.name) {
//            val currentUser = repository.getCurrentUser()
//            if (currentUser?.preference != null) {
//                // Navigate to HomeScreen if user has preferences
//                HomeScreen()
//            } else {
//                // Navigate to Preference screen if user has no preferences
//                navigateToPreference(navController)
//            }
//        }
//
//        composable(HomeRoutes.Preference.name) {
//            // This is where you define the content of the Preference screen
//            // You can replace this with the actual composable for your Preference screen
////            Text("Preference Screen")
//        }
//    }
//}

//
//fun NavGraphBuilder.authGraph(
//    navController: NavHostController,
//    loginViewModel: LoginViewModel,
//    signupViewModel: SignupViewModel,
//) {
//    navigation(
//        startDestination = LoginRoutes.Signin.name,
//        route = NestedRoutes.Login.name
//    ) {
//        composable(route = LoginRoutes.Signin.name) {
//            LoginScreen(
//            onNavToHomePage = {
//                navController.navigate(NestedRoutes.Main.name) {
//                    launchSingleTop = true
//                    popUpTo(route = LoginRoutes.Signin.name) {
//                        inclusive = true
//                    }
//                }
//            },
//            loginViewModel = loginViewModel
//
//            ) {
//                navController.navigate(LoginRoutes.Signup.name) {
//                    launchSingleTop = true
//                    popUpTo(LoginRoutes.SignIn.name) {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//
//        composable(route = LoginRoutes.Signup.name) {
//            SignUpScreen(onNavToHomePage = {
//                navController.navigate(NestedRoutes.Main.name) {
//                    popUpTo(LoginRoutes.Signup.name) {
//                        inclusive = true
//                    }
//                }
//            },
//                loginViewModel = loginViewModel
//            ) {
//                navController.navigate(LoginRoutes.SignIn.name)
//            }
//
//        }
//
//    }

