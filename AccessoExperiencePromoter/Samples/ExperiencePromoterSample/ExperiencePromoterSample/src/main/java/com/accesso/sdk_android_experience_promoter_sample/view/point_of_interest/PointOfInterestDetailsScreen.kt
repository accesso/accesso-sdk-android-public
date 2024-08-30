package com.accesso.sdk_android_experience_promoter_sample.view.point_of_interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.accesso.sdk_android_experience_promoter_sample.R
import com.accessosdk.experiencepromoter.model.PointOfInterest

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun PointOfInterestDetailsScreen(pointOfInterest: PointOfInterest?) {
    Scaffold(
        content = { padding ->
            LazyColumn{
                item {
                    if (pointOfInterest?.images != null && pointOfInterest.images!!.isNotEmpty()) {
                        PointOfInterestImage(imageUrl = pointOfInterest.images!![0].src!!, Modifier
                            .fillMaxWidth()
                            .testTag("poi_details_image_${pointOfInterest.images!![0].src}"))
                    } else {
                        Image(
                            painterResource(R.drawable.message_detail_image_placeholder),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("poi_details_image_placeholder"),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row(Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)){
                        Text(text = pointOfInterest?.description ?: "",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("poi_details_description_text"))
                    }
                }
            }
        }
    )
}
