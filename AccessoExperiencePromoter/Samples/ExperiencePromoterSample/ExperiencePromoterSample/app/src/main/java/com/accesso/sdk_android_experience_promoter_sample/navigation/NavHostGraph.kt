package com.accesso.sdk_android_experience_promoter_sample.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.accesso.sdk_android_experience_promoter_sample.view.BeaconList
import com.accesso.sdk_android_experience_promoter_sample.view.LandingPage
import com.accesso.sdk_android_experience_promoter_sample.view.MessageDetailsScreen
import com.accesso.sdk_android_experience_promoter_sample.view.MessagesList
import com.accesso.sdk_android_experience_promoter_sample.view.point_of_interest.PointOfInterestDetailsScreen
import com.accesso.sdk_android_experience_promoter_sample.view.point_of_interest.PointOfInterestList
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel
import kotlinx.coroutines.launch

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavHostGraph(navController: NavHostController, viewModel: MessagesViewModel) {
    val selectedMessage = viewModel.selectedMessageLiveData.observeAsState()
    val selectedPOI = viewModel.selectedPointOfInterest.observeAsState()
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = LandingPageDestination.route
    ) {
        composable(LandingPageDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = LandingPageDestination.title, modifier = Modifier.testTag("header_text")) },
                        backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    LandingPage(navHostController = navController, viewModel = viewModel)
                })
        }
        composable(MessageListDestination.route) {
            fun refresh() = refreshScope.launch {
                refreshing = true
                viewModel.refreshMessages()
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)

            Box(Modifier.pullRefresh(state)) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = MessageListDestination.title, modifier = Modifier.testTag("header_text")) },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.testTag("navigate_back"))
                                }
                            }, backgroundColor = Color.LightGray
                        )

                    },
                    content = { padding ->
                        Modifier.padding(padding)
                        if (!refreshing) {
                            MessagesList(navController = navController, viewModel = viewModel)
                        }
                    })

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
            }
        }
        composable(MessageDetailsDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = selectedMessage.value?.subject.toString(), modifier = Modifier.testTag("header_text")) } ,
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.testTag("navigate_back"))
                            }
                        }, backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    MessageDetailsScreen(
                        message = selectedMessage.value
                    )
                })
        }
        composable(BeaconListDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = BeaconListDestination.title, modifier = Modifier.testTag("header_text")) } ,
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.testTag("navigate_back"))
                            }
                        }, backgroundColor = Color.LightGray
                    )

                },
                content = { padding ->
                    Modifier.padding(padding)
                    BeaconList( viewModel = viewModel)
                }
            )
        }
        composable(PointOfInterestListDestination.route) {
            fun refresh() = refreshScope.launch {
                refreshing = true
                viewModel.fetchPointsOfInterest()
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)

            Box(Modifier.pullRefresh(state)) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = PointOfInterestListDestination.title, modifier = Modifier.testTag("header_text")) },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.testTag("navigate_back"))
                                }
                            }, backgroundColor = Color.LightGray
                        )

                    },
                    content = { padding ->
                        Modifier.padding(padding)
                        if (!refreshing) {
                            PointOfInterestList(navController = navController, viewModel = viewModel)
                        }
                    })

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
            }
        }
        composable(PointOfInterestDetailsDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = selectedPOI.value?.label.toString(), modifier = Modifier.testTag("header_text")) } ,
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.testTag("navigate_back"))
                            }
                        }, backgroundColor = Color.LightGray
                    )

                },
                content = { padding ->
                    Modifier.padding(padding)
                    PointOfInterestDetailsScreen(
                        pointOfInterest = selectedPOI.value
                    )
                })
        }
    }
}