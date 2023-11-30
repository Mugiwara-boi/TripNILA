package com.example.tripnila.data
import com.example.tripnila.model.DetailViewModel
class PaymentSingleton private constructor() {
    // Singleton instance variable
    companion object {
        // Volatile annotation ensures that the instance is always read from main memory
        @Volatile
        private var INSTANCE: PaymentSingleton? = null
        // Function to get the instance of the singleton
        fun getInstance(): PaymentSingleton {
            // Double-check locking to ensure thread safety
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PaymentSingleton().also { INSTANCE = it }
            }
        }
    }
    object ViewModelHolder {
        var detailViewModel: DetailViewModel? = null
    }
    // Other methods and properties of the singleton class
}