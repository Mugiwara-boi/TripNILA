package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.TouristWallet
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TouristWalletViewModel(private val repository: UserRepository = UserRepository()) : ViewModel()  {
    private val _touristWallet = MutableStateFlow(TouristWallet())
    val touristWallet = _touristWallet.asStateFlow()


    fun addBalance(amount: Double){
        _touristWallet.value = _touristWallet.value.copy(currentBalance = _touristWallet.value.currentBalance + amount)
        Log.d("Amount", "${_touristWallet.value.currentBalance}")
    }

    fun getWallet(touristId: String){
        viewModelScope.launch {
            val touristWallet = repository.getTouristWallet(touristId)
            _touristWallet.value = touristWallet ?: TouristWallet()
            Log.d("Amount", "${_touristWallet.value.currentBalance}")
        }
    }

}