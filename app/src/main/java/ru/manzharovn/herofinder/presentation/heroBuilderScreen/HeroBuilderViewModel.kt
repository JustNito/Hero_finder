package ru.manzharovn.herofinder.presentation.heroBuilderScreen

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import ru.manzharovn.domain.models.ErrorEntity
import ru.manzharovn.domain.models.HeroShortDescription
import ru.manzharovn.domain.models.Power
import ru.manzharovn.domain.models.Result
import ru.manzharovn.domain.usecase.GetPowersUseCase
import ru.manzharovn.domain.usecase.SearchHeroesByPowerUseCase
import ru.manzharovn.herofinder.presentation.utils.Status
import kotlin.coroutines.CoroutineContext

class HeroBuilderViewModel(
   private val getPowersUseCase: GetPowersUseCase,
   private val searchHeroesByPowerUseCase: SearchHeroesByPowerUseCase
   ) : ViewModel() {

   val listOfPower = mutableStateListOf<Power>()

   private var coroutineContext: CoroutineContext? = null

   private val chosenPowers = mutableStateListOf<Power>()

   private val heroIds = mutableStateListOf<Int>()

   private var _amountOfChosenPowers by mutableStateOf(chosenPowers.size)
   val amountOfChosenPowers: Int
      get() = _amountOfChosenPowers

   private var _amountOfFoundHeroes by mutableStateOf(heroIds.size)
   val amountOfFoundHeroes: Int
      get() = _amountOfFoundHeroes

   private var _status by mutableStateOf(Status.LOADING)
   val status: Status
      get() = _status

   private var _heroesStatus by mutableStateOf(Status.OK)
   val heroesStatus: Status
      get() = _heroesStatus

   private fun updateAmountOfChosenPowers() {
      _amountOfChosenPowers = chosenPowers.size
   }

   private fun updateAmountOfFoundHeroes() {
      _amountOfFoundHeroes = heroIds.size
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

   fun clearChosenPowers() {
      chosenPowers.clear()
      heroIds.clear()
      updateAmountOfFoundHeroes()
      updateAmountOfChosenPowers()
      _heroesStatus = Status.OK
   }

   fun unchoosePower(power: Power){
      chosenPowers.remove(power)
      updateAmountOfChosenPowers()
      if(chosenPowers.size == 0) {
         heroIds.clear()
         coroutineContext?.cancel()
         _heroesStatus = Status.OK
         updateAmountOfFoundHeroes()
      } else {
         getHeroes()
      }
   }

   fun choosePower(power: Power) {
      chosenPowers.add(power)
      updateAmountOfChosenPowers()
      getHeroes()
   }

   fun isPowerChosen(power: Power): Boolean = chosenPowers.contains(power)

   private fun getHeroes() {
      coroutineContext?.cancel()
      heroIds.clear()
      coroutineContext = viewModelScope.launch(Dispatchers.IO) {
         _heroesStatus = Status.LOADING
         when (val result = searchHeroesByPowerUseCase(chosenPowers)) {
            is Result.Success -> {
               yield()
               heroIds.addAll(result.data)
               Log.i("HeroBuilder", "len: ${heroIds.size}")
               _heroesStatus = Status.OK
            }
            is Result.Error -> {
               _heroesStatus = Status.NETWORK
            }
         }
         updateAmountOfFoundHeroes()
      }
   }

   fun getPowers() {
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

