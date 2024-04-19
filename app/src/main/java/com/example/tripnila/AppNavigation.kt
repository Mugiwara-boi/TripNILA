package com.example.tripnila

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
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
import com.example.tripnila.model.TouristWalletViewModel
import com.example.tripnila.model.VerificationViewModel
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
import com.example.tripnila.screens.BusinessManagerScreen
import com.example.tripnila.screens.CashInScreen
import com.example.tripnila.screens.ChatScreen
import com.example.tripnila.screens.FavoriteScreen
import com.example.tripnila.screens.HomeScreen
import com.example.tripnila.screens.HostChatScreen
import com.example.tripnila.screens.HostDashboardScreen
import com.example.tripnila.screens.HostInboxScreen
import com.example.tripnila.screens.HostProfileScreen
import com.example.tripnila.screens.HostWalletScreen
import com.example.tripnila.screens.InboxScreen
import com.example.tripnila.screens.InsightsScreen
import com.example.tripnila.screens.ItineraryScreen
import com.example.tripnila.screens.LoginScreen
import com.example.tripnila.screens.PreferenceScreen
import com.example.tripnila.screens.ReviewsScreen
import com.example.tripnila.screens.SalesReportScreen
import com.example.tripnila.screens.SignupScreen
import com.example.tripnila.screens.StaycationBookingRescheduleScreen
import com.example.tripnila.screens.StaycationBookingScreen
import com.example.tripnila.screens.StaycationDetailsScreen
import com.example.tripnila.screens.StaycationManagerScreen
import com.example.tripnila.screens.TourBookingScreen
import com.example.tripnila.screens.TourDatesScreen
import com.example.tripnila.screens.TourDetailsScreen
import com.example.tripnila.screens.TourManagerScreen
import com.example.tripnila.screens.TouristProfileScreen
import com.example.tripnila.screens.TouristWalletScreen
import com.example.tripnila.screens.ViewsReportScreen
import com.example.tripnila.screens.WithdrawScreen

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
    Chat,
    BookingHistory,
    EditProfile,
    AccountVerification,
    Preference,
    Favorite,

    StaycationReschedule,

    Review,

    TourDetails,
    TourDates,
    TourBooking,
    TourReschedule,

    TouristWallet,
    CashIn
}

enum class HostRoutes {
    Dashboard,
    HostInbox,
    HostProfile,
    HostChat,

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

    Insights,
    GeneratedReport,
    GeneratedViewsReport,

    StaycationManager,
    BusinessManager,
    TourManager,

    HostWallet,
    Withdraw
}

