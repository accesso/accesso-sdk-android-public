package com.accesso.sdk_android_experience_promoter_sample.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.accesso.sdk_android_experience_promoter_sample.navigation.BeaconListDestination
import com.accesso.sdk_android_experience_promoter_sample.navigation.MessageListDestination
import com.accesso.sdk_android_experience_promoter_sample.navigation.PointOfInterestListDestination
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun LandingPage(navHostController: NavHostController, viewModel: MessagesViewModel) {
    Scaffold(
    content = { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                Button(
                    onClick = {navHostController.navigate(MessageListDestination.route)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp, vertical = 12.dp)
                        .testTag("home_message_inbox_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text(text = "Message Inbox")
                }
            }
            item {
                Button(
                    onClick = {
                        navHostController.navigate(BeaconListDestination.route)
                        viewModel.fetchBeacons()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp, vertical = 12.dp)
                        .testTag("home_beacons_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text(text = "Beacons")
                }
            }
            item {
                Button(
                    onClick = {
                        navHostController.navigate(PointOfInterestListDestination.route)
                        viewModel.fetchPointsOfInterest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp, vertical = 12.dp)
                        .testTag("home_points_of_interest_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text(text = "Points of Interest")
                }
            }
            item {
                Button(
                    onClick = {
                        if (viewModel.isModuleInitialized) {
                            viewModel.showEvents()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp, vertical = 12.dp)
                        .testTag("home_events_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text(text = "Events")
                }
            }
        }
    }
    )
}