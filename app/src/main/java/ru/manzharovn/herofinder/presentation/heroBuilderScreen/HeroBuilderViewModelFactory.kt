package ru.manzharovn.herofinder.presentation.heroBuilderScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.manzharovn.domain.usecase.GetPowersUseCase
import ru.manzharovn.domain.usecase.SearchHeroesByPowerUseCase
import javax.inject.Inject

class HeroBuilderViewModelFactory @Inject constructor(
        val getPowersUseCase: GetPowersUseCase,
        val searchHeroesByPowerUseCase: SearchHeroesByPowerUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeroBuilderViewModel(
            getPowersUseCase = getPowersUseCase,
            searchHeroesByPowerUseCase = searchHeroesByPowerUseCase
        ) as T
    }

}