enum class NestedRoutes {
    Main,
    Login,
    Host,
    Staycation
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
    locationViewModelFactory: LocationViewModelFactory,
    hostTourViewModel: HostTourViewModel,
    addBusinessViewModel: AddBusinessViewModel,
    staycationManagerViewModel: StaycationManagerViewModel,
    tourManagerViewModel: TourManagerViewModel,
    businessManagerViewModel: BusinessManagerViewModel,
    itineraryViewModel: ItineraryViewModel,
    inboxViewModel: InboxViewModel,
    chatViewModel: ChatViewModel,
    insightViewModel: InsightViewModel,
    salesReportViewModel: SalesReportViewModel,
    tourDetailsViewModel: TourDetailsViewModel,
    reviewViewModel: ReviewViewModel,
    hostInboxViewModel: HostInboxViewModel,
    verificationViewModel: VerificationViewModel,
    favoriteViewModel: FavoriteViewModel,
    businessViewsViewModel: BusinessViewsViewModel,
) {

    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Login.name,
    ) {
        authGraph(navController = navController, loginViewModel = loginViewModel, signupViewModel = signupViewModel, preferenceViewModel = preferenceViewModel)
        homeGraph(
            navController = navController, homeViewModel = homeViewModel, detailViewModel = detailViewModel,
            profileViewModel = profileViewModel, loginViewModel = loginViewModel, bookingHistoryViewModel = bookingHistoryViewModel,
            itineraryViewModel = itineraryViewModel, inboxViewModel = inboxViewModel, chatViewModel = chatViewModel, tourDetailsViewModel = tourDetailsViewModel,
            reviewViewModel = reviewViewModel, verificationViewModel = verificationViewModel, favoriteViewModel = favoriteViewModel
        )
        hostGraph(
            navController = navController, hostDashboardViewModel = hostDashboardViewModel, addListingViewModel = addListingViewModel, locationViewModelFactory = locationViewModelFactory,
            hostTourViewModel = hostTourViewModel, addBusinessViewModel = addBusinessViewModel, staycationManagerViewModel = staycationManagerViewModel,
            tourManagerViewModel = tourManagerViewModel, businessManagerViewModel = businessManagerViewModel, insightViewModel = insightViewModel, hostInboxViewModel = hostInboxViewModel,
            chatViewModel = chatViewModel, profileViewModel = profileViewModel, loginViewModel = loginViewModel, salesReportViewModel = salesReportViewModel, businessViewsViewModel = businessViewsViewModel,
        )
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

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel,
    bookingHistoryViewModel: BookingHistoryViewModel,
    itineraryViewModel: ItineraryViewModel,
    inboxViewModel: InboxViewModel,
    chatViewModel: ChatViewModel,
    tourDetailsViewModel: TourDetailsViewModel,
    reviewViewModel: ReviewViewModel,
    verificationViewModel: VerificationViewModel,
    favoriteViewModel: FavoriteViewModel
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
                onNavToTourDetails = { touristId, tourId ->
                    navigateToTourDetails(navController, touristId, tourId)
                },
                navController = navController
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
                },
                onNavToChat = { senderTouristId, receiverTouristId ->
                    navigateToChat(navController, senderTouristId, receiverTouristId)
                },
                onNavToReviewScreen = { touristId, serviceId, serviceType ->
                    navigateToReview(navController, touristId, serviceId, serviceType)
                }
            )
        }

        composable(
            route = HomeRoutes.Review.name + "/{touristId}/{serviceId}/{serviceType}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("serviceType") { type = NavType.StringType }
            )
        ) {entry ->
            ReviewsScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                serviceId = entry.arguments?.getString("serviceId") ?: "",
                serviceType = entry.arguments?.getString("serviceType") ?: "",
                reviewViewModel = reviewViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }



        composable(
            route = HomeRoutes.Booking.name + "/{touristId}/{staycationId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("staycationId") { type = NavType.StringType },
            )
        ) {entry ->
            StaycationBookingScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                staycationId = entry.arguments?.getString("staycationId") ?: "",
                detailViewModel = detailViewModel,
                onBack = {
                    navController.popBackStack()
                },
                touristWalletViewModel = TouristWalletViewModel()

            )
        }

        composable(
            route = HomeRoutes.StaycationReschedule.name + "/{touristId}/{staycationBookingId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("staycationBookingId") { type = NavType.StringType }
            )
        ) {entry ->
            StaycationBookingRescheduleScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                staycationBookingId = entry.arguments?.getString("staycationBookingId") ?: "",
                detailViewModel = detailViewModel,
                onBack = {
                    navController.popBackStack()
                },
                touristWalletViewModel = TouristWalletViewModel()

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
                inboxViewModel = inboxViewModel,
                onNavToChat = { senderTouristId, receiverTouristId ->
                    navigateToChat(navController, senderTouristId, receiverTouristId)
                },
                navController = navController
            )
        }

        composable(
            route = HostRoutes.HostChat.name + "/{senderTouristId}/{receiverTouristId}",
            arguments = listOf(
                navArgument("senderTouristId") { type = NavType.StringType},
                navArgument("receiverTouristId") { type = NavType.StringType},
              //  navArgument("chatId") { type = NavType.StringType},
            )
        ) {entry ->
            HostChatScreen(
                chatViewModel = chatViewModel,
                senderTouristId = entry.arguments?.getString("senderTouristId") ?: "",
               //chatId = entry.arguments?.getString("chatId") ?: "",
                receiverTouristId = entry.arguments?.getString("receiverTouristId") ?: "",
                onBack = {
                    navController.popBackStack()
                }
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
                itineraryViewModel = itineraryViewModel,
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
                touristWalletViewModel = TouristWalletViewModel(),
                detailViewModel = detailViewModel,
                navController = navController,
                onNavToChat = { senderTouristId, receiverTouristId ->
                    navigateToChat(navController, senderTouristId, receiverTouristId)
                },
                onNavToReschedule = { touristId, staycationBookingId ->
                    navigateToReschedule(navController, touristId, staycationBookingId)
                },
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
                verificationViewModel = verificationViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavToProfile = { touristId ->
                    navigateToProfileAndPop(navController, touristId)
                }
            )
        }

        composable(
            route = HomeRoutes.Profile.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType }
            )
        ) {entry ->
            TouristProfileScreen(
                profileViewModel = profileViewModel,
                loginViewModel = loginViewModel,
                touristId = entry.arguments?.getString("touristId") ?: "",
                navController = navController,
                onNavToBookingHistory = { touristId -> navigateToBookingHistory(navController, touristId) },
                onLogout = { navigateToLogin(navController) },
               // onNavToEditProfile = { touristId -> navigateToEditProfile(navController, touristId) },
                onNavToPreference = { touristId -> navigateToEditPreference(navController, touristId) },
                onNavToVerifyAccount = { touristId -> navigateToVerifyAccount(navController, touristId) },
                onNavToHostDashboard = { touristId -> navigateToHost(navController, touristId)},
                onNavToTouristWallet = { touristId -> navigateToTouristWallet(navController, touristId) },
                onNavToFavorite = { touristId -> navigateToFavorite(navController, touristId)}
            )
        }

        composable(
            route = HomeRoutes.TouristWallet.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType},
            )
        ) {entry ->
            TouristWalletScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                touristWalletViewModel = TouristWalletViewModel(),
                onBack = {
                    navController.popBackStack()
                },
                onNavToCashIn = {  touristId ->
                    navigateToCashIn(navController, touristId)
                }
            )
        }

        composable(
            route = HomeRoutes.CashIn.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType},
            )
        ) {entry ->
            CashInScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                touristWalletViewModel = TouristWalletViewModel(),
                onCancel = {
                    navController.popBackStack()
                },

            )
        }

        composable(
            route = HomeRoutes.TourDetails.name + "/{touristId}/{tourId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
                navArgument("tourId") { type = NavType.StringType }
            )
        ) {entry ->
            TourDetailsScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                tourId = entry.arguments?.getString("tourId") ?: "",
                tourDetailsViewModel = tourDetailsViewModel,
                onNavToChooseDate = { touristId ->
                    navigateToTourDates(navController, touristId)
                },
                onNavToChat = { senderTouristId, receiverTouristId ->
                    navigateToChat(navController, senderTouristId, receiverTouristId)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = HomeRoutes.TourDates.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
            )
        ) {entry ->
            TourDatesScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                tourDetailsViewModel = tourDetailsViewModel,
                onNavToTourBookingScreen = { touristId ->
                    navigateToTourBooking(navController, touristId)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = HomeRoutes.Favorite.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
            )
        ) {entry ->
            FavoriteScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                favoriteViewModel = favoriteViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavToDetailScreen = { touristId, staycationId ->
                    navigateToDetail(navController, touristId, staycationId)
                },
                onNavToTourDetails = { touristId, tourId ->
                    navigateToTourDetails(navController, touristId, tourId)
                },
            )
        }

        composable(
            route = HomeRoutes.TourBooking.name + "/{touristId}",
            arguments = listOf(
                navArgument("touristId") { type = NavType.StringType },
            )
        ) {entry ->
            TourBookingScreen(
                touristId = entry.arguments?.getString("touristId") ?: "",
                tourDetailsViewModel = tourDetailsViewModel,
                touristWalletViewModel = TouristWalletViewModel(),
                onBack = {
                    navController.popBackStack()
                },

            )
        }
    }
}

