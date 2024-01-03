package com.accesso.sdk_android_experience_promoter_sample.view

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
import com.accesso.sdk_android_experience_promoter_sample.util.formatDateTime
import com.accessosdk.experiencepromoter.model.Message

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun MessageDetailsScreen(message: Message?) {
    Scaffold(
        content = { padding ->
            LazyColumn{
                item {
                    if (message?.images != null && message.images!!.isNotEmpty()) {
                        MessageImage(imageUrl = message.images!![0].src, Modifier
                            .fillMaxWidth()
                            .testTag("message_item_image_${message.images!![0].src}"))
                    } else {
                        Image(
                            painterResource(R.drawable.message_detail_image_placeholder),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("message_item_image_placeholder"),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row(modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            bottom = 48.dp
                        )) {
                        message?.publishTimestamp?.let {
                            Text(text = formatDateTime(
                                it,
                                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                                "MMMM dd, yyyy"),
                                fontSize = 10.sp,
                                modifier = Modifier.testTag("message_timestamp")
                            )
                        }
                    }
                    Row(Modifier.padding(start = 8.dp, end = 8.dp)){
                        Text(text = message?.body ?: "",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag("message_details_body"))
                    }
                }
            }
        }
    )
}
