package com.example.tripnila.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: MutableStateFlow<LoginUiState> = _loginUiState

    private val _currentUser = MutableStateFlow<Tourist?>(null)
    val currentUser: StateFlow<Tourist?> get() = _currentUser.asStateFlow()

    fun setUsername(newUsername: String) {
        _loginUiState.value = _loginUiState.value.copy(username = newUsername)
    }

    fun updateIsSuccessLogin(value: Boolean?) {
        val currentState = _loginUiState.value
        val updatedState = currentState.copy(isSuccessLogin = value)
        _loginUiState.value = updatedState
    }

    fun setPassword(newPassword: String) {
        _loginUiState.value = _loginUiState.value.copy(password = newPassword)
    }

    private fun validateLoginForm(): Boolean {
        return _loginUiState.value.username.isNotBlank() && _loginUiState.value.password.isNotBlank()
    }

    fun isEmail(text: String): Boolean {
        return "@" in text
        Log.d("IsEmail", "Result ${"@" in text}")
    }

    fun loginUser(context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting login with username: ${_loginUiState.value.username}")

                if (!validateLoginForm()) {
                    _loginUiState.value = _loginUiState.value.copy(
                        loginError = "Fields cannot be empty",
                        isSuccessLogin = false
                    )
                    return@launch
                }

                _loginUiState.value = _loginUiState.value.copy(isLoading = true, loginError = null)

                Log.d("LoginViewModel", "Login result2: ${_loginUiState.value}")
                if(isEmail(_loginUiState.value.username)){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(_loginUiState.value.username, _loginUiState.value.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user != null && user.isEmailVerified) {
                                    // Email is verified, proceed with further actions
                                    Log.d("LoginViewModel", "Login successful")

                                    viewModelScope.launch {
                                        try {
                                            val success = repository.loginUser(
                                                user.uid,
                                                _loginUiState.value.password,
                                                true
                                            )


                                            if (success) {
                                                // Show success message using Toast
                                                Toast.makeText(
                                                    context,
                                                    "Login successful",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                _loginUiState.value = _loginUiState.value.copy(isSuccessLogin = success)
                                                _currentUser.value = repository.getCurrentUser()
                                                // Proceed with further actions after successful login
                                            } else {
                                                // Show error message using Toast
                                                Toast.makeText(
                                                    context,
                                                    "Login failed: Incorrect username or password",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } catch (e: Exception) {
                                            // Handle login error
                                            Log.e("LoginViewModel", "Login error: ${e.message}", e)
                                            Toast.makeText(
                                                context,
                                                "Login failed: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    // Email is not verified
                                    Log.d("LoginViewModel", "Email is not verified")
                                    Toast.makeText(
                                        context,
                                        "Email is not verified. Please verify your email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // You may choose to sign out the user if the email is not verified
                                    FirebaseAuth.getInstance().signOut()
                                }
                            } else {
                                // Login failed
                                Log.d("LoginViewModel", "Login failed: ${task.exception?.message}")
                                Toast.makeText(
                                    context,
                                    "Login failed: Incorrect username or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                else {
                    viewModelScope.launch {
                        try {
                            val success = repository.loginUser(
                                _loginUiState.value.username,
                                _loginUiState.value.password,
                                false
                            )


                            if (success) {
                                // Show success message using Toast
                                Toast.makeText(
                                    context,
                                    "Login successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                _loginUiState.value = _loginUiState.value.copy(isSuccessLogin = success)
                                _currentUser.value = repository.getCurrentUser()
                                // Proceed with further actions after successful login
                            } else {
                                // Show error message using Toast
                                Toast.makeText(
                                    context,
                                    "Login failed: Incorrect username or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            // Handle login error
                            Log.e("LoginViewModel", "Login error: ${e.message}", e)
                            Toast.makeText(
                                context,
                                "Login failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                /*val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser != null && firebaseUser.isEmailVerified) {
                    // Show success message using Toast
                    Toast.makeText(
                        context,
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = repository.loginUser(
                        _loginUiState.value.username,
                        _loginUiState.value.password
                    )
                    _loginUiState.value = _loginUiState.value.copy(isSuccessLogin = user)
                } else if(firebaseUser != null){
                    // Show error message using Toast
                    if (!firebaseUser.isEmailVerified) {
                        Toast.makeText(
                            context,
                            "Email is not verified. Please verify your email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Login failed: Incorrect username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }*/



            } catch (e: Exception) {
                _loginUiState.value = _loginUiState.value.copy(loginError = e.localizedMessage)
                e.printStackTrace()


            } finally {
                _loginUiState.value = _loginUiState.value.copy(isLoading = false)
                Log.d("LoginViewModel", "Login result3: ${_loginUiState.value}") //_loginUiState.value.isSuccessLogin
            }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean? = null,
    val loginError: String? = null,
)

//class LoginViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
//
//    var loginUiState by mutableStateOf(LoginUiState())
//        private set
//
//    fun setUsername(newUsername: String) {
//        loginUiState = loginUiState.copy(username = newUsername)
//    }
//
//    fun setPassword(newPassword: String) {
//        loginUiState = loginUiState.copy(password = newPassword)
//    }
//
//    private fun validateLoginForm() =
//        loginUiState.username.isNotBlank() &&
//                loginUiState.password.isNotBlank()
//
//    fun loginUser() = viewModelScope.launch {
//        try {
//            if (!validateLoginForm()) {
//                loginUiState = loginUiState.copy(loginError = "Fields cannot be empty", isSuccessLogin = false)
//                return@launch
//            }
//            loginUiState = loginUiState.copy(isLoading = true, loginError = null)
//            val user = repository.loginUser(loginUiState.username, loginUiState.password)
//            loginUiState = loginUiState.copy(isSuccessLogin = user != null)
//
//        } catch (e: Exception) {
//            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
//            e.printStackTrace()
//        } finally {
//            loginUiState = loginUiState.copy(isLoading = false)
//        }
//    }
//}



//    suspend fun hasCurrentUserPreferences(): Boolean = suspendCoroutine { continuation ->
//        val currentUser = repository.getCurrentUser()
//
//
//        if (currentUser != null) {
//            viewModelScope.launch {
//                try {
//                    val preferences = repository.getTouristPreferences(currentUser.touristId)
//                    Log.d("LoginViewModel", "$currentUser")
//                    continuation.resume(preferences.isNotEmpty())
//                } catch (e: Exception) {
//                    // Handle exceptions if needed
//                    e.printStackTrace()
//                    continuation.resume(false)
//                }
//            }
//        } else {
//            continuation.resume(false)
//        }
//    }


//    fun loginUser() {
//
//        if (!validateLoginForm()) {
//            loginResult.value = OperationResult.Error("Fields cannot be empty")
//            Log.d("RESULT", loginResult.value.toString())
//            return
//        }
//
//        isLoading.value = true // Set isLoading to true at the start of login process
//
//        val username = loginUiState.username
//        val password = loginUiState.password
//
//        viewModelScope.launch {
//            try {
//                val result = repository.loginUser(username, password)
//                loginResult.value = result
//
//                Log.d("RESULT", loginResult.value.toString())
//
//            } finally {
//                isLoading.value = false // Set isLoading to false after login process completes
//            }
//        }
//    }
//}
//    fun loginUser() {
//        // Retrieve the username and password from LoginUiState and proceed with the authentication
//        val username = loginUiState.username
//        val password = loginUiState.password
//
//        Log.d("LoginViewModel", "Username: $username, Password: $password")
//
//        viewModelScope.launch {
//            loginResult.value = repository.loginUser(username, password)
//            //Log.d("result", loginResult.value.toString())
//        }
//    }


//class LoginViewModel(
//    private val repository: UserRepository = UserRepository()
//) : ViewModel() {
//
//    var loginUiState by mutableStateOf(LoginUiState())
//        private set
//
//    fun setUsername(newUsername: String) {
//        loginUiState = loginUiState.copy(username = newUsername)
//    }
//
//    fun setPassword(newPassword: String) {
//        loginUiState = loginUiState.copy(password = newPassword)
//    }
//
//    private fun validateLoginForm() =
//        loginUiState.username.isNotBlank() &&
//                loginUiState.password.isNotBlank()
//
//    fun loginUser(context: Context) = viewModelScope.launch {
//            try {
//                if (!validateLoginForm()) {
//                    // If form validation fails, display a toast
//                    Toast.makeText(context, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
//                    return@launch
//                }
//
//                // Update the UI state to reflect loading
//                loginUiState = loginUiState.copy(isLoading = true, loginError = null)
//
//                // Simulated login by checking credentials
//                val user = repository.getUserByCredentials(loginUiState.username, loginUiState.password)
//
//                // Handle the login result
//                loginUiState = loginUiState.copy(isSuccessLogin = user != null)
//
//                // Display success message or failure message using Toast
//                val message = if (user != null) "Login successful" else "Login failed"
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//
//            } catch (e: Exception) {
//                loginUiState = loginUiState.copy(loginError = e.localizedMessage)
//                e.printStackTrace()
//            } finally {
//                loginUiState = loginUiState.copy(isLoading = false)
//            }
//    }
//}


