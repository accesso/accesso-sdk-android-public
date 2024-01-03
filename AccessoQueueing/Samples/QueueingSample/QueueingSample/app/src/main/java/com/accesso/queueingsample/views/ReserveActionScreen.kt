package com.accesso.queueingsample.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accesso.queueingsample.AttractionViewModel
import com.accessosdk.accessoqueueing.model.Attraction

@Composable
fun ReserveActionScreen(attraction: Attraction, viewModel: AttractionViewModel) {
    val scrollState = rememberLazyListState()

    Scaffold(
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                state = scrollState
            ) {
                item {
                    AttractionImage(
                        imageUrl = attraction.thumbnailImage,
                        Modifier
                            .fillMaxWidth()
                            .testTag("attraction_item_image_${attraction.thumbnailImage}"),
                        contentScale = ContentScale.FillWidth
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = attraction.name,
                            fontSize = 20.sp,
                            modifier = Modifier.testTag("attraction_details_name")
                        )

                        Text(
                            text = attraction.state,
                            fontSize = 14.sp,
                            modifier = Modifier.testTag("attraction_details_state")
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Row(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = attraction.description,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("attraction_details_description")
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(attraction.reserveActions) { reserveAction ->
                    ReserveActionInput(reserveAction = reserveAction, viewModel)
                }
            }
        }
    )
}
