package com.example.plantcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantcare.data.model.CombinedPlantDto
import com.example.plantcare.data.model.UserPlant
import com.example.plantcare.data.model.UserPlantDto
import com.example.plantcare.data.network.UserPlantService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlantViewModel @Inject constructor(private val userPlantService: UserPlantService) : ViewModel() {

    private val _userPlants = MutableLiveData<List<UserPlant>>()
    val userPlants: LiveData<List<UserPlant>> = _userPlants

    // LiveData to observe the operation's success or error
    private val _addPlantStatus = MutableLiveData<Result<UserPlant>>()
    val addPlantStatus: LiveData<Result<UserPlant>> = _addPlantStatus

    init {
        loadUserPlants()
    }

    private fun loadUserPlants() {
        viewModelScope.launch {
            try {
                val response = userPlantService.getAllSavedPlants()
                if (response.isSuccessful) {
                    _userPlants.value = response.body() ?: emptyList()
                } else {
                    // Handle errors
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun addPlantToUserCollection(userPlantDto: UserPlantDto) {
        viewModelScope.launch {
            try {
                val response = userPlantService.addUserPlant(userPlantDto)
                if (response.isSuccessful) {
                    _addPlantStatus.value = Result.success(response.body()!!)
                } else {
                    _addPlantStatus.value = Result.failure(
                        RuntimeException("Error adding plant: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _addPlantStatus.value = Result.failure(e)
            }
        }
    }
}
