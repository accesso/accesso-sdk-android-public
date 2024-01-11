package com.accesso.queueingsample.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.accesso.queueingsample.AttractionViewModel
import com.accesso.queueingsample.R
import com.accessosdk.accessoqueueing.model.ReserveAction

@Composable
fun ReserveActionInput(reserveAction: ReserveAction, viewModel: AttractionViewModel) {
    val inputRequirementsValueMap = remember { mutableStateMapOf<String, Int>() }
    inputRequirementsValueMap.clear()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        reserveAction.inputRequirements.forEach {
            val counter: MutableState<Int> =
                remember { mutableStateOf(it.defaultValue) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = it.description)
                val minusImage = painterResource(id = R.drawable.remove)
                Image(
                    painter = minusImage,
                    contentDescription = "image description",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clickable {
                            if (counter.value > it.minimum) {
                                counter.value--
                            }
                        }
                        .fillMaxSize()
                        .weight(1f, true)
                        .alpha(if (counter.value == it.minimum) 0.5f else 1f)
                )

                Text(text = counter.value.toString())
                val plusImage = painterResource(id = R.drawable.add)
                Image(
                    painter = plusImage,
                    contentDescription = "image description",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clickable {
                            if (counter.value < it.maximum) {
                                counter.value++
                            }
                        }
                        .fillMaxSize()
                        .weight(1f, true)
                        .alpha(if (counter.value == it.maximum) 0.5f else 1f)
                )
            }
            inputRequirementsValueMap[it.id] = counter.value
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                 viewModel.reserve(
                     reserveActionId = reserveAction.id,
                     input = inputRequirementsValueMap
                 )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 12.dp)
                .testTag("confirm_reservation_button"),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Confirm Reservation")
        }
    }
}