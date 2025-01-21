package pl.hincka.hopshop.presentation.qrScanner

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import pl.hincka.hopshop.R
import pl.hincka.hopshop.components.buttons.Button
import pl.hincka.hopshop.components.design.VerticalSpacer
import pl.hincka.hopshop.components.form.InputText
import pl.hincka.hopshop.components.topBar.TopBar
import pl.hincka.hopshop.nav.destinations.DashboardScreenDestination
import pl.hincka.hopshop.nav.destinations.QrScannerScreenDestination
import pl.hincka.hopshop.presentation.components.LoadingDialog
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import pl.hincka.hopshop.presentation.main.SnackbarHandler
import pl.hincka.hopshop.presentation.main.bottomBarPadding
import pl.hincka.hopshop.ui.theme.AppTheme
import pl.hincka.hopshop.ui.theme.Typography
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@kotlin.OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition", "PermissionLaunchedDuringComposition")
@Destination
//@RootNavGraph(start = true)
@Composable
fun QrScannerScreen(
    navigator: DestinationsNavigator,
    viewModel: QrScannerViewModel = koinViewModel(),
    navController: NavController,
    snackbarHandler: SnackbarHandler,
) {
    val qrScannerState = viewModel.qrScannerState.collectAsState().value
    val shareListState = viewModel.shareListState.collectAsState().value

    var isCreateItemDialogVisible by remember { mutableStateOf(false) }
    var isShareCodeDialogVisible by remember { mutableStateOf(false) }
    var listId by remember { mutableStateOf("") }
    var shareCode by remember { mutableStateOf("") }

    if (qrScannerState is QrScannerState.Error) isCreateItemDialogVisible = false

    LaunchedEffect(key1 = listId) {
        launch {
            delay(10000L)
            listId = ""
        }
    }

    LaunchedEffect(shareListState, qrScannerState) {
        launch {
            if (shareListState is ShareListState.Error) snackbarHandler.showErrorSnackbar(message = shareListState.message)
            if (shareListState is ShareListState.Success) navigator.navigate(DashboardScreenDestination(message = "Pomyślnie udostępniono listę"))
            if (qrScannerState is QrScannerState.Error) snackbarHandler.showErrorSnackbar(message = qrScannerState.message)
        }
    }

    val cameraPermissionState = rememberPermissionState(permission = CAMERA)

    LaunchedEffect(cameraPermissionState.status) {
        when (cameraPermissionState.status) {
            PermissionStatus.Granted -> { }
            is PermissionStatus.Denied -> { cameraPermissionState.launchPermissionRequest() }
        }
    }

    Box(
        modifier = Modifier
            .bottomBarPadding(navController = navController)
            .fillMaxSize()
    ) {
        Column {
            CenterAlignedTopAppBar(
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    actionIconContentColor = Color.Transparent,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Skanuj kod QR",
                            style = Typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.black,
                        )
                    }
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navigator.navigateUp()
                            },
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            AppTheme.colors.purpleWhite,
                                            shape = CircleShape
                                        )
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Localized description",
                                        tint = AppTheme.colors.purple,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.getListById("B6fFR1HQCnfJ29t7kUsb")
                                isCreateItemDialogVisible = true
                              },
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            AppTheme.colors.purpleWhite,
                                            shape = CircleShape
                                        )
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "Localized description",
                                        tint = AppTheme.colors.purple,
                                    )
                                }
                            }
                        }
                    }
                }
            )

            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.white)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Możesz też ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline,
                                ),
                            ) {
                                append("kliknać tutaj")
                            }
                            append(" i wpisać ręcznie kod udostępniania.")
                        },
                        style = Typography.p,
                        color = AppTheme.colors.black,
                        modifier = Modifier.clickable(
                            onClick = { isShareCodeDialogVisible = true }
                        )
                    )
                }

                QRCodeScanner(
                    showBottomSheet = {
                        if (!isCreateItemDialogVisible && listId != it) {
                            listId = it
                            viewModel.getListById(listId)
                            isCreateItemDialogVisible = true
                        }
                    }
                )
            }
        }

        BottomSheet(
            isVisible = isCreateItemDialogVisible,
            setVisible = { isCreateItemDialogVisible = it },
        ) {
            if (qrScannerState is QrScannerState.Loading) LoadingDialog(
                height = Modifier.height(144.dp),
                backgroundColor = Color.Transparent,
            )

            if (qrScannerState is QrScannerState.Success) {
                val list = qrScannerState.list

                Text(
                    text = "Informacje o liście:",
                    style = Typography.h5,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer(8.dp)

                Text(
                    text = "Nazwa: ${list.name}",
                    style = Typography.p,
                    color = AppTheme.colors.neutral70,
                )

                VerticalSpacer(32.dp)

                Button(
                    text = "Dodaj listę",
                    onClick = {
                        viewModel.shareList(list.id)
                        isCreateItemDialogVisible = false
                    },
                )
            }
        }

        BottomSheet(
            isVisible = isShareCodeDialogVisible,
            setVisible = { isShareCodeDialogVisible = it },
        ) {
            Text(
                text = "Wpisz kod udostępnienia:",
                style = Typography.h5,
                fontWeight = FontWeight.SemiBold
            )
            VerticalSpacer(8.dp)

            InputText(
                placeholder = "wpisz kod",
                value = shareCode,
                onValueChange = {
                    shareCode = it
                },
            )

            Button(
                text = "Dodaj listę",
                onClick = {
                    viewModel.getListBySharedCode(shareCode)
                    isShareCodeDialogVisible = false
                    isCreateItemDialogVisible = true
                    shareCode = ""
                },
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@OptIn(ExperimentalGetImage::class)
@Composable
fun QRCodeScanner(
    showBottomSheet: (String) -> Unit,
) {
    val context = LocalContext.current
    var code by remember { mutableStateOf<Result?>(null) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            val previewView = PreviewView(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(android.util.Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val image = imageProxy.image ?: return@setAnalyzer

                    if (image.format == ImageFormat.YUV_420_888) {
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.capacity()).also { buffer.get(it) }

                        val source = YUVLuminanceSource(
                            bytes,
                            image.width,
                            image.height,
                            0,
                            0,
                            image.width,
                            image.height
                        )

                        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                        try {
                            val result = QRCodeReader().decode(binaryBitmap)
                            code = result
                        } catch (e: Exception) {
                            // No result found
                        }
                    }

                    imageProxy.close()
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        ctx as ComponentActivity,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    // Handle exception
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }, modifier = Modifier.fillMaxSize())

        code?.let {
            showBottomSheet(it.text)
        }
    }
}

class YUVLuminanceSource(
    private val yuvData: ByteArray,
    private val dataWidth: Int,
    private val dataHeight: Int,
    private val left: Int,
    private val top: Int,
    private val width: Int,
    private val height: Int
) : LuminanceSource(width, height) {

    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        val rowBytes = row ?: ByteArray(width)
        System.arraycopy(yuvData, (y + top) * dataWidth + left, rowBytes, 0, width)
        return rowBytes
    }

    override fun getMatrix(): ByteArray {
        val area = width * height
        val matrix = ByteArray(area)
        for (y in 0 until height) {
            System.arraycopy(yuvData, (y + top) * dataWidth + left, matrix, y * width, width)
        }
        return matrix
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isVisible: Boolean,
    setVisible: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        onDismissRequest = {
            setVisible(false)
        },
        sheetState = sheetState,
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 64.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                content()
            }
        }
    }
}