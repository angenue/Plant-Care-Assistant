package com.example.plantcare.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantcare.data.model.ImageRequest
import com.example.plantcare.data.model.Plant
import com.example.plantcare.data.model.RecentlySearchedPlantsDto
import com.example.plantcare.data.model.SimplePlant
import com.example.plantcare.data.network.PlantService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result


@HiltViewModel
class PlantViewModel @Inject constructor(
    private val plantService: PlantService
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SimplePlant>>()
    val searchResults: LiveData<List<SimplePlant>> = _searchResults

    private val _recentlySearched = MutableLiveData<List<RecentlySearchedPlantsDto>>()
    val recentlySearched: LiveData<List<RecentlySearchedPlantsDto>> = _recentlySearched

    private val _plantDetailsResponse = MutableLiveData<OperationStatus<Plant>>()
    val plantDetailsResponse: LiveData<OperationStatus<Plant>> = _plantDetailsResponse


    val plantDetails = MutableLiveData<Plant>()

    //debounce search query to reduce number of requests
    private var searchJob: Job? = null

    fun searchPlants(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            try {
                val response = plantService.searchPlants(query)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()
                } else {
                    Log.e("PlantViewModel", "Error searching plants: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PlantViewModel", "Exception in searchPlants: ${e.message}")
            }
        }
    }

    fun loadRecentlySearchedPlants() {
        viewModelScope.launch {
            try {
                val response = plantService.getRecentlySearchedPlants()
                if (response.isSuccessful) {
                    _recentlySearched.value = response.body()
                } else {
                    Log.e("PlantViewModel", "Error loading recently searched plants: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PlantViewModel", "Exception in loadRecentlySearchedPlants: ${e.message}")
            }
        }
    }

    fun getPlantDetails(plantId: String) {
        viewModelScope.launch {
            try {
                val response = plantService.getPlantDetails(plantId)
                if (response.isSuccessful) {
                    plantDetails.value = response.body()
                    loadRecentlySearchedPlants()
                } else {
                    Log.e("PlantViewModel", "Error fetching plant details: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PlantViewModel", "Exception in getPlantDetails: ${e.message}")
            }
        }
    }

    fun identifyPlantFromImage(base64Image: String) {
        viewModelScope.launch {
            val request = ImageRequest(listOf(base64Image))
            try {
                val response = plantService.identifyPlantFromImage(request)
                if (response.isSuccessful) {
                    // Update LiveData with the plant details
                    _plantDetailsResponse.value = OperationStatus.Success(response.body()!!)
                } else {
                    _plantDetailsResponse.value = OperationStatus.Error(Exception("Error identifying plant: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _plantDetailsResponse.value = OperationStatus.Error(e)
                Log.e("PlantViewModel", "Error in identifyPlantFromImage: ${e.message}")
            }
        }
    }


}