fun NavGraphBuilder.hostGraph(
    navController: NavHostController,
    hostDashboardViewModel: HostDashboardViewModel,
    addListingViewModel: AddListingViewModel,
    locationViewModelFactory: LocationViewModelFactory,
    hostTourViewModel: HostTourViewModel,
    addBusinessViewModel: AddBusinessViewModel,
    staycationManagerViewModel: StaycationManagerViewModel,
    tourManagerViewModel: TourManagerViewModel,
    businessManagerViewModel: BusinessManagerViewModel,
    businessViewsViewModel: BusinessViewsViewModel,
    insightViewModel: InsightViewModel,
    salesReportViewModel: SalesReportViewModel,
    hostInboxViewModel: HostInboxViewModel,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    loginViewModel: LoginViewModel
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
                onNavToAddListing = { serviceId, hostId, listingType ->
                    navigateToAddListing1(navController, serviceId, hostId, listingType)
                },
                onNavToHostTour = { serviceId, hostId,listingType ->
                    navigateToAddListing1(navController, serviceId, hostId, listingType)
                },
                onNavToAddBusiness = { serviceId, hostId, listingType ->
                    navigateToAddListing1(navController, serviceId, hostId, listingType)
                },
                onNavToStaycationManager = { hostId, staycationId ->
                   navigateToStaycationManager(navController, hostId, staycationId)
                },
                onNavToBusinessManager = { hostId, businessId ->
                    navigateToBusinessManager(navController, hostId, businessId)
                },
                onNavToTourManager = { hostId, tourId ->
                    navigateToTourManager(navController, hostId, tourId)
                },
                touristWalletViewModel = TouristWalletViewModel(),
                hostDashboardViewModel = hostDashboardViewModel,
                onNavToInsights = { hostId ->
                    navigateToInsights(navController,hostId)
                },
                onNavToHostWallet = { hostId ->
                    navigateToHostWallet(navController, hostId)
                },
                navController = navController
            )
        }
        composable(
            route = HostRoutes.AddListing1.name + "/{serviceId}/{hostId}/{listingType}",
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType},
                navArgument("hostId") { type = NavType.StringType},
                navArgument("listingType") { type = NavType.StringType },
            )
        ) {entry ->
            AddListingScreen1(
                serviceId = entry.arguments?.getString("serviceId") ?: "",
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
                locationViewModelFactory = locationViewModelFactory,
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
                        "Staycation" -> navigateToAddListingNext(navController, 13, listingType)
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
        composable(
            route = HostRoutes.GeneratedReport.name + "/{reportType}",
            arguments = listOf(navArgument("reportType") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            SalesReportScreen(
                salesReportViewModel = salesReportViewModel,
                reportType = it.arguments?.getString("reportType") ?: "",
                onNavToBack = {
                    onNavToBack(navController)
                }
            )
        }

        composable(
            route = HostRoutes.GeneratedViewsReport.name + "/{reportType}",
            arguments = listOf(navArgument("reportType") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            ViewsReportScreen(
                businessViewsViewModel = businessViewsViewModel,
                reportType = it.arguments?.getString("reportType") ?: "",
                onNavToBack = {
                    onNavToBack(navController)
                }
            )
        }


        composable(
            route = HostRoutes.StaycationManager.name + "/{hostId}/{staycationId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
                navArgument("staycationId") { type = NavType.StringType},
            )
        ) {entry ->
            StaycationManagerScreen(
                staycationManagerViewModel = staycationManagerViewModel,
                hostId = entry.arguments?.getString("hostId") ?: "",
                staycationId = entry.arguments?.getString("staycationId") ?: "",
                onNavToEditStaycation = { serviceId, hostId, listingType ->
                    navigateToEditListing(navController, serviceId, hostId, listingType)
                },
                onNavToDashboard = { touristId ->
                   navigateToHost(navController, touristId)
                },
            )
        }

        composable(
            route = HostRoutes.BusinessManager.name + "/{hostId}/{businessId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
                navArgument("businessId") { type = NavType.StringType},
            )
        ) {entry ->
            BusinessManagerScreen(
                businessManagerViewModel = businessManagerViewModel,
                hostId = entry.arguments?.getString("hostId") ?: "",
                businessId = entry.arguments?.getString("businessId") ?: "",
                onNavToEditBusiness = { serviceId, hostId, listingType ->
                    navigateToEditListing(navController, serviceId, hostId, listingType)
                },
                onNavToDashboard = { touristId ->
                    navigateToHost(navController, touristId)
                },
                businessViewsViewModel = businessViewsViewModel,
                onNavToGeneratedViewsReport = { reportType ->
                    navigateToGeneratedViewsReportScreen(navController, reportType)
                }
            )
        }

        composable(
            route = HostRoutes.TourManager.name + "/{hostId}/{tourId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
                navArgument("tourId") { type = NavType.StringType},
            )
        ) {entry ->
            TourManagerScreen(
                tourManagerViewModel = tourManagerViewModel,
                hostId = entry.arguments?.getString("hostId") ?: "",
                tourId = entry.arguments?.getString("tourId") ?: "",
                onNavToEditTour = { serviceId, hostId, listingType ->
                    navigateToEditListing(navController, serviceId, hostId, listingType)
                },
                onNavToDashboard = { touristId ->
                    navigateToHost(navController, touristId)
                },
            )
        }

        composable(
            route = HostRoutes.HostWallet.name + "/{hostId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
            )
        ) {entry ->
            HostWalletScreen(
                hostId = entry.arguments?.getString("hostId") ?: "",
                touristWalletViewModel = TouristWalletViewModel(),
                onBack = {
                    navController.popBackStack()
                },
                onNavToWithdraw = {  hostId ->
                    navigateToWithdraw(navController, hostId)
                }
            )
        }

        composable(
            route = HostRoutes.Withdraw.name + "/{hostId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
            )
        ) {entry ->
            WithdrawScreen(
                hostId = entry.arguments?.getString("hostId") ?: "",
                touristWalletViewModel = TouristWalletViewModel(),
                onCancel = {
                    navController.popBackStack()
                },

            )
        }

        composable(
            route = HostRoutes.HostInbox.name + "/{hostId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
            )
        ) {entry ->
            HostInboxScreen(
                hostId = entry.arguments?.getString("hostId") ?: "",
                hostInboxViewModel = hostInboxViewModel,
                onNavToChat = { senderTouristId, receiverTouristId ->
                    navigateToChat(navController, senderTouristId, receiverTouristId)
                },
                navController = navController

            )
        }



        composable(
            route = HostRoutes.HostProfile.name + "/{hostId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
            )
        ) {entry ->
            HostProfileScreen(
                hostId = entry.arguments?.getString("hostId") ?: "",
                loginViewModel = loginViewModel,
                profileViewModel = profileViewModel,
                onLogout = {
                    navigateToLogin(navController)
                },
                onNavToHome = { touristId ->
                    navigateToHome(navController, touristId)
                },
                navController = navController

            )
        }

        composable(
            route = HomeRoutes.Chat.name + "/{senderTouristId}/{receiverTouristId}",
            arguments = listOf(
                navArgument("senderTouristId") { type = NavType.StringType},
                navArgument("receiverTouristId") { type = NavType.StringType},
                //  navArgument("chatId") { type = NavType.StringType},
            )
        ) {entry ->
            ChatScreen(
                chatViewModel = chatViewModel,
                senderTouristId = entry.arguments?.getString("senderTouristId") ?: "",
                //chatId = entry.arguments?.getString("chatId") ?: "",
                receiverTouristId = entry.arguments?.getString("receiverTouristId") ?: "",
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = HostRoutes.Insights.name + "/{hostId}",
            arguments = listOf(
                navArgument("hostId") { type = NavType.StringType},
            )
        ) { entry ->
            InsightsScreen(
                hostId = entry.arguments?.getString("hostId") ?: "",
                onBack = {
                    navController.popBackStack()
                },
                insightViewModel = insightViewModel,
                salesReportViewModel = salesReportViewModel,
                onNavToGeneratedReport = { reportType ->
                    navigateToGeneratedReportScreen(navController, reportType)
                }
//                onNavToEditTour = { serviceId, hostId, listingType ->
//                    navigateToEditListing(navController, serviceId, hostId, listingType)
//                },
//                onNavToDashboard = { touristId ->
//                    navigateToHost(navController, touristId)
//                },
            )
        }

//        composable(
//            route = HostRoutes.EditListing1.name + "/{hostId}/{tourId}",
//            arguments = listOf(
//                navArgument("hostId") { type = NavType.StringType},
//                navArgument("tourId") { type = NavType.StringType},
//            )
//        ) {entry ->
//            TourManagerScreen(
//                hostId = entry.arguments?.getString("hostId") ?: "",
//                tourId = entry.arguments?.getString("tourId") ?: "",
//            )
//        }




    }
}



