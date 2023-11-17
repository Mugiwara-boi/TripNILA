package com.example.tripnila

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.tripnila.data.Tourist
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.repository.UserRepository
import com.example.tripnila.screens.HomeScreen
import com.example.tripnila.screens.LoginScreen
import com.example.tripnila.screens.PreferenceScreen
import com.example.tripnila.screens.SignupScreen
import com.example.tripnila.screens.StaycationBookingScreen
import com.example.tripnila.screens.StaycationDetailsScreen

enum class LoginRoutes {
    Signup,
    SignIn,
    Preference
}

enum class HomeRoutes {
    Home,
    Detail,
    Booking,
    Preference
}

enum class NestedRoutes {
    Main,
    Login
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    preferenceViewModel: PreferenceViewModel,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
) {

    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Login.name
    ) {
        authGraph(navController = navController, loginViewModel = loginViewModel, signupViewModel = signupViewModel, preferenceViewModel = preferenceViewModel)
        homeGraph(navController = navController, homeViewModel = homeViewModel, detailViewModel = detailViewModel)
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

    }
}

// Navigation functions

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

private fun navigateToBooking(navController: NavHostController, touristId: String, staycationId: String) {
    navController.navigate("${HomeRoutes.Booking.name}/$touristId/$staycationId") {
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

