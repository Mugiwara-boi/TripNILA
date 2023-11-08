import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import kotlinx.coroutines.launch
import com.example.tripnila.repository.UserRepository

class LoginViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun setUsername(newUsername: String) {
        loginUiState = loginUiState.copy(username = newUsername)
    }

    fun setPassword(newPassword: String) {
        loginUiState = loginUiState.copy(password = newPassword)
    }

    private fun validateLoginForm() =
        loginUiState.username.isNotBlank() &&
                loginUiState.password.isNotBlank()

    fun loginUser(context: Context) = viewModelScope.launch {
            try {
                if (!validateLoginForm()) {
                    // If form validation fails, display a toast
                    Toast.makeText(context, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Update the UI state to reflect loading
                loginUiState = loginUiState.copy(isLoading = true, loginError = null)

                // Simulated login by checking credentials
                val user = repository.getUserByCredentials(loginUiState.username, loginUiState.password)

                // Handle the login result
                loginUiState = loginUiState.copy(isSuccessLogin = user != null)

                // Display success message or failure message using Toast
                val message = if (user != null) "Login successful" else "Login failed"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                loginUiState = loginUiState.copy(loginError = e.localizedMessage)
                e.printStackTrace()
            } finally {
                loginUiState = loginUiState.copy(isLoading = false)
            }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val loginError: String? = null,
)
