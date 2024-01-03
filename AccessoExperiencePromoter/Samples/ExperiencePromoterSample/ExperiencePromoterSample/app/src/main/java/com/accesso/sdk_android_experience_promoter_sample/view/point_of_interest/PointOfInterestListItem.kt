package com.accesso.sdk_android_experience_promoter_sample.view.point_of_interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.accesso.sdk_android_experience_promoter_sample.R
import com.accesso.sdk_android_experience_promoter_sample.navigation.PointOfInterestDetailsDestination
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel
import com.accessosdk.experiencepromoter.model.PointOfInterest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun PointOfInterestListItem(pointOfInterest: PointOfInterest, navController: NavHostController, viewModel: MessagesViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .clickable {
                    viewModel.setSelectedPOI(pointOfInterest)
                    navController.navigate(PointOfInterestDetailsDestination.route)
                }
                .testTag("poi_list_item_${pointOfInterest.id}"),
            horizontalArrangement = Arrangement.Start
        ) {

            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                )
            ) {
                if (pointOfInterest.images != null && pointOfInterest.images!!.isNotEmpty()) {
                    PointOfInterestImage(imageUrl = pointOfInterest.images!![0].src!!, Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                        .testTag("poi_list_item_image_${pointOfInterest.images!![0].src}")
                    )
                } else {
                    Image(painterResource(R.drawable.message_list_image_placeholder),
                        contentDescription = null,
                        Modifier
                            .size(56.dp)
                            .testTag("poi_list_item_image_placeholder")
                    )
                }
            }

            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 16.dp
                )
            ) {
                Text(
                    text = pointOfInterest.label ?: "",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag("poi_list_item_title_${pointOfInterest.label ?: ""}")
                )
                Text(
                    text = pointOfInterest.description ?: "",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("poi_list_item_description_${pointOfInterest.description ?: ""}")
                )
            }
        }
    }
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PointOfInterestImage(imageUrl: String, modifier: Modifier) {
    GlideImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}