//package pl.hincka.pszzapp.presentation.components
//
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.runtime.Composable
//import pl.hincka.hopshop.presentation.components.HiveRow
//import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//
//@Composable
//fun HivesLazyColumn(
//    hives: List<HiveModel>,
//    navigator : DestinationsNavigator
//) {
//    HorizontalDivider()
//    LazyColumn() {
//        items(items = hives, key = { it.id }) { hive ->
//            HiveRow(hive, navigator)
//            HorizontalDivider()
//        }
//    }
//}