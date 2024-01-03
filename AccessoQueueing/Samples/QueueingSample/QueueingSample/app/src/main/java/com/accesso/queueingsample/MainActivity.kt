package com.accesso.queueingsample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.accesso.queueingsample.ui.theme.QueueingSampleTheme
import com.accesso.queueingsample.views.Dialog
import com.accesso.queueingsample.views.NavHostGraph
import com.accessosdk.accessoqueueing.model.ReservationFailed
import com.accessosdk.accessoqueueing.model.ReservationSuccessful

class MainActivity : ComponentActivity() {

    internal lateinit var viewModel: AttractionViewModel
    internal lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = AttractionViewModel()
        viewModel.initializeQueueing(applicationContext, this)

        setContent {
            QueueingTheme()
        }

        viewModel.reservationResponseLiveData.observe(this){
            if (it is ReservationSuccessful) {
                Toast.makeText(this, "Reservation Successful!", Toast.LENGTH_SHORT).show()
                navController.navigateUp()
                navController.navigateUp()
            } else if (it is ReservationFailed){
                Toast.makeText(this, "Reservation Unsuccessful: ${it.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun QueueingTheme() {
        QueueingSampleTheme {
            navController = rememberNavController()

            Dialog(viewModel)

            Column(modifier = Modifier.background(Color(0xFF96A8F5))) {
                NavHostGraph(navController = navController, viewModel)
            }
        }
    }
}
