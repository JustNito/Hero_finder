package ru.manzharovn.herofinder.presentation.ui.herobuilder

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.manzharovn.domain.models.Power
import ru.manzharovn.herofinder.R
import ru.manzharovn.herofinder.presentation.ui.theme.HeroFinderTheme
import ru.manzharovn.herofinder.presentation.utils.ErrorMessage
import ru.manzharovn.herofinder.presentation.utils.LoadingScreen
import ru.manzharovn.herofinder.presentation.utils.Status
import ru.manzharovn.herofinder.presentation.viewmodel.HeroBuilderViewModel


@Composable
fun HeroBuilderScreen(
    viewModel: HeroBuilderViewModel,
    showHeroes: () -> Unit
) {
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
                viewModel.amountOfFoundHeroes,
                viewModel.heroesStatus,
                viewModel::isPowerChosen,
                viewModel::choosePower,
                viewModel::unchoosePower,
                showHeroes,
            )
            Status.LOADING -> LoadingScreen()
            else -> {
                ErrorMessage(
                    status,
                    viewModel::getPowers
                )
            }
        }
    }
}

@Composable
fun HeroBuilder(
    powers: List<Power>,
    amountOfFoundHeroes: Int,
    heroesStatus: Status,
    isPowerChosen : (Power) -> Boolean,
    choosePower: (Power) -> Unit,
    unchoosePower: (Power) -> Unit,
    showHeroes: () -> Unit
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
        Basement(
            amountOfFoundHeroes,
            heroesStatus,
            showHeroes
        )
    }
}



@Composable
fun Basement(
    amountOfFoundHeroes: Int,
    heroesStatus: Status,
    showHeroes: () -> Unit
) {
    var isButtonEnabled by remember {
        mutableStateOf(true)
    }
    when(heroesStatus) {
        Status.LOADING -> {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "Found heroes: "
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
            }
            isButtonEnabled = false
        }
        Status.OK -> {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.h6,
                text = "Found heroes: $amountOfFoundHeroes"
            )
            isButtonEnabled = amountOfFoundHeroes != 0
        }
        else -> {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                text = "No connection"
            )
            isButtonEnabled = false
        }
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {showHeroes()},
        enabled = isButtonEnabled
    ) {
        Text(stringResource(R.string.search_button))
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
        HeroBuilder(listOf(),100, Status.LOADING, { false },{},{},{})
    }
}