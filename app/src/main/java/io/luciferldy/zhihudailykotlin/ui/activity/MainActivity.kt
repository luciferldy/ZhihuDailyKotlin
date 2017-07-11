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
import android.view.Gravity
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

    private val mPresenter = MainActivityPresenter()
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar
    private lateinit var mCurrentFragment: Fragment
    /***
     * Define [mDailyInfoFragment] in constructor, so no necessary declared as null.
     * But it can be joint with assignment, so auto complete.
     */
    private var mDailyInfoFragment: DailyInfoFragment? = null
    private var mThemeInfoFragment: ThemeInfoFragment? = null

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            Log.d(LOG_TAG, "savedInstanceState is not null.")
            mDailyInfoFragment = supportFragmentManager.findFragmentByTag(DailyInfoFragment::class.java.simpleName) as DailyInfoFragment
            mThemeInfoFragment = supportFragmentManager.findFragmentByTag(ThemeInfoFragment::class.java.simpleName) as ThemeInfoFragment
            Log.d(LOG_TAG, "DailyInfoFragment is null ${mDailyInfoFragment == null}, ThemeInfoFragment is null ${mThemeInfoFragment == null}.")
        }

        setContentView(R.layout.activity_main)

        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mRecyclerView = findViewById(R.id.drawer_recycler) as RecyclerView
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
                    mDrawerLayout.closeDrawer(Gravity.LEFT)
                })
        mRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        mRecyclerView.adapter = adapter
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

        mPresenter.getThemeList(adapter)
    }

    fun initDefaultFragment() {

        mDailyInfoFragment?: let {
            mDailyInfoFragment = DailyInfoFragment()
        }

        mDailyInfoFragment!!.setScrollCallback(object: DailyInfoFragment.ScrollCallback {
            override fun onTitleChanged(text: String) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                mToolbar.title = text
            }
        })

        setCurrentFragment(mDailyInfoFragment!!)
        toFragment(mDailyInfoFragment!!)
    }

    fun switchTheme(type: Int, themeId: Int, title: String) {

        mToolbar.title = title

        // click main item
        if (type == ThemeListAdapter.TAG_MAIN) {

            toFragment(mDailyInfoFragment!!)
        } else {

            mThemeInfoFragment?: let {
                mThemeInfoFragment = ThemeInfoFragment()
                val bundle = Bundle()
                bundle.putInt(ThemeInfoFragment.THEME_ID, themeId)
                mThemeInfoFragment!!.arguments = bundle
            }
            mThemeInfoFragment!!.themeId = themeId
            toFragment(mThemeInfoFragment!!)
            // 重新拉取新数据
            if (mThemeInfoFragment!!.isAdded) {
                mThemeInfoFragment!!.updateThemeInfo()
            } else {
                Log.d(LOG_TAG, "switchTheme ThemeInfoFragment has not added.")
            }
        }

    }

    private fun toFragment(toFragment: Fragment) {
        mCurrentFragment.let {
            Log.d(LOG_TAG, "switchFragment current fragment is ${mCurrentFragment.javaClass.simpleName}, to fragment is ${toFragment.javaClass.simpleName}")
            Log.d(LOG_TAG, "to fragment is Add? ${toFragment.isAdded}, tag is ${toFragment.tag}")

            if (toFragment.isAdded) {
                supportFragmentManager.beginTransaction()
                        .hide(mCurrentFragment)
                        .show(toFragment)
                        .commit()
            } else {
                supportFragmentManager.beginTransaction()
                        .hide(mCurrentFragment)
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
        val LOG_TAG: String = MainActivity::class.java.simpleName
    }
}
