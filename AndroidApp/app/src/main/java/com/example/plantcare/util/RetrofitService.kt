package com.example.plantcare.util

import com.example.plantcare.data.network.PlantService
import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.data.network.UserPlantService
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class RetrofitService(sessionManager: SessionManager) {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionManager))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(ToStringConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
    val userPlantApi: UserPlantService by lazy { retrofit.create(UserPlantService::class.java) }
    val plantApi: PlantService by lazy {
        retrofit.create(PlantService::class.java)
    }
}

class ToStringConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type == String::class.java) {
            return Converter<String, RequestBody> { value ->
                value.toRequestBody("text/plain".toMediaTypeOrNull())
            }
        }
        return null
    }
}
