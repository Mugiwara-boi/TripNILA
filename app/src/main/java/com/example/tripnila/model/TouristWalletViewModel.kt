package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.data.TouristWallet
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TouristWalletViewModel(private val repository: UserRepository = UserRepository()) : ViewModel()  {
    private val _touristWallet = MutableStateFlow(TouristWallet())
    val touristWallet = _touristWallet.asStateFlow()

    private val _hostWallet = MutableStateFlow(TouristWallet())
    val hostWallet = _hostWallet.asStateFlow()

    private val _isEnoughBalance = MutableStateFlow(false)
    val isEnoughBalance = _isEnoughBalance.asStateFlow()

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    private val _totalFee = MutableStateFlow(0.0)
    val totalFee = _totalFee.asStateFlow()

    private val _isLoadingAddBalance = MutableStateFlow<Boolean?>(null)
    val isLoadingAddBalance: StateFlow<Boolean?> = _isLoadingAddBalance

    private val _selectedMethod = MutableStateFlow<String?>(null)
    val selectedMethod: StateFlow<String?> = _selectedMethod

    private val _tourist = MutableStateFlow(Tourist())
    val tourist = _tourist.asStateFlow()

    private val _remainingBalance = MutableStateFlow(0.0)
    val remainingBalance = _remainingBalance.asStateFlow()
    fun setSelectedMethod(method: String) {
        _selectedMethod.value = method
        Log.d("SelectedMethod", "${selectedMethod.value}")
    }

    fun setEnoughBalance(enough: Boolean){
        _isEnoughBalance.value = enough
    }
    fun setTotalFee(amount:Double) {
        _totalFee.value = amount
        Log.d("TotalFee", "${totalFee.value}")
    }

    /*fun getMethodBalance(method:String){
        val paypalBalance = _touristWallet.value.paypalBalance
        val paymayaBalance = _touristWallet.value.paymayaBalance
        val gcashbalance = _touristWallet.value.gcashBalance
    }*/

    fun setRemainingBalance(amount: Double){
        viewModelScope.launch {
            _remainingBalance.value = amount
        }
    }

    fun setBookingPayment(totalFee: Double, touristId: String){

        val currentBalance = _touristWallet.value.currentBalance
        val newBalance = currentBalance.minus(totalFee)

        _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance)
        Log.d("TotalFee", "${_touristWallet.value.currentBalance}")
        updateBalance(touristId)
    }

    fun setPendingAmount(amount: Double, hostWalletId: String){
        viewModelScope.launch {


            // Update only the pendingBalance field
            _hostWallet.value = _hostWallet.value.copy(pendingBalance = _hostWallet.value.pendingBalance + amount)

            Log.d("TotalFee", "${_hostWallet.value.pendingBalance}")

            // Update the balance on the server
            updateHostBalance(hostWalletId)
        }
    }
    fun getTouristProfile(touristId: String){
        viewModelScope.launch{
            val touristProfile = repository.getTouristProfile(touristId)
            _tourist.value = touristProfile!!
        }

    }
    fun addBalance(touristId: String) {

            val currentBalance = _touristWallet.value.currentBalance
            val cashInAmount = _amount.value// Convert Int to Double
            Log.d("CashInAmountInAdd", "${amount.value}")

            val newBalance = currentBalance.plus(cashInAmount)
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

    fun withdrawBalance(touristId: String){

        val currentBalance = _touristWallet.value.currentBalance
        val cashInAmount = _amount.value// Convert Int to Double
        Log.d("CashInAmountInAdd", "${amount.value}")

        val newBalance = currentBalance.minus(cashInAmount)
        _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance!!)

        when (_selectedMethod.value ){
            "paypal" -> {
                _touristWallet.value = _touristWallet.value.copy(paypalBalance = _touristWallet.value.paypalBalance + cashInAmount)
                Log.d("paypalBalance", "${_touristWallet.value.paypalBalance}")
            }
            "gcash" -> {
                _touristWallet.value = _touristWallet.value.copy(gcashBalance = _touristWallet.value.gcashBalance + cashInAmount)
                Log.d("gcashBalance", "${_touristWallet.value.gcashBalance}")
            }
            "paymaya" -> {
                _touristWallet.value = _touristWallet.value.copy(paymayaBalance = _touristWallet.value.paymayaBalance + cashInAmount)
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

    /*fun clearWallet(){
        viewModelScope.launch {
            _touristWallet.value = TouristWallet()
            Log.d("Cleared", "wallet clear")
        }
    }*/

    fun getWallet(touristId: String){
        viewModelScope.launch {

            val touristWallet = repository.getTouristWallet(touristId)

            // Update the touristWallet LiveData
            _touristWallet.value = touristWallet
            Log.d("TouristId", "${_touristWallet.value.touristId}")
            Log.d("currentAmount", "${_touristWallet.value.currentBalance}")
            Log.d("paypalAmount", "${_touristWallet.value.paypalBalance}")
            Log.d("paymayaAmount", "${_touristWallet.value.paymayaBalance}")
            Log.d("gcashAmount", "${_touristWallet.value.gcashBalance}")
        }
    }

    fun getHostWallet(hostWalletId: String){
        viewModelScope.launch {

            val touristWallet = repository.getTouristWallet(hostWalletId)

            // Update the touristWallet LiveData
            _hostWallet.value = touristWallet
            Log.d("TouristId", "${touristWallet.touristId}")
            Log.d("currentAmount", "${touristWallet.currentBalance}")
            Log.d("paypalAmount", "${touristWallet.paypalBalance}")
            Log.d("paymayaAmount", "${touristWallet.paymayaBalance}")
            Log.d("gcashAmount", "${touristWallet.gcashBalance}")
        }
    }
    fun updateHostBalance(touristId: String){
        viewModelScope.launch {
            val balance = _hostWallet.value.currentBalance
            val paypalBalance = _hostWallet.value.paypalBalance
            val gcashBalance = _hostWallet.value.gcashBalance
            val paymayaBalance = _hostWallet.value.paymayaBalance
            val pendingBalance = _hostWallet.value.pendingBalance

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