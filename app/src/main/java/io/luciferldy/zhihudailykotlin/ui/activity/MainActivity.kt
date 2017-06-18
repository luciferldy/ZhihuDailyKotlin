package io.luciferldy.zhihudailykotlin.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.luciferldy.zhihudailykotlin.R
import io.luciferldy.zhihudailykotlin.adapter.ThemeListAdapter
import io.luciferldy.zhihudailykotlin.model.ThemeListGson
import io.luciferldy.zhihudailykotlin.presenter.MainActivityPresenter
import io.luciferldy.zhihudailykotlin.ui.fragment.DailyInfoFragment
import io.luciferldy.zhihudailykotlin.ui.fragment.ThemeInfoFragment
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {

    var mDrawer: RecyclerView? = null
    var mToolbar: Toolbar? = null
    var mPresenter: MainActivityPresenter? = null
    var mCurrentFragment: Fragment? = null
    var mDailyInfoFragment: DailyInfoFragment? = null
    var mThemeInfoFragment: ThemeInfoFragment? = null

    init {
        mPresenter = MainActivityPresenter()
        mDailyInfoFragment = DailyInfoFragment()
        mDailyInfoFragment!!.setScrollCallback(object: DailyInfoFragment.ScrollCallback {
            override fun onTitleChanged(text: String) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                mToolbar?.title = text
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawer = findViewById(R.id.main_drawer) as RecyclerView
        mToolbar = findViewById(R.id.main_toolbar) as Toolbar

        initToolbar()
        initThemeList()
        initDefaultFragment()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    fun initToolbar() {
        try {
            val field: Field? = Toolbar::class.java.getDeclaredField("mTitleTextView")
            if (field != null) {
                field.isAccessible = true
                val tv: TextView = field.get(mToolbar) as TextView
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f) // f or F both works
                Log.d(LOG_TAG, "onPostCreate changed title TextView size to 16dip.")
            } else {
                Log.d(LOG_TAG, "onPostCreate field is null.")
            }
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initThemeList() {
        val userInfo: ThemeListGson.OthersBean = ThemeListGson.OthersBean()
        userInfo.id = -1
        val main: ThemeListGson.OthersBean = ThemeListGson.OthersBean()
        main.id = -2
        val adapter: ThemeListAdapter = ThemeListAdapter(
                themes = mutableListOf(userInfo, main),
                downloadsClick = {
                    Toast.makeText(baseContext, "download", Toast.LENGTH_SHORT).show()
                },
                favoritesClick = {
                    Toast.makeText(baseContext, "favorites", Toast.LENGTH_SHORT).show()
                },
                userAvatarClick = {
                    Toast.makeText(baseContext, "avatar", Toast.LENGTH_SHORT).show()
                },
                drawerItemClick = {
                    type, themeId, title ->
                    Toast.makeText(baseContext, "drawer item position $type, $themeId, $title", Toast.LENGTH_SHORT).show()
                    switchTheme(type, themeId, title)
                }
                )
        mDrawer?.layoutManager = LinearLayoutManager(baseContext)
        mDrawer?.adapter = adapter
        val drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            override fun syncState() {
                super.syncState()
                Log.d(LOG_TAG, "syncState.")
            }

            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                Log.d(LOG_TAG, "onDrawerOpened.")
            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                Log.d(LOG_TAG, "onDrawerClosed.")
            }
        }
        drawerLayout.addDrawerListener(toggle)
        drawerLayout.post { toggle.syncState() }

        mPresenter?.getThemeList(adapter)
    }

    fun initDefaultFragment() {
        setCurrentFragment(mDailyInfoFragment!!)
        toFragment(mDailyInfoFragment!!)
    }

    fun switchTheme(type: Int, themeId: Int, title: String) {
        // click main item
        if (type == ThemeListAdapter.TAG_MAIN) {
            // change title content
            mDailyInfoFragment?.let {
                toFragment(mDailyInfoFragment!!)
            }
        } else {
            if (mThemeInfoFragment == null) {
                mThemeInfoFragment = ThemeInfoFragment()
                mThemeInfoFragment!!.themeId = themeId
            } else {
                mThemeInfoFragment!!.themeId = themeId
            }
            toFragment(mThemeInfoFragment!!)
            // 重新拉取新数据
            if (mThemeInfoFragment!!.isAdded) {
                mThemeInfoFragment!!.updateThemeInfo()
            }
        }

    }

    private fun toFragment(toFragment: Fragment) {
        mCurrentFragment?.let {
            Log.d(LOG_TAG, "switchFragment current fragment is ${mCurrentFragment?.javaClass?.simpleName}, to fragment is ${toFragment.javaClass.simpleName}")
            Log.d(LOG_TAG, "to fragment is Add? ${toFragment.isAdded}, tag is ${toFragment.tag}")

            if (mCurrentFragment!!.isAdded) {
                supportFragmentManager.beginTransaction()
                        .hide(mCurrentFragment)
                        .show(toFragment)
                        .commit()
            } else {
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, toFragment, toFragment.javaClass.simpleName)
                        .show(toFragment)
                        .commit()
            }
            setCurrentFragment(toFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
    }

    companion object {
        var LOG_TAG: String = MainActivity::class.java.simpleName
    }
}
