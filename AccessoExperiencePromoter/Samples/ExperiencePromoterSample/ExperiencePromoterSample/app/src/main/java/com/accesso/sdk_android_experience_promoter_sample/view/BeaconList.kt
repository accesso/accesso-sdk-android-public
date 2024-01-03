package com.accesso.sdk_android_experience_promoter_sample.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun BeaconList(viewModel: MessagesViewModel) {
    val beacons = viewModel.beaconsLiveData.observeAsState()
    val isListeningState = viewModel.isListeningForBeaconsLiveData.observeAsState()
    val listeningStatusLabel: String
    val listeningButtonLabel: String
    if (isListeningState.value == true) {
        listeningStatusLabel = "Listening"
        listeningButtonLabel = "Stop Listening"
    } else {
        listeningStatusLabel = "Stopped"
        listeningButtonLabel = "Start Listening"
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Status: $listeningStatusLabel", Modifier.padding(vertical = 16.dp).testTag("beacons_status"))
        }
        beacons.value.let { beaconsList ->
            Row {
                Text(text = "  Beacon Regions:")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (beaconsList != null) {
                    items(beaconsList) { beacon ->
                        Divider()
                        BeaconListItem(beacon = beacon, viewModel = viewModel)
                        Divider()
                    }
                }
            }
        }
        Row {
            Button(
                onClick = {
                    beacons.value?.let {
                        if (isListeningState.value == true) {
                            viewModel.stopListeningForBeacons(it)
                        } else {
                            viewModel.startListeningForBeacons(it)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp, vertical = 12.dp)
                    .testTag("beacons_listening_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Text(text = listeningButtonLabel)
            }
        }
    }
}