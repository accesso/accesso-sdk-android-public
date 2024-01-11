package com.accesso.queueingsample.views


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.accesso.queueingsample.AttractionViewModel
import com.accesso.queueingsample.AttractionDetailsDestination
import com.accessosdk.accessoqueueing.model.Attraction
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun AttractionCard(attraction: Attraction, viewModel: AttractionViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                viewModel.setSelectedAttraction(attraction)
                navController.navigate(AttractionDetailsDestination.route)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AttractionImage(imageUrl = attraction.thumbnailImage, Modifier
                .size(75.dp)
                .clip(CircleShape)
                .testTag("attraction_list_item_image_${attraction.thumbnailImage}"),
                ContentScale.Crop)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = attraction.name,
                    style = TextStyle(fontSize = 24.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = attraction.description,
                    style = TextStyle(fontSize = 12.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AttractionImage(imageUrl: String, modifier: Modifier, contentScale: ContentScale) {
    GlideImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
