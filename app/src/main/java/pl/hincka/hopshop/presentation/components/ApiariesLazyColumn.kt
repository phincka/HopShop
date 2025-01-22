//package pl.hincka.hopshop.presentation.components
//
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.runtime.Composable
//import pl.hincka.pszzapp.presentation.components.ApiaryRow
//import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//
//@Composable
//fun ApiariesLazyColumn(
//    apiaries: List<ApiaryModel>,
//    navigator : DestinationsNavigator
//) {
//    HorizontalDivider()
//    LazyColumn() {
//        items(items = apiaries, key = { it.id }) { apiary ->
//            ApiaryRow(apiary, navigator)
//            HorizontalDivider()
//        }
//    }
//}