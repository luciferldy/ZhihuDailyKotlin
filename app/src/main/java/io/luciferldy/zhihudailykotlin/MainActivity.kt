package io.luciferldy.zhihudailykotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val refreshLayout = findViewById(R.id.main_refresh_layout) as SwipeRefreshLayout
        refreshLayout.setOnRefreshListener {
        }
    }
}
