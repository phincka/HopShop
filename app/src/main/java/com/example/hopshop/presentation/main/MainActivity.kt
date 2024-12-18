package pl.hincka.hopshop.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import pl.hincka.hopshop.presentation.NavGraphs
import pl.hincka.hopshop.ui.theme.AppTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency

class MainActivity : ComponentActivity() {
    @OptIn(
        ExperimentalMaterialNavigationApi::class,
        ExperimentalAnimationApi::class
    )
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val snackbarState = SnackbarHandler(scope)

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        BottomNavigationBar(
                            navController
                        ) {
                            DestinationsNavHost(
                                navController = navController,
                                navGraph = NavGraphs.root,
                                engine = rememberAnimatedNavHostEngine(
                                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                                        enterTransition = { fadeIn(animationSpec = tween(300)) },
                                        exitTransition = { fadeOut(animationSpec = tween(300)) }
                                    )
                                ),
                                dependenciesContainerBuilder = {
                                    dependency(snackbarState)
                                }
                            )
                        }

                        Snackbar(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .align(Alignment.BottomCenter),
                            snackbarHandler = snackbarState,
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}