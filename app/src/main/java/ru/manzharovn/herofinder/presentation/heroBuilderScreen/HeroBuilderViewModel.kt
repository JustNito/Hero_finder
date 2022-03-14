package ru.manzharovn.herofinder.presentation.heroBuilderScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import ru.manzharovn.domain.models.ErrorEntity
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.usecase.GetPowersUseCase
import ru.manzharovn.herofinder.presentation.utils.Status

class HeroBuilderViewModel(private val getPowersUseCase: GetPowersUseCase) : ViewModel() {

   val listOfPower = mutableStateListOf<Power>()

   private var status = Status.OK

   fun getStatus(): Status = status

   private fun changeStatusByError(error: ErrorEntity) {
      status = when (error) {
         ErrorEntity.Unknown -> Status.UNKNOWN
         ErrorEntity.AccessDenied -> Status.ACCESS_DENIED
         ErrorEntity.ServiceUnavailable -> Status.UNAVAILABLE
         ErrorEntity.Network -> Status.NETWORK
         ErrorEntity.NotFound -> Status.NOT_FOUND
      }
   }

   fun getData() {
      viewModelScope.launch() {
         when(val result = getPowersUseCase()){
            is Result.Success -> listOfPower.addAll(result.data)

            is Result.Error -> changeStatusByError(result.error)
         }
      }
   }
}

