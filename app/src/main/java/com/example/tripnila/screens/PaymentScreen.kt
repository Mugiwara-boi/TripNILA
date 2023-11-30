package com.example.tripnila.screens
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.tripnila.data.PaymentSingleton
import com.example.tripnila.databinding.ScreenPaymentBinding
import com.example.tripnila.model.DetailViewModel
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class PaymentScreen : ComponentActivity() {
    private lateinit var binding: ScreenPaymentBinding
    private val tag = "MyTag"
    val detailViewModel = PaymentSingleton.ViewModelHolder.detailViewModel
    val sampleamount = 30.50
    val total = detailViewModel?.calculateTotalAmount().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val touristId = intent.getStringExtra("touristId")
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        binding = ScreenPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.paymentButtonContainer.setup(
            createOrder = CreateOrder { createOrderActions ->
                Log.v(tag, "CreateOrder")
                createOrderActions.create(
                    OrderRequest.Builder()
                        .appContext(
                            AppContext(
                                userAction = UserAction.PAY_NOW
                            )
                        )
                        .intent(OrderIntent.CAPTURE)
                        .purchaseUnitList(
                            listOf(
                                PurchaseUnit.Builder()
                                    .amount(
                                        Amount.Builder()
                                            .value(total)
                                            .currencyCode(CurrencyCode.PHP)
                                            .build()
                                    )
                                    .build()
                            )
                        )
                        .build()
                        .also { Log.d(tag, "Order: $it") }
                )
            },
            onApprove = OnApprove { approval ->
                Log.v(tag, "OnApprove")
                Log.d(tag, "Approval details: $approval")
                approval.orderActions.capture { captureOrderResult ->
                    Log.v(tag, "Capture Order")
                    Log.d(tag, "Capture order result: $captureOrderResult")
                }
                coroutineScope.launch {
                    detailViewModel?.addBooking(touristId ?: "")
                }
                finish()
            },
            onCancel = OnCancel {
                Log.v(tag, "OnCancel")
                Log.d(tag, "Buyer cancelled the checkout experience.")
                finish()
            },
            onError = OnError { errorInfo ->
                Log.v(tag, "OnError")
                Log.d(tag, "Error details: $errorInfo")
                finish()
            }
        )
    }
    companion object {
        fun startIntent(context: Context): Intent {
            return Intent(context, PaymentScreen::class.java)
        }
    }
}