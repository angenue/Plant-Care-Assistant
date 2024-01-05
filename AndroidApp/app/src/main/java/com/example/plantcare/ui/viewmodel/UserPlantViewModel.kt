package com.example.plantcare.ui.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantcare.data.model.CombinedPlantDto
import com.example.plantcare.data.model.PlantWateringHistory
import com.example.plantcare.data.model.UserPlant
import com.example.plantcare.data.model.UserPlantDto
import com.example.plantcare.data.model.UserPlantWateringHistoryResponse
import com.example.plantcare.data.model.WateringEventDto
import com.example.plantcare.data.network.UserPlantService
import com.example.plantcare.util.RetrofitService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
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

    private val _uploadImageStatus = MutableLiveData<OperationStatus<String>>()
    val uploadImageStatus: LiveData<OperationStatus<String>> = _uploadImageStatus


    private val _deletePlantStatus = MutableLiveData<OperationStatus<Boolean>>()
    val deletePlantStatus: LiveData<OperationStatus<Boolean>> = _deletePlantStatus

    private val _wateringHistory = MutableLiveData<OperationStatus<UserPlantWateringHistoryResponse>>()
    val wateringHistory: LiveData<OperationStatus<UserPlantWateringHistoryResponse>> = _wateringHistory
    private val _wateringLogUpdateStatus = MutableLiveData<OperationStatus<PlantWateringHistory>>()
    val wateringLogUpdateStatus: LiveData<OperationStatus<PlantWateringHistory>> = _wateringLogUpdateStatus

    private val _wateringLogDeletionStatus = MutableLiveData<OperationStatus<Unit>>()
    val wateringLogDeletionStatus: LiveData<OperationStatus<Unit>> = _wateringLogDeletionStatus


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

    /*fun updateUserPlantPicture(userPlantId: Long, newPicture: String) {
        viewModelScope.launch {
            try {
                val updatedUserPlant = userPlantService.updateUserPlantPicture(userPlantId, newPicture)
                _updatePlantStatus.value = OperationStatus.Success(updatedUserPlant)
                getPlantDetails(userPlantId)
            } catch (e: Exception) {
                _updatePlantStatus.value = OperationStatus.Error(e)
            }
        }
    }*/

    fun updatePlantName(userPlantId: Long, newName: String) {
        viewModelScope.launch {
            try {
                val updatedUserPlant = userPlantService.updatePlantName(userPlantId, newName)
                _updatePlantStatus.value = OperationStatus.Success(updatedUserPlant)
                getPlantDetails(userPlantId)
            } catch (e: Exception) {
                _updatePlantStatus.value = OperationStatus.Error(e)
            }
        }
    }

    fun uploadUserPlantImage(userPlantId: Long, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val file = uriToFile(imageUri, context)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                // Use userPlantService for the network request
                val response = userPlantService.uploadImage(userPlantId, body)
                if (response.isSuccessful && response.body() != null) {
                    _uploadImageStatus.value = OperationStatus.Success(response.body()!!.imageUrl)
                    getPlantDetails(userPlantId)
                } else {
                    _uploadImageStatus.value = OperationStatus.Error(
                        RuntimeException("Error uploading image: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                _uploadImageStatus.value = OperationStatus.Error(e)
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

    fun clearUploadStatus() {
        _uploadImageStatus.value = null
    }

    private fun uriToFile(selectedImageUri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = File(context.cacheDir, contentResolver.getFileName(selectedImageUri))

        val inputStream = contentResolver.openInputStream(selectedImageUri)
        inputStream?.use { input ->
            FileOutputStream(myFile).use { output ->
                input.copyTo(output)
            }
        }

        return myFile
    }


    private fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val returnCursor = this.query(uri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    // Function to fetch watering history
    fun fetchWateringHistory(userPlantId: Long) {
        viewModelScope.launch {
            try {
                val response = userPlantService.getWateringLog(userPlantId)
                if (response.isSuccessful && response.body() != null) {
                    _wateringHistory.value = OperationStatus.Success(response.body()!!)
                } else {
                    _wateringHistory.value = OperationStatus.Error(Exception("Failed to fetch watering history"))
                }
            } catch (e: Exception) {
                _wateringHistory.value = OperationStatus.Error(e)
            }
        }
    }

    // Function to add a watering event
    fun addWateringEvent(userPlantId: Long, wateringEvent: WateringEventDto) {
        viewModelScope.launch {
            try {
                val response = userPlantService.addWateringEvent(userPlantId, wateringEvent)
                if (response.isSuccessful && response.body() != null) {
                    fetchWateringHistory(userPlantId) // Refresh history after adding
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    /*fun updateWateringLog(logId: Long, newWateringDateTime: LocalDateTime, waterAmount: Double) {
        viewModelScope.launch {
            try {
                val response = userPlantService.updateWateringLog(logId, newWateringDateTime, waterAmount)
                if (response.isSuccessful) {
                    // Handle successful update
                    _wateringLogUpdateStatus.value = OperationStatus.Success(response.body()!!)
                } else {
                    // Handle error scenario
                    _wateringLogUpdateStatus.value = OperationStatus.Error(Exception("Failed to update watering log"))
                }
            } catch (e: Exception) {
                _wateringLogUpdateStatus.value = OperationStatus.Error(e)
            }
        }
    }*/

    fun deleteWateringLog(logId: Long) {
        viewModelScope.launch {
            try {
                val response = userPlantService.deleteWateringHistory(logId)
                if (response.isSuccessful) {
                    _wateringLogDeletionStatus.value = OperationStatus.Success(Unit)
                } else {
                    _wateringLogDeletionStatus.value = OperationStatus.Error(Exception("Failed to delete watering log"))
                }
            } catch (e: Exception) {
                _wateringLogDeletionStatus.value = OperationStatus.Error(e)
            }
        }
    }


}

sealed class OperationStatus<out T> {
    data class Success<out T>(val data: T): OperationStatus<T>()
    data class Error(val exception: Exception): OperationStatus<Nothing>()
}

