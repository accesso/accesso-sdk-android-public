package com.accesso.sdk_android_experience_promoter_sample.view

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
import com.accesso.sdk_android_experience_promoter_sample.navigation.MessageDetailsDestination
import com.accesso.sdk_android_experience_promoter_sample.util.SharedPrefsUtils
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel
import com.accessosdk.experiencepromoter.model.Message
import com.accessosdk.experiencepromoter.model.MessageEvent
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun MessageListItem(message: Message, navController: NavHostController, viewModel: MessagesViewModel) {
    val isMessageRead = SharedPrefsUtils.isMessageRead(viewModel.sharedPreferences, message)

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .clickable {
                    viewModel.setSelectedMessage(message)
                    navController.navigate(MessageDetailsDestination.route)
                    viewModel.trackMessageAction(message.msgId, MessageEvent.MessageAction.Read)
                }
                .testTag("message_list_item_${message.msgId}"),
            horizontalArrangement = Arrangement.Start
        ) {

            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = 8.dp,
                )
            ) {
                if (message.images != null && message.images!!.isNotEmpty()) {
                    MessageImage(imageUrl = message.images!![0].src, Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                        .testTag("message_list_item_image_${message.images!![0].src}"))
                } else {
                    Image(painterResource(R.drawable.message_list_image_placeholder),
                        contentDescription = null,
                        Modifier.size(56.dp)
                                .testTag("message_list_item_image_placeholder"))
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
                    text = message.subject ?: "",
                    fontWeight = if (isMessageRead) FontWeight.Light else FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag("message_list_item_subject")
                )
                Text(
                    text = message.body ?: "",
                    fontWeight = if (isMessageRead) FontWeight.Light else FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("message_list_item_body")
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
fun MessageImage(imageUrl: String, modifier: Modifier) {
    GlideImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}