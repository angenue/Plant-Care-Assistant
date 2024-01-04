package com.example.plantcare.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantcare.data.model.Plant
import com.example.plantcare.data.model.RecentlySearchedPlantsDto
import com.example.plantcare.data.model.SimplePlant
import com.example.plantcare.data.network.PlantService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val plantService: PlantService
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SimplePlant>>()
    val searchResults: LiveData<List<SimplePlant>> = _searchResults

    private val _recentlySearched = MutableLiveData<List<RecentlySearchedPlantsDto>>()
    val recentlySearched: LiveData<List<RecentlySearchedPlantsDto>> = _recentlySearched

    val plantDetails = MutableLiveData<Plant>()

    //debounce search query to reduce number of requests
    private var searchJob: Job? = null

    // Function to search plants by query
    fun searchPlants(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            val response: Response<List<SimplePlant>> = plantService.searchPlants(query)
            if (response.isSuccessful) {
                _searchResults.value = response.body()
            } else {
                // Handle errors (e.g., show a message or log an error)
            }
        }
    }

    // Function to load recently searched plants
    fun loadRecentlySearchedPlants() {
        viewModelScope.launch {
            val response: Response<List<RecentlySearchedPlantsDto>> = plantService.getRecentlySearchedPlants()
            if (response.isSuccessful) {
                _recentlySearched.value = response.body()
            } else {
                // Handle errors
            }
        }
    }

    // Function to fetch plant details and update recently searched
    fun getPlantDetails(plantId: String) {
        viewModelScope.launch {
            val response: Response<Plant> = plantService.getPlantDetails(plantId)
            if (response.isSuccessful) {
                plantDetails.value = response.body()

                loadRecentlySearchedPlants()
            } else {
                // Handle errors
            }
        }
    }

}
