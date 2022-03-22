package ru.manzharovn.herofinder.presentation.ui.herolist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.herofinder.R
import ru.manzharovn.herofinder.presentation.ui.theme.HeroFinderTheme
import ru.manzharovn.herofinder.presentation.utils.ErrorMessage
import ru.manzharovn.herofinder.presentation.utils.LoadingScreen
import ru.manzharovn.herofinder.presentation.utils.Status
import ru.manzharovn.herofinder.presentation.viewmodel.HeroListViewModel
import ru.manzharovn.herofinder.presentation.viewmodel.NUMBER_OF_HEROES_PER_PAGE


@Composable
fun HeroListScreen(
    viewModel: HeroListViewModel,
    onHeroClick: () -> Unit
){
    when(val status = viewModel.initStatus){
        Status.OK -> {
            HeroList(
                heroes = viewModel.heroes,
                status = viewModel.nextPageStatus,
                isLastPage = viewModel::isLastPage,
                nextPage = viewModel::nextPage
            )
        }
        Status.LOADING -> {
            LoadingScreen()
        }
        else -> {
            ErrorMessage(
                status = status,
                viewModel::tryAgain
            )
        }
    }
}

@Composable
fun HeroList(
    heroes: List<HeroShortDescription>,
    status: Status,
    isLastPage: () -> Boolean,
    nextPage: () -> Unit
) {
    val listState = rememberLazyListState()
    Column() {
        LazyColumn(state = listState) {
            items(items = heroes) { hero ->
                HeroCard(hero = hero)
            }
            if(status == Status.LOADING) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        if (listState.firstVisibleItemIndex == heroes.size - NUMBER_OF_HEROES_PER_PAGE / 2) {
            if(status != Status.LOADING && !isLastPage()) {
                nextPage()
            }
        }
    }
}


@Composable
fun HeroCard(hero: HeroShortDescription){
    Surface(
        shape = RoundedCornerShape(size = 10.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(160.dp),
                model = hero.imageSrc,
                placeholder = painterResource(id = R.drawable.placeholder),
                alignment = Alignment.TopCenter,
                contentDescription = "Hero image"
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    text = cropDescription(hero.description),
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

fun cropDescription(desc: String?): String =
    if (desc != null) {
        if(desc.length < 150) {
            desc
        } else {
            desc.substring(0,150)
        }
    } else {
        "No description"
    }


@Preview(showBackground = true)
@Composable
fun HeroListPreview() {
    HeroFinderTheme {
        HeroList(heroes = Test.heroes, status = Status.OK, isLastPage = {true}) {

        }
    }
}
object Test{
    val heroes = listOf(
        HeroShortDescription("Batman","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","sss"),
        HeroShortDescription("Batman","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","sss"),
        HeroShortDescription("Batman","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","sss"),
        HeroShortDescription("Batman","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","sss"),
        HeroShortDescription("Batman","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss","sss"),
    )
}