// Navigation functions

private fun navigateToProfileAndPop(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.Profile.name}/$touristId") {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToFavorite(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.Favorite.name}/$touristId") {
        launchSingleTop = true
    }
}

private fun navigateToReview(navController: NavHostController, touristId: String, serviceId: String, serviceType: String) {
    navController.navigate("${HomeRoutes.Review.name}/$touristId/$serviceId/$serviceType") {
        launchSingleTop = true
    }
}

private fun navigateToReschedule(navController: NavHostController, touristId: String, staycationBookingId: String ) {
    navController.navigate("${HomeRoutes.StaycationReschedule.name}/$touristId/$staycationBookingId") {
        launchSingleTop = true
    }
}

private fun navigateToTourDetails(navController: NavHostController, touristId: String, tourId: String ) {
    navController.navigate("${HomeRoutes.TourDetails.name}/$touristId/$tourId") {
        launchSingleTop = true
    }
}
fun navigateToTourDates(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.TourDates.name}/$touristId") {
        launchSingleTop = true
    }
}

private fun navigateToTourBooking(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.TourBooking.name}/$touristId") {
        launchSingleTop = true
    }
}



private fun navigateToTouristWallet(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.TouristWallet.name}/$touristId") {
        launchSingleTop = true
    }
}

private fun navigateToCashIn(navController: NavHostController, touristId: String) {
    navController.navigate("${HomeRoutes.CashIn.name}/$touristId") {
        launchSingleTop = true
    }
}

