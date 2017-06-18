package io.luciferldy.zhihudailykotlin.presenter

import android.util.Log
import io.luciferldy.zhihudailykotlin.adapter.DailyInfoAdapter
import io.luciferldy.zhihudailykotlin.model.LatestInfoGson
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

class DailyInfoFragmentPresenter {
    companion object {
        val LOG_TAG: String = DailyInfoFragmentPresenter::class.java.simpleName
    }

    var mSubscription: Subscription? = null

    fun getLatestInfo(adapter: DailyInfoAdapter) {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(DailyInfoService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service: DailyInfoService = retrofit.create(DailyInfoService::class.java)
        mSubscription = service.getLastInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<LatestInfoGson>() {
                    override fun onError(e: Throwable?) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.d(LOG_TAG, "getLatestInfo occur an error.")
                        e?.printStackTrace()
                    }

                    override fun onNext(t: LatestInfoGson?) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.d(LOG_TAG, "getLatestInfo onNext top stories size ${t?.top_stories?.size}, stories size ${t?.stories?.size}")
                        val stories: ArrayList<DailyInfoAdapter.StoryWrapper> = ArrayList()
                        stories.add(DailyInfoAdapter.StoryWrapper(DailyInfoAdapter.TYPE_TOP_STORIES, "", "", "", "首页"))
                        stories.add(DailyInfoAdapter.StoryWrapper(DailyInfoAdapter.TYPE_CATEGORY, "", "", "今日热闻", "今日热闻"))
                        t?.let {
                            t.stories?.let {
//                                t.stories.asSequence().
//                                        mapTo(stories) {
//                                            stories.add(DailyInfoAdapter.StoryWrapper(DailyInfoAdapter.TYPE_STORY, it.images[0], it.id.toString(), it.title))
//                                        }

                                // more effective loop
                                t.stories.map {
                                    stories.add(DailyInfoAdapter.StoryWrapper(DailyInfoAdapter.TYPE_STORY, it.images[0], it.id.toString(), it.title, "今日热闻"))
                                }
//                                for (story in t.stories) {
//                                    stories.add(DailyInfoAdapter.StoryWrapper(DailyInfoAdapter.TYPE_STORY, story.images[0], story.id.toString(), story.title))
//                                }
                                adapter.add(topStories = t.top_stories, info = stories)
                            }


                        }

                    }

                    override fun onCompleted() {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.d(LOG_TAG, "getLatestInfo onCompleted.")
                    }
                })
    }

    fun getBeforeInfo() {

    }

    fun unsubscribe() {
        mSubscription?.unsubscribe()
    }

}