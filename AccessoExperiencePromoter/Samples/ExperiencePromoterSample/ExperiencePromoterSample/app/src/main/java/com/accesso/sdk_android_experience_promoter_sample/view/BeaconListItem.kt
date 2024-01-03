package com.accesso.sdk_android_experience_promoter_sample.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accesso.networking.model.BeaconRegion
import com.accesso.sdk_android_experience_promoter_sample.R
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun BeaconListItem(beacon: BeaconRegion, viewModel: MessagesViewModel) {

    val isListeningState = viewModel.isListeningForBeaconsLiveData.observeAsState()
    val enterExitState = viewModel.beaconEnterExitLiveData.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            Box(Modifier.padding(16.dp)) {
                val imageId = if (enterExitState.value?.peekContent()?.get(beacon) == true && isListeningState.value == true) {
                    R.drawable.beacon_entered_icon
                } else {
                    R.drawable.beacon_not_entered_icon
                }
                Image(
                    painterResource(imageId),
                    contentDescription = null,
                    Modifier
                        .size(10.dp)
                        .align(Alignment.Center)
                        .testTag("beacon_uuid_indicator_${beacon.proximityUUID}_${imageId}")
                )
            }
        }
        Column {
            Text(
                text = "UUID: ${beacon.proximityUUID} Major: ${beacon.major}",
                fontSize = 14.sp,
                modifier = Modifier.testTag("beacon_uuid_${beacon.proximityUUID}")
            )
        }

    }
}