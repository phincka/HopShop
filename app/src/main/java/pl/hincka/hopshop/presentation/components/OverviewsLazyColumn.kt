//package pl.hincka.pszzapp.presentation.components
//
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.runtime.Composable
//import pl.hincka.hopshop.presentation.components.OverviewRow
//import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//
//@Composable
//fun OverviewsLazyColumn(
//    overviews: List<OverviewModel>,
//    navigator : DestinationsNavigator
//) {
//    HorizontalDivider()
//    LazyColumn() {
//        items(items = overviews, key = { it.id }) { overview ->
//            OverviewRow(overview, navigator)
//            HorizontalDivider()
//        }
//    }
//}