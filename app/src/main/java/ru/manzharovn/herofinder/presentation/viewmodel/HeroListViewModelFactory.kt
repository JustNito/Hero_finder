package ru.manzharovn.herofinder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.manzharovn.domain.usecase.GetHeroShortDescriptionByIdsUseCase
import ru.manzharovn.domain.usecase.GetPowersUseCase
import ru.manzharovn.domain.usecase.SearchHeroesByPowerUseCase
import javax.inject.Inject

class HeroListViewModelFactory @Inject constructor(
    val getHeroShortDescriptionByIdsUseCase: GetHeroShortDescriptionByIdsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeroListViewModel(
            getHeroShortDescriptionByIdsUseCase = getHeroShortDescriptionByIdsUseCase
        ) as T
    }

}