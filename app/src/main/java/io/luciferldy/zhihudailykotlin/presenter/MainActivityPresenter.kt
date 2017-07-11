package io.luciferldy.zhihudailykotlin.presenter

import android.util.Log
import io.luciferldy.zhihudailykotlin.adapter.ThemeListAdapter
import io.luciferldy.zhihudailykotlin.model.ThemeListGson
import io.luciferldy.zhihudailykotlin.service.DailyInfoService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Lucifer on 2017/6/13.
 */

class MainActivityPresenter {

    companion object {
        val LOG_TAG: String = MainActivityPresenter::class.java.simpleName
    }

    var mSubscription: Subscription? = null

    fun getThemeList(adapter: ThemeListAdapter) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(DailyInfoService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service: DailyInfoService = retrofit.create(DailyInfoService::class.java)
        mSubscription = service.getThemeList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<ThemeListGson>() {
                    override fun onNext(t: ThemeListGson?) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.d(LOG_TAG, "getThemeList onNext others size ${t?.others?.size}")
                        t?.let {
                            adapter.add(t.others as List<ThemeListGson.OthersBean>)
                        }
                    }

                    override fun onError(e: Throwable?) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.d(LOG_TAG, "getThemeList occur an error.")
                        e?.printStackTrace()
                    }

                    override fun onCompleted() {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    fun unsubscribe() {
        if (mSubscription?.isUnsubscribed ?: false)
        mSubscription?.unsubscribe()
    }
}