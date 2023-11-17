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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern

class SignupViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _signupUiState = MutableStateFlow(SignUpUiState())
    val signupUiState: StateFlow<SignUpUiState> = _signupUiState

    fun setFirstName(newFirstName: String) {
        _signupUiState.value = _signupUiState.value.copy(firstName = newFirstName)
    }

    fun setMiddleName(newMiddleName: String) {
        _signupUiState.value = _signupUiState.value.copy(middleName = newMiddleName)
    }

    fun setLastName(newLastName: String) {
        _signupUiState.value = _signupUiState.value.copy(lastName = newLastName)
    }

    fun setUsername(newUsername: String) {
        _signupUiState.value = _signupUiState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        _signupUiState.value = _signupUiState.value.copy(password = newPassword)
    }

    fun setConfirmPassword(newPassword: String) {
        _signupUiState.value = _signupUiState.value.copy(confirmPassword = newPassword)
    }

    fun checkAgreement(isAgree: Boolean) {
        _signupUiState.value = _signupUiState.value.copy(isAgree = isAgree)
    }

    private fun validateSignUpForm(): Boolean {
        return _signupUiState.value.firstName.isNotBlank() &&
                _signupUiState.value.middleName.isNotBlank() &&
                _signupUiState.value.lastName.isNotBlank() &&
                _signupUiState.value.username.isNotBlank() &&
                _signupUiState.value.password.isNotBlank() &&
                _signupUiState.value.confirmPassword.isNotBlank() &&
                _signupUiState.value.isAgree
    }


    private fun isPasswordValid(): Boolean {
        val password = _signupUiState.value.password
        val passwordLength = password.length
        val hasNumber = Pattern.compile("\\d").matcher(password).find()
        return passwordLength in 8..36 && hasNumber
    }

    private fun validateMatchingPassword(): Boolean {
        return _signupUiState.value.password == _signupUiState.value.confirmPassword
    }

    private suspend fun isUsernameAvailable(): Boolean {
        return try {
            !repository.checkIfUsernameExists(_signupUiState.value.username)
        } catch (e: Exception) {
            // Handle the exception, for example, log it
            e.printStackTrace()
            false
        }
    }

    fun createUser() {
        viewModelScope.launch {
            try {
                if (!validateSignUpForm()) {
                    _signupUiState.value = _signupUiState.value.copy(
                        signInError = "Fields cannot be empty",
                        isSuccessSignin = false
                    )
                    return@launch
                } else if (!isUsernameAvailable()) {
                    _signupUiState.value = _signupUiState.value.copy(
                        signInError = "Username already exists",
                        isSuccessSignin = false
                    )
                    return@launch
                } else if (!isPasswordValid()) {
                    _signupUiState.value = _signupUiState.value.copy(
                        signInError = "Password must be 8-36 characters length and has number",
                        isSuccessSignin = false
                    )
                    return@launch
                } else if (!validateMatchingPassword()) {
                    _signupUiState.value = _signupUiState.value.copy(
                        signInError = "Passwords must be match",
                        isSuccessSignin = false
                    )
                    return@launch
                }
                _signupUiState.value = _signupUiState.value.copy(isLoading = true, signInError = null)
                val userCreated = repository.addUser(
                    Tourist(
                        firstName = _signupUiState.value.firstName,
                        middleName = _signupUiState.value.middleName,
                        lastName = _signupUiState.value.lastName,
                        username = _signupUiState.value.username,
                        password = _signupUiState.value.password
                    )
                )
                _signupUiState.value = _signupUiState.value.copy(isSuccessSignin = userCreated)

            } catch (e: Exception) {
                _signupUiState.value = _signupUiState.value.copy(signInError = e.localizedMessage)
                e.printStackTrace()
            } finally {
                _signupUiState.value = _signupUiState.value.copy(isLoading = false)
            }
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
    val isSuccessSignin: Boolean? = null,
    val signInError: String? = null,
)
