package com.example.tripnila.data

data class Host (
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val username: String = "",
    val profilePicture: String = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
    val touristId: String = "",
    val hostId: String = ""
) {
    val fullName: String
        get() {
            val middle = if (middleName.isNotBlank()) "$middleName " else ""
            return "$firstName $middle$lastName"
        }

    val fullDisplayName: String
        get() {
            return "$firstName $lastName"
        }
}

data class HostWallet(
    val currentBalance : Double = 0.0,
    val pendingBalance : Double = 0.0,
    val hostId: String = "",
    val paypalBalance: Double = 0.0,
    val paymayaBalance: Double = 0.0,
    val gcashBalance: Double = 0.0,
)