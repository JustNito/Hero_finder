package ru.manzharovn.herofinder.presentation


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.manzharovn.domain.models.Power
import ru.manzharovn.herofinder.R
import ru.manzharovn.herofinder.presentation.ui.herobuilder.HeroBuilderScreen
import ru.manzharovn.herofinder.presentation.ui.herolist.HeroListScreen
import ru.manzharovn.herofinder.presentation.viewmodel.HeroBuilderViewModel
import ru.manzharovn.herofinder.presentation.viewmodel.HeroBuilderViewModelFactory
import ru.manzharovn.herofinder.presentation.ui.theme.HeroFinderTheme
import ru.manzharovn.herofinder.presentation.utils.HeroScreens
import ru.manzharovn.herofinder.presentation.utils.Status
import ru.manzharovn.herofinder.presentation.viewmodel.HeroListViewModel
import ru.manzharovn.herofinder.presentation.viewmodel.HeroListViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var heroListViewModelFactory: HeroListViewModelFactory

    @Inject
    lateinit var heroBuilderViewModelFactory: HeroBuilderViewModelFactory

    private val heroBuilderViewModel: HeroBuilderViewModel by viewModels{
        heroBuilderViewModelFactory
    }

    private val heroListViewModel: HeroListViewModel by viewModels {
        heroListViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        heroBuilderViewModel.getPowers()
        setContent {
            HeroFinderTheme {
                HeroApp(
                    heroBuilderViewModel = heroBuilderViewModel,
                    heroListViewModel = heroListViewModel
                )
            }
        }
    }
}
@Composable
fun HeroApp(
    heroBuilderViewModel: HeroBuilderViewModel,
    heroListViewModel: HeroListViewModel
){
    HeroFinderTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = HeroScreens.HeroBuilder.name
        ) {
            composable(HeroScreens.HeroBuilder.name) {
               HeroBuilderScreen(
                   viewModel = heroBuilderViewModel
               ) {
                   heroListViewModel.initList(heroBuilderViewModel.getHeroIds())
                   Log.i("HeroApp", "to next screen")
                   navController.navigate(HeroScreens.HeroList.name)
               }
            }
            composable(HeroScreens.HeroList.name) {
                HeroListScreen(
                    viewModel = heroListViewModel,
                ) {

                }
            }
        }
    }
}



