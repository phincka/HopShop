package pl.hincka.hopshop.presentation.qrScanner

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.androidx.compose.koinViewModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.usecase.list.GetListByIdUseCase
import pl.hincka.hopshop.domain.usecase.list.GetListByShareCodeUseCase
import pl.hincka.hopshop.domain.usecase.list.ShareListUseCase
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@KoinViewModel
class QrScannerViewModel(
    private val getListByIdUseCase: GetListByIdUseCase,
    private val getListByShareCodeUseCase: GetListByShareCodeUseCase,
    private val shareListUseCase: ShareListUseCase,
) : ViewModel() {
    private val _qrScannerState: MutableStateFlow<QrScannerState> = MutableStateFlow(QrScannerState.None)
    val qrScannerState: StateFlow<QrScannerState> = _qrScannerState

    private val _shareListState: MutableStateFlow<ShareListState> = MutableStateFlow(ShareListState.None)
    val shareListState: StateFlow<ShareListState> = _shareListState

    fun getListById(id: String) {
        _qrScannerState.value = QrScannerState.Loading
        viewModelScope.launch {
            try {
                val list = getListByIdUseCase(listId = id)

                if (list != null) {
                    _qrScannerState.value = QrScannerState.Success(
                        list = list,
                    )
                } else {
                    _qrScannerState.value = QrScannerState.Error("Failed: Nie znaleziono ula o podanym ID")
                }

                Log.d("LOG_H", _qrScannerState.value.toString())
            } catch (e: Exception) {
                _qrScannerState.value = QrScannerState.Error("Failed: ${e.message}")
            }
        }
    }

    fun getListBySharedCode(shareCode: String) {
        _qrScannerState.value = QrScannerState.Loading
        viewModelScope.launch {
            try {
                val list = getListByShareCodeUseCase(shareCode = shareCode)

                if (list != null) {
                    _qrScannerState.value = QrScannerState.Success(
                        list = list,
                    )
                } else {
                    _qrScannerState.value = QrScannerState.Error("Failed: Nie znaleziono ula o podanym ID")
                }
            } catch (e: Exception) {
                _qrScannerState.value = QrScannerState.Error("Failed: ${e.message}")
            }
        }
    }

    fun shareList(id: String) {
        _shareListState.value = ShareListState.Loading
        viewModelScope.launch {
            try {
                _shareListState.value = shareListUseCase(id)
            } catch (e: Exception) {
                _shareListState.value = ShareListState.Error("Failed: ${e.message}")
            }
        }
    }
}

sealed class QrScannerState {
    data object None : QrScannerState()
    data object Loading : QrScannerState()
    data class Success(
        val list: ListModel,
    ) : QrScannerState()
    data class Error(val message: String) : QrScannerState()
}