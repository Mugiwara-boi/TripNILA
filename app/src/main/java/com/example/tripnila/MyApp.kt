package com.example.tripnila
import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = CheckoutConfig(
            application = this,
            clientId = "AeBBGkx26cxTUdWwGGUu9hnU_BorPvle_D9POWpJWbiJIMVIHnnJGLRyS5vkI_CBV-fMCOGueqp7c2lv",
            environment = Environment.SANDBOX,
            returnUrl = "com.example.tripnila://paypalpay",
            currencyCode = CurrencyCode.PHP,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
    }
}