package com.example.tripnila.model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    fun setFirstName(newFirstName: String) {
        signUpUiState = signUpUiState.copy(firstName = newFirstName)
    }
    fun setMiddleName(newMiddleName: String) {
        signUpUiState = signUpUiState.copy(middleName = newMiddleName)
    }

    fun setLastName(newLastName: String) {
        signUpUiState = signUpUiState.copy(lastName = newLastName)
    }

    fun setUsername(newUsername: String) {
        signUpUiState = signUpUiState.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        signUpUiState = signUpUiState.copy(password = newPassword)
    }

    fun setConfirmPassword(newPassword: String) {
        signUpUiState = signUpUiState.copy(confirmPassword = newPassword)
    }
    fun checkAgreement(isAgree: Boolean) {
        signUpUiState = signUpUiState.copy(isAgree = isAgree)
    }

    private fun validateSignUpForm() =
        signUpUiState.firstName.isNotBlank() &&
        signUpUiState.middleName.isNotBlank() &&
        signUpUiState.lastName.isNotBlank() &&
        signUpUiState.username.isNotBlank() &&
        signUpUiState.password.isNotBlank() &&
        signUpUiState.confirmPassword.isNotBlank() &&
        signUpUiState.isAgree

    private fun isPasswordValid(password: String): Boolean {
        val passwordLength = password.length
        val hasNumber = Pattern.compile("\\d").matcher(password).find()
        return passwordLength in 8..36 && hasNumber
    }

    private fun validateMatchingPassword() =
        signUpUiState.password == signUpUiState.confirmPassword


    fun createUser(context: Context) = viewModelScope.launch {
        try {
            if (!validateSignUpForm()) {
                // If form validation fails, display a toast
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@launch
            } else if (!isPasswordValid(signUpUiState.password)) {
                Toast.makeText(context, "Password must be 8-36 characters length and has number", Toast.LENGTH_SHORT).show()
                return@launch
            } else if (!validateMatchingPassword()) {
                Toast.makeText(context, "Passwords must be match", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Update the UI state to reflect loading
            signUpUiState = signUpUiState.copy(isLoading = true, signInError = null)

            // Simulated creation of a user (adjust the actual call according to your implementation)
            val userCreated = repository.addUser(
                Tourist(
                    firstName = signUpUiState.firstName,
                    middleName = signUpUiState.middleName,
                    lastName = signUpUiState.lastName,
                    username = signUpUiState.username,
                    password = signUpUiState.password
                )
            )

            // Handle the result of creating the user
            signUpUiState = signUpUiState.copy(isSuccessSignin = userCreated)

            // Display a success message or a failure message using Toast
            val message = if (userCreated) "User created successfully" else "Failed to create user"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            signUpUiState = signUpUiState.copy(signInError = e.localizedMessage)
            e.printStackTrace()
        } finally {
            signUpUiState = signUpUiState.copy(isLoading = false)
        }
    }


}

data class SignUpUiState(
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isAgree: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccessSignin: Boolean = false,
    val signInError: String? = null,
)