private fun navigateToHostWallet(navController: NavHostController, hostId: String) {
    navController.navigate("${HostRoutes.HostWallet.name}/$hostId") {
        launchSingleTop = true
    }
}

private fun navigateToWithdraw(navController: NavHostController, hostId: String) {
    navController.navigate("${HostRoutes.Withdraw.name}/$hostId") {
        launchSingleTop = true
    }
}


private fun navigateToEditListing(navController: NavHostController, serviceId: String, hostId: String, listingType: String) {
    navController.navigate("${HostRoutes.AddListing1.name}/$serviceId/$hostId/$listingType") {
        launchSingleTop = true
        popUpTo(0)
    }
}

private fun navigateToChat(navController: NavHostController, senderTouristId: String, receiverTouristId: String) {
    navController.navigate("${HomeRoutes.Chat.name}/$senderTouristId/$receiverTouristId") {
        launchSingleTop = true
    }
}



private fun navigateToStaycationManager(navController: NavHostController, hostId: String, staycationId: String) {
    navController.navigate("${HostRoutes.StaycationManager.name}/$hostId/$staycationId") {
        launchSingleTop = true
    }
}

private fun navigateToBusinessManager(navController: NavHostController, hostId: String, businessId: String) {
    navController.navigate("${HostRoutes.BusinessManager.name}/$hostId/$businessId") {
        launchSingleTop = true
    }
}

