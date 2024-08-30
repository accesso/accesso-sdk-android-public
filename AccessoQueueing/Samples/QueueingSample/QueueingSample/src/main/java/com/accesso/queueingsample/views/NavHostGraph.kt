package com.accesso.queueingsample.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.accesso.queueingsample.AttractionDetailsDestination
import com.accesso.queueingsample.AttractionListDestination
import com.accesso.queueingsample.AttractionViewModel
import com.accesso.queueingsample.AuthViewModel
import com.accesso.queueingsample.LandingPageDestination
import com.accesso.queueingsample.ReserveActionDestination
import com.accesso.queueingsample.SingInDestination
import com.accesso.queueingsample.models.AuthState
import com.accesso.queueingsample.models.SignInStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavHostGraph(
    navController: NavHostController,
    viewModel: AttractionViewModel,
    authViewModel: AuthViewModel
) {
    val selectedAttraction = viewModel.selectedAttractionLiveData.observeAsState()
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()
    val signInStatus by authViewModel.signInStatusLiveData.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.RequireAuthentication -> navController.navigate(SingInDestination.route) {
                popUpTo(LandingPageDestination.route) {
                    inclusive = true
                }
            }

            AuthState.Authenticated -> Unit // No action needed, user is authenticated
        }
    }
    LaunchedEffect(signInStatus) {
        when (signInStatus) {
            is SignInStatus.Success -> navController.navigate(LandingPageDestination.route) // Replace with your actual route
            is SignInStatus.Failure -> Toast.makeText(
                context,
                (signInStatus as SignInStatus.Failure).message,
                Toast.LENGTH_SHORT
            ).show()
            null -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = LandingPageDestination.route
    ) {
        composable(LandingPageDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = LandingPageDestination.title,
                                modifier = Modifier.testTag("header_text")
                            )
                        },
                        backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    LandingPage(navHostController = navController, viewModel = viewModel)
                })
        }
        composable(AttractionListDestination.route) {
            fun refresh() = refreshScope.launch {
                refreshing = true
                viewModel.getAttractions()
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)

            Box(Modifier.pullRefresh(state)) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = AttractionListDestination.title,
                                    modifier = Modifier.testTag("header_text")
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        Icons.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        modifier = Modifier.testTag("navigate_back")
                                    )
                                }
                            }, backgroundColor = Color.LightGray
                        )

                    },
                    content = { padding ->
                        Modifier.padding(padding)
                        if (!refreshing) {
                            AttractionList(navController = navController, viewModel = viewModel)
                        }
                    })

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
            }
        }
        composable(AttractionDetailsDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            selectedAttraction.value?.name?.let { it1 ->
                                Text(
                                    text = it1,
                                    modifier = Modifier.testTag("header_text")
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.testTag("navigate_back")
                                )
                            }
                        }, backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    selectedAttraction.value?.let { it1 ->
                        AttractionDetailsScreen(
                            navHostController = navController,
                            attraction = it1
                        )
                    }
                })
        }
        composable(ReserveActionDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            selectedAttraction.value?.name?.let { it1 ->
                                Text(
                                    text = it1,
                                    modifier = Modifier.testTag("header_text")
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.testTag("navigate_back")
                                )
                            }
                        }, backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    selectedAttraction.value?.let { it1 ->
                        ReserveActionScreen(
                            attraction = it1,
                            viewModel = viewModel
                        )
                    }
                })
        }
        composable(SingInDestination.route) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = SingInDestination.title,
                                modifier = Modifier.testTag("header_text")
                            )
                        },
                        backgroundColor = Color.LightGray
                    )
                },
                content = { padding ->
                    Modifier.padding(padding)
                    SignInScreen { email, pass ->
                        authViewModel.signIn(email, pass)
                    }
                })
        }
    }
}
