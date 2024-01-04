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

    private val _addPlantStatus = MutableLiveData<Result<UserPlant>>()
    val addPlantStatus: LiveData<Result<UserPlant>> = _addPlantStatus

    private val _plantDetails = MutableLiveData<CombinedPlantDto>()
    val plantDetails: LiveData<CombinedPlantDto> = _plantDetails

    private val _updatePlantStatus = MutableLiveData<OperationStatus<UserPlant>>()
    val updatePlantStatus: LiveData<OperationStatus<UserPlant>> = _updatePlantStatus

    private val _deletePlantStatus = MutableLiveData<OperationStatus<Boolean>>()
    val deletePlantStatus: LiveData<OperationStatus<Boolean>> = _deletePlantStatus


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
                    loadUserPlants()
                } else {
                    _addPlantStatus.value = Result.failure(
                        RuntimeException("Error adding plant: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _addPlantStatus.value = Result.failure(e)
            }
        }
    }

    fun getPlantDetails(userPlantId: Long) {
        viewModelScope.launch {
            try {
                val response = userPlantService.getUserPlant(userPlantId)
                if (response.isSuccessful) {
                    _plantDetails.value = response.body()
                } else {
                    // Handle errors
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun updateUserPlantPicture(userPlantId: Long, newPicture: String) {
        viewModelScope.launch {
            try {
                val response = userPlantService.updateUserPlantPicture(userPlantId, newPicture)
                if (response.isSuccessful) {
                    _updatePlantStatus.value = OperationStatus.Success(response.body()!!)
                    getPlantDetails(userPlantId)
                } else {
                    _updatePlantStatus.value = OperationStatus.Error(
                        RuntimeException("Error updating plant picture: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _updatePlantStatus.value = OperationStatus.Error(e)
            }
        }
    }

    fun updatePlantName(userPlantId: Long, newName: String) {
        viewModelScope.launch {
            try {
                val response = userPlantService.updatePlantName(userPlantId, newName)
                if (response.isSuccessful) {
                    _updatePlantStatus.value = OperationStatus.Success(response.body()!!)
                    getPlantDetails(userPlantId)
                } else {
                    _updatePlantStatus.value = OperationStatus.Error(
                        RuntimeException("Error updating plant name: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _updatePlantStatus.value = OperationStatus.Error(e)
            }
        }
    }

    fun deleteUserPlant(userPlantId: Long) {
        viewModelScope.launch {
            try {
                val response = userPlantService.deleteUserPlant(userPlantId)
                if (response.isSuccessful) {
                    _deletePlantStatus.value = OperationStatus.Success(true)
                    loadUserPlants()
                } else {
                    _deletePlantStatus.value = OperationStatus.Error(
                        RuntimeException("Error deleting plant: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _deletePlantStatus.value = OperationStatus.Error(e)
            }
        }
    }
}

sealed class OperationStatus<out T> {
    data class Success<out T>(val data: T): OperationStatus<T>()
    data class Error(val exception: Exception): OperationStatus<Nothing>()
}

