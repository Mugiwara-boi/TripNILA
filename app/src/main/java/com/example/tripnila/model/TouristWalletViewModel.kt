package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.TouristWallet
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TouristWalletViewModel(private val repository: UserRepository = UserRepository()) : ViewModel()  {
    private val _touristWallet = MutableStateFlow(TouristWallet())
    val touristWallet = _touristWallet.asStateFlow()

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    private val _isLoadingAddBalance = MutableStateFlow<Boolean?>(null)
    val isLoadingAddBalance: StateFlow<Boolean?> = _isLoadingAddBalance

    private val _selectedMethod = MutableStateFlow<String?>(null)
    val selectedMethod: StateFlow<String?> = _selectedMethod

    fun setSelectedMethod(method: String) {
        _selectedMethod.value = method
        Log.d("SelectedMethod", "${selectedMethod.value}")
    }

    fun getMethodBalance(method:String){
        val paypalBalance = _touristWallet.value.paypalBalance
        val paymayaBalance = _touristWallet.value.paymayaBalance
        val gcashbalance = _touristWallet.value.gcashBalance
    }
    fun addBalance(touristId: String) {

            val currentBalance = _touristWallet.value.currentBalance
            val cashInAmount = _amount.value// Convert Int to Double
            Log.d("CashInAmountInAdd", "${amount.value}")

            val newBalance = currentBalance + cashInAmount
            _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance)

            when (_selectedMethod.value ){
                "paypal" -> {
                    _touristWallet.value = _touristWallet.value.copy(paypalBalance = _touristWallet.value.paypalBalance - cashInAmount)
                    Log.d("paypalBalance", "${_touristWallet.value.paypalBalance}")
                }
                "gcash" -> {
                    _touristWallet.value = _touristWallet.value.copy(gcashBalance = _touristWallet.value.gcashBalance - cashInAmount)
                    Log.d("gcashBalance", "${_touristWallet.value.gcashBalance}")
                }
                "paymaya" -> {
                    _touristWallet.value = _touristWallet.value.copy(paymayaBalance = _touristWallet.value.paymayaBalance - cashInAmount)
                    Log.d("paymayaBalance", "${_touristWallet.value.paymayaBalance}")
                }
            }
            updateBalance(touristId)
            Log.d("currentBalance", "${_touristWallet.value.currentBalance}")

    }

    fun setAmount(cash: Double){
            _amount.value = cash
            Log.d("CashInAmount", "${amount.value}")

    }

    fun getWallet(touristId: String){
        viewModelScope.launch {
            val touristWallet = repository.getTouristWallet(touristId)
            _touristWallet.value = touristWallet
            Log.d("currentAmount", "${_touristWallet.value.currentBalance}")
            Log.d("paypalAmount", "${_touristWallet.value.paypalBalance}")
            Log.d("paymayaAmount", "${_touristWallet.value.paymayaBalance}")
            Log.d("gcashAmount", "${_touristWallet.value.gcashBalance}")
        }
    }

    fun updateBalance(touristId: String){
        viewModelScope.launch {
            val balance = _touristWallet.value.currentBalance
            val paypalBalance = _touristWallet.value.paypalBalance
            val gcashBalance = _touristWallet.value.gcashBalance
            val paymayaBalance = _touristWallet.value.paymayaBalance
            val pendingBalance = _touristWallet.value.pendingBalance

            val wallet = repository.addBalance(
                touristId = touristId,
                amount = balance,
                paymayaBalance = paymayaBalance,
                paypalBalance = paypalBalance,
                pendingBalance = pendingBalance,
                gcashBalance = gcashBalance,
            )
        }
    }

}