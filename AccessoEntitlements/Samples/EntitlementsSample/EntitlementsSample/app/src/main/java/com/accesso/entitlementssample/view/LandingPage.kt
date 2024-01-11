package com.accesso.entitlementssample.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.accesso.entitlementssample.EntitlementsViewModel
import com.accesso.entitlementssample.MainActivity
import com.accesso.entitlementssample.view.TestHelper.isValidInput
import com.accesso.entitlementssample.R


@Composable
fun LandingPage(viewModel: EntitlementsViewModel, activity: MainActivity) {
    var orderId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var isAddToWalletButtonVisible by remember { mutableStateOf(false) }

    viewModel.showAddToWalletButtonLiveData.observe(activity) {
        isAddToWalletButtonVisible = it
    }
    Scaffold(
        topBar = { EntitlementsTopBar() },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Row {
                    Button(
                        onClick = { getEntitlements(viewModel) },
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 24.dp)
                            .size(width = 240.dp, height = 48.dp)
                            .testTag("GetEntitlementButton"),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(text = "Get Entitlement", color = Color.White)
                    }
                }
                Row {
                    if (isAddToWalletButtonVisible) {
                        Column {
                            Button(
                                onClick = { addToWallet(viewModel) },
                                modifier = Modifier
                                    .padding(horizontal = 36.dp, vertical = 12.dp)
                                    .size(width = 240.dp, height = 48.dp)
                                    .testTag("ClaimEntitlementButton"),
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(Color.Black)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.add_to_google_wallet_button_foreground),
                                        modifier = Modifier
                                            .size(200.dp),
                                        contentDescription = "drawable icons",
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                            Button(
                                onClick = { updatePass(viewModel) },
                                modifier = Modifier
                                    .padding(horizontal = 36.dp, vertical = 12.dp)
                                    .size(width = 240.dp, height = 48.dp)
                                    .testTag("ClaimEntitlementButton"),
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(Color.Black)
                            ) {
                                Text(text = "Update Pass", color = Color.White)
                            }
                        }
                    }
                }
                Row {
                    Divider(color = Color.LightGray, thickness = 2.dp,
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 24.dp),)
                }
                Row {
                    TextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        label = { Text("Barcode", modifier = Modifier.testTag("BarcodeLabel")) },
                        trailingIcon = { Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = null,
                            tint = Color.Blue,
                            modifier = Modifier.clickable {
                                viewModel.scanBarcode()
                            }
                        )},
                        placeholder = { Text("Enter or scan your Barcode Number", modifier = Modifier.testTag("BarcodeTextFieldPlaceholderText")) },
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 24.dp)
                            .testTag("BarcodeTextField")
                    )
                }
                Row {
                    Text(text = "Or",
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 16.dp)
                            .testTag("OrSeparator"),
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    TextField(
                        value = orderId,
                        onValueChange = { orderId = it },
                        label = { Text("Order Id", modifier = Modifier.testTag("OrderIdLabel")) },
                        placeholder = { Text("Enter your Order ID", modifier = Modifier.testTag("OrderIdTextFieldPlaceholderText")) },
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 24.dp)
                            .testTag("OrderIdTextField")
                    )
                }
                Row {
                    Text(text = "And",
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 16.dp)
                            .testTag("AndSeparator"),
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("EmailAddress", modifier = Modifier.testTag("EmailLabel")) },
                        placeholder = { Text("Enter your Email Address", modifier = Modifier.testTag("EmailTextFieldPlaceholderText")) },
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 24.dp)
                            .testTag("EmailTextField")
                    )
                }
                Row {
                    Button(
                        onClick = { claimEntitlements(barcode, orderId, email, viewModel) },
                        modifier = Modifier
                            .padding(horizontal = 36.dp, vertical = 12.dp)
                            .size(width = 240.dp, height = 48.dp)
                            .testTag("ClaimEntitlementButton"),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(text = "Claim Entitlement", color = Color.White)
                    }
                }
            }
        }
    )
}

private fun addToWallet(viewModel: EntitlementsViewModel) {
    viewModel.addToWallet()
}

private fun updatePass(viewModel: EntitlementsViewModel) {
    viewModel.updatePass()
}

@Composable
fun EntitlementsTopBar() {
    TopAppBar(title = {
        Text(
            text = "Entitlements",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.testTag("EntitlementsHomeHeaderText")
        )
    },
        backgroundColor = Color.White
    )
}

private fun claimEntitlements(barcode: String = "", orderId: String = "", email: String = "", viewModel: EntitlementsViewModel) {
    if (barcode.isNotEmpty()) {
        viewModel.claimEntitlements(barcode)
        viewModel.isValidInput(true)
        isValidInput = true
    } else if (orderId.isNotEmpty() && email.isNotEmpty()) {
        viewModel.claimEntitlements(orderEmail = email, orderId = orderId)
        viewModel.isValidInput(true)
        isValidInput = true
    } else {
        viewModel.isValidInput(false)
        isValidInput = false
    }
}

private fun getEntitlements(viewModel: EntitlementsViewModel) {
    viewModel.getEntitlements()
    isValidInput = true
}

object TestHelper{
    var isValidInput: Boolean = false //for testing only
}
