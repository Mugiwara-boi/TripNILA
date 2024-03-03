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



    private val _adminWallet = MutableStateFlow(TouristWallet())
    val adminWallet = _adminWallet.asStateFlow()

    private val _hostWallet = MutableStateFlow(TouristWallet())
    val hostWallet = _hostWallet.asStateFlow()

    private val _isEnoughBalance = MutableStateFlow(false)
    val isEnoughBalance = _isEnoughBalance.asStateFlow()

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    private val _refundAmount = MutableStateFlow(0.0)
    val refundAmount = _refundAmount.asStateFlow()

    private val _refundedPendingBalance = MutableStateFlow(0.0)
    val refundedPendingBalance = _refundedPendingBalance.asStateFlow()

    private val _refundedCurrentBalance = MutableStateFlow(0.0)
    val refundedCurrentBalance = _refundedCurrentBalance.asStateFlow()

    private val _percentRefunded = MutableStateFlow(0.0)
    val percentRefunded = _percentRefunded.asStateFlow()

    private val _totalFee = MutableStateFlow(0.0)
    val totalFee = _totalFee.asStateFlow()

    private val _initialTotalFee = MutableStateFlow(0.0)
    val initialTotalFee = _initialTotalFee.asStateFlow()

    private val _newTotalFee = MutableStateFlow(0.0)
    val newTotalFee = _newTotalFee.asStateFlow()

    private val _initialTripnilaFee = MutableStateFlow(0.0)
    val initialTripnilaFee = _initialTripnilaFee.asStateFlow()

    private val _newTripnilaFee = MutableStateFlow(0.0)
    val newTripnilaFee = _newTripnilaFee.asStateFlow()

    private val _tripnilaFee = MutableStateFlow(0.0)
    val tripnilaFee = _tripnilaFee.asStateFlow()

    private val _isLoadingAddBalance = MutableStateFlow<Boolean?>(null)
    val isLoadingAddBalance: StateFlow<Boolean?> = _isLoadingAddBalance

    private val _selectedMethod = MutableStateFlow<String?>(null)
    val selectedMethod: StateFlow<String?> = _selectedMethod

    private val _tourist = MutableStateFlow(Tourist())
    val tourist = _tourist.asStateFlow()

    private val _remainingBalance = MutableStateFlow(0.0)
    val remainingBalance = _remainingBalance.asStateFlow()

    private val _alertDialogMessage = MutableStateFlow<String?>(null)
    val alertDialogMessage: StateFlow<String?> get() = _alertDialogMessage

    fun setAlertDialogMessage() {
        _alertDialogMessage.value =  "Are you sure you want to proceed?" // No issues, return null for no alert dialog

    }
    fun setSelectedMethod(method: String) {
        _selectedMethod.value = method
        Log.d("SelectedMethod", "${selectedMethod.value}")
    }

    fun setRefundAmount(amount:Double){
        _refundAmount.value = amount
        Log.d("RefundAmount", "${_refundAmount.value}")
    }

    fun setInitialTotalFee(amount:Double){
        _initialTotalFee.value = amount
        Log.d("RefundAmount", "${_initialTotalFee.value}")
    }
    fun setNewTotalFee(amount:Double){
        _newTotalFee.value = amount
        Log.d("RefundAmount", "${_newTotalFee.value}")
    }

    fun setInitialTripnilaFee(amount:Double) {
        _initialTripnilaFee.value = amount
        Log.d("InitialTripnilaFee", "${initialTripnilaFee.value}")
    }

    fun setNewTripnilaFee(amount:Double) {
        _newTripnilaFee.value = amount
        Log.d("TripnilaFee", "${newTripnilaFee.value}")
    }


    fun setEnoughBalance(enough: Boolean){
        _isEnoughBalance.value = enough
    }
    fun setTotalFee(amount:Double) {
        _totalFee.value = amount
        Log.d("TotalFee", "${totalFee.value}")
    }

    fun setTripnilaFee(amount:Double) {
        _tripnilaFee.value = amount
        Log.d("TripnilaFee", "${tripnilaFee.value}")
    }

    fun setPercentRefunded(days:Int){
        if(days > 7){
            _percentRefunded.value = 1.0
        } else {
            _percentRefunded.value = 0.8
        }
    }

    fun getAdminWallet(){
        viewModelScope.launch {
            _adminWallet.value = repository.getAdminWallet()
            Log.d("Admin", "${_adminWallet.value.touristId}")
            Log.d("currentAmount", "${_adminWallet.value.currentBalance}")
            Log.d("paypalAmount", "${_adminWallet.value.paypalBalance}")
            Log.d("paymayaAmount", "${_adminWallet.value.paymayaBalance}")
            Log.d("gcashAmount", "${_adminWallet.value.gcashBalance}")
            Log.d("pendingBalance", "${_adminWallet.value.pendingBalance}")
        }
    }

    fun setRefundedBalance(touristId: String, hostWalletId: String, tripnilaFee: Double){
        val refundedAmount = (_refundAmount.value - tripnilaFee) * _percentRefunded.value
        _refundedCurrentBalance.value = _refundAmount.value - refundedAmount - tripnilaFee
        _refundedPendingBalance.value = _refundAmount.value - tripnilaFee
        val newBalance = _touristWallet.value.currentBalance + refundedAmount + tripnilaFee
        _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance)
        updateBalance(touristId)
        setRefundedAmountToHostWallet(hostWalletId, tripnilaFee)
        Log.d("RefundedAmount", "$refundedAmount")
        Log.d("NewBalanceAfterRefundedAmount", "${_touristWallet.value.currentBalance}")
        Log.d("NewRefundedCurrentBalance", "${_refundedCurrentBalance.value}")
        Log.d("NewRefundedPendingBalance", "${_refundedPendingBalance.value}")
    }

    fun setRefundedAmountToHostWallet(hostWalletId: String, tripnilaFee: Double){
        val refundedPending = _hostWallet.value.pendingBalance - _refundedPendingBalance.value
        val refundedCurrent = _hostWallet.value.currentBalance + _refundedCurrentBalance.value
        val refundedFee = _adminWallet.value.pendingBalance - tripnilaFee
        _hostWallet.value = _hostWallet.value.copy(pendingBalance = refundedPending)
        _adminWallet.value = _adminWallet.value.copy(pendingBalance = refundedFee)
        _hostWallet.value = _hostWallet.value.copy(currentBalance = refundedCurrent)
        updateHostBalance(hostWalletId)
        updateAdminBalance()
        Log.d("RefundedPendingAmount", "$refundedPending")
        Log.d("RefundedCurrentAmount", "$refundedPending")
        Log.d("RefundedAmountToHostCurrent", "${_hostWallet.value.currentBalance}")
        Log.d("RefundedAmountToHostPending", "${_hostWallet.value.pendingBalance}")
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

    fun setReschedulePayment(totalFee: Double, touristId: String, initialTotalFee: Double){
        if(totalFee > initialTotalFee){
            val newFee = totalFee - initialTotalFee
            val currentBalance = _touristWallet.value.currentBalance
            val newBalance = currentBalance.minus(newFee)
            _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance)
            Log.d("TotalFee", "Paid extra $newBalance")
            updateBalance(touristId)
        }
        else if(totalFee < initialTotalFee){
            val refundFee = initialTotalFee - totalFee
            val currentBalance = _touristWallet.value.currentBalance
            val newBalance = currentBalance + refundFee
            _touristWallet.value = _touristWallet.value.copy(currentBalance = newBalance)
            Log.d("TotalFee", "Refunded back$newBalance")
            updateBalance(touristId)
        } else{
            Log.d("TotalFee", "Same amount ${_touristWallet.value.currentBalance}")
        }

    }

    fun setReschedulePendingAmount(totalFee: Double, hostWalletId: String, tripnilaFee: Double, initialTotalFee: Double, initialTripnilaFee: Double){
        viewModelScope.launch {
            if(totalFee > initialTotalFee){
                val newTotal = totalFee - initialTotalFee
                val newTripnila = tripnilaFee - initialTripnilaFee
                val newPendingFee = newTotal - newTripnila
                _hostWallet.value = _hostWallet.value.copy(pendingBalance = _hostWallet.value.pendingBalance + newPendingFee)
                _adminWallet.value = _adminWallet.value.copy(pendingBalance = _adminWallet.value.pendingBalance + newTripnila)
                Log.d("HostPendingAmount", "added $newPendingFee")
                Log.d("AdminPendingAmount", "added $newTripnila")
                updateHostBalance(hostWalletId)
                updateAdminBalance()
            }
            else if(totalFee < initialTotalFee){
                val newTotal = initialTotalFee - totalFee
                val newTripnila = initialTripnilaFee - tripnilaFee
                val refundFee = newTotal - newTripnila
                _hostWallet.value = _hostWallet.value.copy(pendingBalance = _hostWallet.value.pendingBalance - refundFee)
                _adminWallet.value = _adminWallet.value.copy(pendingBalance = _adminWallet.value.pendingBalance - newTripnila)
                Log.d("HostPendingAmount", "refunded $refundFee")
                Log.d("AdminPendingAmount", "refunded $newTripnila")
                updateHostBalance(hostWalletId)
                updateAdminBalance()
            }
            else{
                Log.d("HostPendingAmount", "same ${hostWallet.value.pendingBalance}")
                Log.d("AdminPendingAmount", "same ${adminWallet.value.pendingBalance}")
            }

        }
    }

    fun setPendingAmount(totalFee: Double, hostWalletId: String, tripnilaFee: Double){
        viewModelScope.launch {
            val hostpending = totalFee - tripnilaFee
            _hostWallet.value = _hostWallet.value.copy(pendingBalance = _hostWallet.value.pendingBalance + hostpending)
            _adminWallet.value = _adminWallet.value.copy(pendingBalance = _adminWallet.value.pendingBalance + tripnilaFee)

            Log.d("HostPendingAmount", "${hostWallet.value.pendingBalance}")
            Log.d("AdminPendingAmount", "${adminWallet.value.pendingBalance}")

            // Update the balance on the server
            updateHostBalance(hostWalletId)
            updateAdminBalance()
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
            getAdminWallet()


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
            getAdminWallet()

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

    fun updateAdminBalance(){
        viewModelScope.launch {
            val touristId = "admin"
            val balance = _adminWallet.value.currentBalance
            val paypalBalance = _adminWallet.value.paypalBalance
            val gcashBalance = _adminWallet.value.gcashBalance
            val paymayaBalance = _adminWallet.value.paymayaBalance
            val pendingBalance = _adminWallet.value.pendingBalance

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