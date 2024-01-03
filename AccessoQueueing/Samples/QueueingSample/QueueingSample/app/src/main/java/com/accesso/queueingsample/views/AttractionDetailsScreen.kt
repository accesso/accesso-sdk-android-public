package com.accesso.queueingsample.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.accesso.queueingsample.ReserveActionDestination
import com.accessosdk.accessoqueueing.model.Attraction

@Composable
fun AttractionDetailsScreen(navHostController: NavHostController, attraction: Attraction) {
    Scaffold(
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                item {
                    AttractionImage(
                        imageUrl = attraction.thumbnailImage,
                        Modifier
                            .fillMaxWidth()
                            .testTag("attraction_item_image_${attraction.thumbnailImage}"),
                        ContentScale.FillWidth
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                            fontSize = 14.sp,  // Made this smaller
                            modifier = Modifier.testTag("attraction_details_state")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = attraction.description,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("attraction_details_description")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Full object response:",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("attraction_details_object_label")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = attraction.toString(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("attraction_details_full_object")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    attraction.reserveActions.forEach {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                            Button(onClick = { navHostController.navigate(ReserveActionDestination.route) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 36.dp, vertical = 12.dp)
                                    .testTag("reserve_action_button"),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
                                ) {
                                Text(text = it.description)
                            }
                        }
                    }
                }
            }
        }
    )
}
