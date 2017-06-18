package io.luciferldy.zhihudailykotlin

import android.app.Application
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by Lucifer on 2017/6/13.
 */
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    fun init() {
        Fresco.initialize(applicationContext)
    }
}