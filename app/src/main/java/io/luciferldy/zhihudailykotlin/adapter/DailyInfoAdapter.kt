package io.luciferldy.zhihudailykotlin.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import io.luciferldy.zhihudailykotlin.R
import io.luciferldy.zhihudailykotlin.model.LatestInfoGson
import io.luciferldy.zhihudailykotlin.ui.view.IndicatorView

/**
 * Created by Lucifer on 2017/6/14.
 */
class DailyInfoAdapter(val callback: FragmentCallback) : RecyclerView.Adapter<DailyInfoAdapter.BaseViewHolder>() {

    // 不加修饰符的变量默认是 public
    private var mInfo: ArrayList<StoryWrapper> = ArrayList()
    private var mTopStories: ArrayList<LatestInfoGson.TopStoriesBean> = ArrayList()
    private val mStoryOnClickListener: View.OnClickListener = View.OnClickListener {
        view ->
        Toast.makeText(view.context, "click info ${view.tag}", Toast.LENGTH_SHORT).show()
        callback.toActivity(mInfo[view.tag as Int].url)
    }

    companion object {
        val LOG_TAG: String = DailyInfoAdapter::class.java.simpleName
        val TYPE_TOP_STORIES = -1
        val TYPE_STORY = -2
        val TYPE_CATEGORY = -3
    }

    override fun getItemViewType(position: Int): Int {
        return mInfo[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(LOG_TAG, "onCreateViewHolder viewType: $viewType")
        when(viewType) {
            TYPE_TOP_STORIES -> return TopStoriesHolder(LayoutInflater.from(parent?.context).inflate(R.layout.info_item_topstories, parent, false))
            TYPE_CATEGORY -> return CategoryViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.info_item_category, parent, false))
            TYPE_STORY -> return StoryHolder(LayoutInflater.from(parent?.context).inflate(R.layout.info_item_story, parent, false))
            else -> return BaseViewHolder(View(parent?.context))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(LOG_TAG, "onBindViewHolder viewHolder: ${holder!!::class.java.simpleName}, position: $position")
        // a little confused. !! can kill after ?
        holder.bindView(position)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return mInfo.size
    }

    fun add(topStories: List<LatestInfoGson.TopStoriesBean>, info: List<StoryWrapper>) {
        mTopStories.clear()
        mTopStories.addAll(topStories)

        mInfo.clear()
        mInfo.addAll(info)
        notifyDataSetChanged()
    }

    fun add(info: ArrayList<StoryWrapper>) {
        mInfo.addAll(info)
        notifyDataSetChanged()
    }

    fun getDescription(position: Int): String {
        if (position < mInfo.size) {
            when(mInfo[position].type) {
                TYPE_TOP_STORIES -> return mInfo[position].des
                TYPE_CATEGORY -> return mInfo[position].des
                TYPE_STORY -> return mInfo[position].des
                else -> return ""
            }
        } else {
            return ""
        }
    }

    inner open class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
        }

        open fun bindView(position: Int) {

        }
    }

    /**
     * 上方轮播动画的 Holder
     */
    inner class TopStoriesHolder(itemView: View): BaseViewHolder(itemView) {

        var mViewPager: ViewPager? = null
        var mIndicator: IndicatorView? = null

        init {
            mViewPager = itemView.findViewById(R.id.top_stories) as ViewPager
            mIndicator = itemView.findViewById(R.id.story_indicator) as IndicatorView
        }

        override fun bindView(position: Int) {
            super.bindView(position)
            val stories: ArrayList<View> = ArrayList()
            val listener: View.OnClickListener = View.OnClickListener {
                view ->
                Toast.makeText(view.context, "click top stories ${view.tag}", Toast.LENGTH_SHORT).show()
                callback.toActivity(mTopStories[view.tag as Int].id.toString())
            }
            for (i in 0 until mTopStories.size) {
                val v: View = LayoutInflater.from(itemView.context).inflate(R.layout.top_stories_item, null)
                v.tag = i
                v.setOnClickListener(listener)
                val image: SimpleDraweeView = v.findViewById(R.id.image) as SimpleDraweeView
                image.setImageURI(mTopStories[i].image)
                val description: TextView = v.findViewById(R.id.description) as TextView
                description.text = mTopStories[i].title
                stories.add(v)
            }

            mViewPager!!.adapter = object : PagerAdapter() {
                override fun getCount(): Int {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    return stories.size
                }

                override fun isViewFromObject(p0: View?, p1: Any?): Boolean {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    return p0 == p1
                }

                override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                    Log.d(LOG_TAG, "PagerAdapter instantiateItem position: $position")
                    container?.addView(stories[position])
                    return stories[position]
                }

                override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                    Log.d(LOG_TAG, "PagerAdapter destroyItem position: $position")
                    container?.removeView(stories[position])
                }

            }
            mViewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    mIndicator?.moveIndicator(p0, p1, p2)
                }

                override fun onPageSelected(p0: Int) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    mIndicator?.setCurrentPosition(p0)
                }
            })
        }
    }

    inner class CategoryViewHolder(itemView: View): BaseViewHolder(itemView) {

        override fun bindView(position: Int) {
            (itemView.findViewById(R.id.category) as TextView).text = mInfo[position].title
        }
    }

    inner class StoryHolder(itemView: View): BaseViewHolder(itemView) {

        override fun bindView(position: Int) {
            val title: TextView = itemView.findViewById(R.id.title) as TextView
            title.text = mInfo[position].title
            val image: SimpleDraweeView = itemView.findViewById(R.id.image) as SimpleDraweeView
            image.setImageURI(mInfo[position].imageUrl)
            itemView.tag = position
            itemView.setOnClickListener(mStoryOnClickListener)
        }
    }

    data class StoryWrapper(val type: Int, val imageUrl: String, val url: String, val title: String, val des: String)

    interface FragmentCallback {
        fun toActivity(id: String)
    }
}