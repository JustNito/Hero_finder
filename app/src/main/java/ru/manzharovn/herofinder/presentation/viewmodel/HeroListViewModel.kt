package ru.manzharovn.herofinder.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.usecase.GetHeroShortDescriptionByIdsUseCase
import ru.manzharovn.herofinder.presentation.utils.Status

const val NUMBER_OF_HEROES_PER_PAGE = 10

class HeroListViewModel(
   private val getHeroShortDescriptionByIdsUseCase: GetHeroShortDescriptionByIdsUseCase
) : ViewModel() {

    val heroes = mutableStateListOf<HeroShortDescription>()

    var nextPageStatus by mutableStateOf(Status.OK)

    var initStatus by mutableStateOf(Status.LOADING)

    private var currentPage = 0

    private var heroIds = listOf<Int>()

    fun isLastPage(): Boolean = heroes.size == heroIds.size

    fun tryAgain() {
        initList(heroIds)
    }

    fun initList(heroIds: List<Int>) {
        heroes.clear()
        currentPage = 0
        this.heroIds = heroIds
        viewModelScope.launch(Dispatchers.IO) {
            initStatus = Status.LOADING
            when(val result = getHeroes()) {
                is Result.Success -> {
                    heroes.addAll(result.data)
                    initStatus = Status.OK
                }
                is Result.Error -> {}
            }

        }
    }

    private suspend fun getHeroes(): Result<List<HeroShortDescription>> {
        val remainingHeroes = heroIds.size - heroes.size
        val result = getHeroShortDescriptionByIdsUseCase(
            heroIds.subList(
                heroes.size,
                if(remainingHeroes <= NUMBER_OF_HEROES_PER_PAGE)
                    NUMBER_OF_HEROES_PER_PAGE * currentPage + remainingHeroes
                else
                    NUMBER_OF_HEROES_PER_PAGE * (currentPage + 1)
            )
        )
        currentPage += 1
        return result
    }

    fun nextPage(){
        viewModelScope.launch(Dispatchers.IO) {
            nextPageStatus = Status.LOADING
            when(val result = getHeroes()) {
                is Result.Success -> {
                    heroes.addAll(result.data)
                    nextPageStatus = Status.OK
                }
                is Result.Error -> {}
            }

        }
    }
}