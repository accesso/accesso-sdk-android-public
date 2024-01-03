package com.accesso.sdk_android_experience_promoter_sample.view.point_of_interest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun PointOfInterestList(navController: NavHostController, viewModel: MessagesViewModel) {

    val pointsOfInterest = viewModel.pointsOfInterestLiveData.observeAsState()

    pointsOfInterest.value.let { poiList ->
        if (poiList.isNullOrEmpty()) {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Points of Interest", color = Color.Black.copy(alpha = 0.5f))
                }
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                   Text(text = "(Pull to refresh)",color = Color.Black.copy(alpha = 0.5f), modifier = Modifier.testTag("empty_state_refresh_text"))
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            if (poiList != null) {
                items(poiList) { pointOfInterest ->
                    if (pointOfInterest != null) {
                        PointOfInterestListItem(pointOfInterest, navController, viewModel)
                    }
                }
            }
        }
    }
}