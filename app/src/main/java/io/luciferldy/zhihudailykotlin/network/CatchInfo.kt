package io.luciferldy.zhihudailykotlin.network

import io.luciferldy.zhihudailykotlin.service.DailyInfoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLConnection

/**
 * Created by Lucifer on 2017/6/9.
 */

class CatchInfo {

    val MAIN_URL = ""

    fun catchLatestInfo() {

        val retrofit = Retrofit.Builder()
                .baseUrl(DailyInfoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(DailyInfoService::class.java)
    }

    fun catchBeforeInfo(date: String) {

    }
}
