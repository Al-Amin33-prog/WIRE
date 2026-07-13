package com.example.wire.feature.payments.presentation.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.payments.presentation.PaymentViewModel
import com.example.wire.feature.payments.presentation.event.PaymentUiEvent
import com.example.wire.feature.payments.presentation.state.PaymentMode
import com.example.wire.feature.payments.presentation.state.PaymentUiState
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun SendMoneyScreen(
    recipientId: String,
    recipientName: String,
    mode: String,
    amount: String,
    onBackClick: () -> Unit,
    onPaymentSuccess: (amount: String, name: String) -> Unit, // Pass data to success screen
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    // 1. Initialize Stripe
    val paymentSheet = rememberPaymentSheet { result ->
        viewModel.onPaymentResult(result)
    }

    // Inside SendMoneyScreen.kt

    LaunchedEffect(uiState.paymentIntent) {
        uiState.paymentIntent?.let { intent ->
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret = intent.clientSecret,
                configuration = PaymentSheet.Configuration(
                    merchantDisplayName = "WIRE",
                    // THE FIX: Adding the Customer Context here
                    customer = intent.customerId?.let {
                        PaymentSheet.CustomerConfiguration(
                            id = it,
                            ephemeralKeySecret = intent.ephemeralKeySecret ?: ""
                        )
                    },
                    allowsDelayedPaymentMethods = false
                )
            )
        }
    }

    // 2. Stripe Side Effect: Launch sheet when Intent is generated
    LaunchedEffect(uiState.stripeClientSecret) {
        uiState.stripeClientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret = secret,
                configuration = PaymentSheet.Configuration(
                    merchantDisplayName = "WIRE",
                    allowsDelayedPaymentMethods = false
                )
            )
        }
    }

    // 3. Initialize Recipient from Navigation
    LaunchedEffect(recipientId,mode,amount) {
        val paymentMode = try {
            PaymentMode.valueOf(mode)
        }catch (e: Exception){PaymentMode.SEND}
        viewModel.onEvent(PaymentUiEvent
            .InitPayment(
                recipientId,
                recipientName,
                paymentMode,
                amount = amount.ifBlank { null }))
    }

    // 4. Success Side Effect
    LaunchedEffect(uiState.isPaymentSuccessful) {
        if (uiState.isPaymentSuccessful) {
            onPaymentSuccess(uiState.amount, uiState.recipientName)
        }
    }

    WireTheme{
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            SendMoneyContent(
                uiState = uiState,
                onEvent = {event->
                    viewModel.onEvent(event, context as? FragmentActivity)
                },
                onBackClick = onBackClick
            )
        }

    }


}
@Preview
@Composable
fun SendMoneyPreview(){
    SendMoneyContent(
        uiState = PaymentUiState(),
        onEvent = {},
        onBackClick = {}
    )
}