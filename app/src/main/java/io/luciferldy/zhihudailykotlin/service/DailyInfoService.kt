package io.luciferldy.zhihudailykotlin.service

import io.luciferldy.zhihudailykotlin.model.LatestInfoGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Lucifer on 2017/6/9.
 */

interface DailyInfoService {

    @get:GET("news/latest")
    val latestInfo: Call<LatestInfoGson>

    @GET("news/before/{date}")
    fun getBeforeInfo(@Path("date") date: String): Call<LatestInfoGson>

    companion object {

        val BASE_URL = "http://news-at.zhihu.com/api/4/"
    }
}
