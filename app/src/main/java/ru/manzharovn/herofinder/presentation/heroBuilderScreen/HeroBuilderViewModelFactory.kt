package ru.manzharovn.herofinder.presentation.heroBuilderScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.manzharovn.domain.usecase.GetPowersUseCase
import javax.inject.Inject

class HeroBuilderViewModelFactory @Inject constructor(
        val getPowersUseCase: GetPowersUseCase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeroBuilderViewModel(
                getPowersUseCase = getPowersUseCase
        ) as T
    }

}