package io.luciferldy.zhihudailykotlin.presenter

import android.util.Log
import io.luciferldy.zhihudailykotlin.adapter.ThemeInfoAdapter
import io.luciferldy.zhihudailykotlin.model.ThemeDetailGson
import io.luciferldy.zhihudailykotlin.service.DailyInfoService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Lucifer on 2017/7/10.
 */
class ThemeInfoPresenter {

    companion object {
        val LOG_TAG: String = ThemeInfoPresenter::class.java.simpleName
    }

    private var mIsLoading = false
    private var mSubscription: Subscription? = null

    fun getThemeDetail(themeId: Int, adapter: ThemeInfoAdapter) {
        mIsLoading = true
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(DailyInfoService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(DailyInfoService::class.java)
        mSubscription = service.getThemeInfoList(themeId = themeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<ThemeDetailGson>() {
                    override fun onError(e: Throwable?) {
                        Log.d(LOG_TAG, "getThemeDetail occur an error.")
                        e?.printStackTrace()
                        mIsLoading = false
                    }

                    override fun onNext(t: ThemeDetailGson?) {
                        t?.let {
                            adapter.add(t)
                        }?:let {
                            Log.d(LOG_TAG, "getThemeDetail onNext, but ThemeDetailGson is null.")
                        }
                    }

                    override fun onCompleted() {
                        Log.d(LOG_TAG, "getThemeDetail onCompleted.")
                        mIsLoading = false
                    }
                })
    }

    @Synchronized fun isLoading() = mIsLoading

    fun unsubscribe() {
        if (mSubscription?.isUnsubscribed ?: false)
            mSubscription?.unsubscribe()
    }
}