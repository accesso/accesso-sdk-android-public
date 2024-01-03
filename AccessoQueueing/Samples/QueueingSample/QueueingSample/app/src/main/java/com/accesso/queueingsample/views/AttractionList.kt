package com.accesso.queueingsample.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.accesso.queueingsample.AttractionViewModel

@Composable
fun AttractionList(navController: NavHostController, viewModel: AttractionViewModel) {

    val attractions = viewModel.attractionsLiveData.observeAsState()
    viewModel.getAttractions()

    attractions.value.let { attractionList ->
        if (attractionList.isNullOrEmpty()) {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Attractions", color = Color.Black.copy(alpha = 0.5f))
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
            if (attractionList != null) {
                items(attractionList) { attraction ->
                    AttractionCard(attraction, viewModel, navController)
                }
            }
        }
    }
}
