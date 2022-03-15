package ru.manzharovn.herofinder.presentation


import android.os.Bundle
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
import androidx.compose.material.icons.filled.Replay
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import ru.manzharovn.domain.models.Power
import ru.manzharovn.herofinder.R
import ru.manzharovn.herofinder.presentation.heroBuilderScreen.HeroBuilderViewModel
import ru.manzharovn.herofinder.presentation.heroBuilderScreen.HeroBuilderViewModelFactory
import ru.manzharovn.herofinder.presentation.ui.theme.HeroFinderTheme
import ru.manzharovn.herofinder.presentation.ui.theme.Shapes
import ru.manzharovn.herofinder.presentation.utils.Batman
import ru.manzharovn.herofinder.presentation.utils.Status
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var heroBuilderViewModelFactory: HeroBuilderViewModelFactory

    private val heroBuilderViewModel: HeroBuilderViewModel by viewModels{
        heroBuilderViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        heroBuilderViewModel.getData()
        setContent {
            HeroFinderTheme {
                HeroBuilderScreen(heroBuilderViewModel)
            }
        }
    }
}

@Composable
fun HeroCard(){
    Surface(
        shape = RoundedCornerShape(size = 10.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.batman),
                contentDescription = "Hero image"
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = Batman.name,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    text = Batman.batmanInfo,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}


@Composable
fun LoadingScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }

}

@Composable
fun HeroBuilderScreen(viewModel: HeroBuilderViewModel) {
    Scaffold(
        topBar = {
            if(viewModel.amountOfChosenPowers == 0) {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.hero_builder_default_title))
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(text = stringResource(
                                id = R.string.hero_builder_expanded_title,
                                viewModel.amountOfChosenPowers
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = viewModel::clearChosenPowers ) {
                            Icon(Icons.Filled.Clear, contentDescription = "Restart")
                        }
                    }
                )
            }
        }
    ) {
        when (val status = viewModel.status) {
            Status.OK -> HeroBuilder(
                viewModel.listOfPower,
                viewModel::isPowerChosen,
                viewModel::choosePower,
                viewModel::unchoosePower
            )
            Status.LOADING -> LoadingScreen()
            else -> {
                ErrorMessage(
                    status,
                    viewModel::getData
                )
            }
        }
    }
}

@Composable
fun HeroBuilder(
    powers: List<Power>,
    isPowerChosen : (Power) -> Boolean,
    choosePower: (Power) -> Unit,
    unchoosePower: (Power) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = powers) { power ->
                PowerRow(
                    power = power,
                    isPowerChosen = isPowerChosen,
                    choosePower = choosePower,
                    unchoosePower = unchoosePower
                )
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
            Text(stringResource(R.string.search_button))
        }
    }
}

@Composable
fun PowerRow(
    modifier: Modifier = Modifier,
    power: Power,
    isPowerChosen : (Power) -> Boolean,
    choosePower: (Power) -> Unit,
    unchoosePower: (Power) -> Unit,
){
    var isClicked = isPowerChosen(power)
    val surfaceColor by animateColorAsState(targetValue = if(isClicked) Color.Green else Color.White)
    Surface(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(50),
        color = surfaceColor,
        elevation = 5.dp
    ) {
        Text(
            modifier = Modifier
                .clickable {
                    if (!isClicked) {
                        choosePower(power)
                    } else {
                        unchoosePower(power)
                    }
                    isClicked = !isClicked
                }
                .fillMaxSize()
                .padding(4.dp),
            textAlign = TextAlign.Center,
            text = power.name
        )
    }
}

@Composable
fun ErrorMessage(status: Status, tryAgain: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "JOPA"
        )
        Button(
            modifier = Modifier.padding(all = 8.dp),
            onClick = tryAgain
        ) {
            Text(stringResource(R.string.try_again_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeroCardPreview() {
    HeroFinderTheme {
        HeroCard()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview(){
    HeroFinderTheme {
        ErrorMessage(status = Status.NETWORK,{})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HeroFinderTheme {
        HeroBuilder(Batman.powers, {false},{},{})
    }
}