private fun navigateToTourManager(navController: NavHostController, hostId: String, tourId: String) {
    navController.navigate("${HostRoutes.TourManager.name}/$hostId/$tourId") {
        launchSingleTop = true
    }
}

private fun navigateToInsights(navController: NavHostController, hostId: String) {
    navController.navigate("${HostRoutes.Insights.name}/$hostId") {
        launchSingleTop = true
    }
}

private fun navigateToBooking(navController: NavHostController, touristId: String, staycationId: String) {
    navController.navigate("${HomeRoutes.Booking.name}/$touristId/$staycationId") {
        launchSingleTop = true
    }
}

private fun navigateToAddListing1(navController: NavHostController, serviceId: String, hostId: String, listingType: String) {
    navController.navigate("${HostRoutes.AddListing1.name}/$serviceId/$hostId/$listingType") {
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
private fun onNavToBack(navController: NavHostController) {
    navController.popBackStack()
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
private fun navigateToGeneratedReportScreen(navController: NavHostController, reportType: String) {
    navController.navigate("${HostRoutes.GeneratedReport.name}/$reportType") {
        launchSingleTop = true
    }
}

private fun navigateToGeneratedViewsReportScreen(navController: NavHostController, reportType: String) {
    navController.navigate("${HostRoutes.GeneratedViewsReport.name}/$reportType") {
        launchSingleTop = true
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

