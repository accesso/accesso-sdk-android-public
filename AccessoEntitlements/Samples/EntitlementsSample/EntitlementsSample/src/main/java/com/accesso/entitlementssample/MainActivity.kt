package com.accesso.entitlementssample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.accesso.entitlementssample.view.LandingPage
import com.google.android.gms.pay.PayClient

class MainActivity : ComponentActivity() {

    internal lateinit var viewModel: EntitlementsViewModel
    private val addToGoogleWalletRequestCode = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = EntitlementsViewModel()

        viewModel.initializeEntitlements(applicationContext = applicationContext, activity = this)
        viewModel.entitlementsLiveData.observe(this) {
            if (it?.isNotEmpty() == true) {
                Toast.makeText(this, "Entitlements fetched successfully", Toast.LENGTH_SHORT).show()
                Log.d(this.javaClass.simpleName, "entitlements: $it")
                viewModel.isWalletAvailable()
                viewModel.entitlement = it[0]
            }
        }
        viewModel.claimEntitlementsLiveData.observe(this) {
            if (it?.success == "true") {
                Toast.makeText(this, "Entitlement claimed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unable to claim entitlement", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isValidInputLiveData.observe(this) { isValid ->
            if (!isValid) {
                Toast.makeText(
                    this,
                    "Must supply either Barcode or both of email and order ID",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.addedToWalletLiveData.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "successfully added to wallet!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.isWalletAvailableLiveData.observe(this) {
            Toast.makeText(
                this,
                "isWalletAvailable value: $it",
                Toast.LENGTH_SHORT
            ).show()
            if (it) {
                viewModel.shouldShowWalletButton()
            }
        }
        viewModel.updatePassResponseLiveData.observe(this) {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }

        setContent {
            LandingPage(viewModel, this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == viewModel.accessoWallet.addToGoogleWalletRequestCode) {
            when (resultCode) {
                RESULT_OK -> {
                    // Pass saved successfully
                    viewModel.addToWalletSuccess()
                }

                RESULT_CANCELED -> {
                    // Save operation canceled
                    viewModel.addToWalletFail()
                }

                PayClient.SavePassesResult.SAVE_ERROR -> data?.let { intentData ->
                    val errorMessage = intentData.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE)
                    // Handle error
                    viewModel.addToWalletFail()
                }

                else -> {
                    // Handle unexpected (non-API) exception
                    viewModel.addToWalletFail()
                }
            }
        }
    }
}