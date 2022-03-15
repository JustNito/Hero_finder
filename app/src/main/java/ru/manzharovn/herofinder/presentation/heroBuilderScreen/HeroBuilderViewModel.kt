package ru.manzharovn.herofinder.presentation.heroBuilderScreen

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import ru.manzharovn.domain.models.ErrorEntity
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.usecase.GetPowersUseCase
import ru.manzharovn.herofinder.presentation.utils.Status

class HeroBuilderViewModel(private val getPowersUseCase: GetPowersUseCase) : ViewModel() {

   val listOfPower = mutableStateListOf<Power>()

   private val chosenPowers = mutableStateListOf<Power>()

   private var _amountOfChosenPowers by mutableStateOf(chosenPowers.size)
   val amountOfChosenPowers: Int
      get() = _amountOfChosenPowers

   private var _status by mutableStateOf(Status.LOADING)
   val status: Status
      get() = _status

   private fun updateAmountOfChosenPowers() {
      _amountOfChosenPowers = chosenPowers.size
   }

   fun clearChosenPowers() {
      chosenPowers.clear()
      updateAmountOfChosenPowers()
   }

   private fun changeStatusByError(error: ErrorEntity) {
      _status = when (error) {
         ErrorEntity.Unknown -> Status.UNKNOWN
         ErrorEntity.AccessDenied -> Status.ACCESS_DENIED
         ErrorEntity.ServiceUnavailable -> Status.UNAVAILABLE
         ErrorEntity.Network -> Status.NETWORK
         ErrorEntity.NotFound -> Status.NOT_FOUND
      }
   }

   fun unchoosePower(power: Power){
      chosenPowers.remove(power)
      updateAmountOfChosenPowers()
   }

   fun choosePower(power: Power) {
      chosenPowers.add(power)
      updateAmountOfChosenPowers()
   }

   fun isPowerChosen(power: Power): Boolean = chosenPowers.contains(power)

   fun getData() {
      if (listOfPower.isEmpty()) {
         viewModelScope.launch(Dispatchers.IO) {
            _status = Status.LOADING
            when (val result = getPowersUseCase()) {
               is Result.Success -> {
                  listOfPower.addAll(result.data)
                  Log.i("HeroBuilder", "listOfPower length: ${listOfPower.size}")
                  _status = Status.OK
               }

               is Result.Error -> changeStatusByError(result.error)
            }
         }
      }
   }
}

