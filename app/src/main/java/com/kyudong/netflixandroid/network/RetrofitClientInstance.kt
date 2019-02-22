package com.kyudong.netflixandroid.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.kyudong.netflixandroid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okio.Okio
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Kyudong on 31/01/2019.
 */
object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private var BASE_URL = "https://netflixshare.shop:8080/"
    //private var BASE_URL = "https://api.3rd.supply/"

    fun retrofitInstance(): Retrofit {
        if (retrofit == null) {

            val client = OkHttpClient.Builder()

            client.addInterceptor(LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build())

            val okHttpClient = client.build()
            retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }

        return retrofit!!
    }
}