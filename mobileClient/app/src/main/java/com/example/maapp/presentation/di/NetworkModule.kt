package com.example.maapp.presentation.di

import com.example.maapp.BuildConfig
import com.example.maapp.data.network.DeviceTestApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val CONNECT_TIMEOUT_MILLIS = 600000L
        private const val READ_TIMEOUT_MILLIS = 600000L
        //private const val BASE_URl = "http://www.mocky.io/"
        //private const val BASE_URL = "http://93.180.9.72:12333/"
        //private const val BASE_URL = "http://192.168.0.43:8000"
        //private const val BASE_URL = "http://172.20.10.4:8000"
        private const val BASE_URL = "http://team2010.parallel.ru:12333"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
            interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()
    }

    /**
     *
     * @param gson парсер JSON файлов
     * @param okHttpClient клиент для отправки и получения запросов
     *
     */

    @Provides
    @Singleton
    fun provideAuthRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()


    /**
     * @param retrofit формирует запрос и взаимодействует с клиентом okHttpClient
     */

    @Provides
    @Singleton
    fun provideApiDeviceTestApi(
            retrofit: Retrofit
    ): DeviceTestApi = retrofit.create(DeviceTestApi::class.java)
}