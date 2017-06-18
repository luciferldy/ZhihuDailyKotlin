package io.luciferldy.zhihudailykotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.luciferldy.zhihudailykotlin.R
import io.luciferldy.zhihudailykotlin.model.ThemeListGson

/**
 * Created by Lucifer on 2017/6/13.
 */

class ThemeListAdapter(var themes: MutableList<ThemeListGson.OthersBean>,
                       val userAvatarClick: () -> Unit,
                       val favoritesClick: () -> Unit,
                       val downloadsClick: () -> Unit,
                       val drawerItemClick: (type: Int, themeId: Int, title: String) -> Unit)
    : RecyclerView.Adapter<ThemeListAdapter.BaseViewHolder>() {

    private var selectedPosition = 1

    companion object {
        val TAG_USERINFOR = -1
        val TAG_MAIN = -2
    }

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): BaseViewHolder {
        when (p1) {
            TAG_USERINFOR -> return UserInfoViewHolder(LayoutInflater.from(p0!!.context).inflate(R.layout.navi_item_userinfo, p0, false))
            TAG_MAIN -> return MainViewHolder(LayoutInflater.from(p0!!.context).inflate(R.layout.navi_item_main, p0, false))
            else -> return SimpleViewHolder(LayoutInflater.from(p0!!.context).inflate(R.layout.navi_item, p0, false))
        }
    }

    override fun onBindViewHolder(p0: BaseViewHolder?, p1: Int) {
        p0?.bindView(p1)
        p0?.onSelected(selectedPosition == p1)
    }

    override fun getItemCount(): Int = themes.size

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> TAG_USERINFOR
        1 -> TAG_MAIN
        else -> position}

    fun add (element: ThemeListGson.OthersBean) {
        themes.add(element)
        // 值得商榷
        notifyItemInserted(themes.size - 1)
    }

    fun add (element: ThemeListGson.OthersBean, index: Int) {
        if (index >= themes.size)
            return
        themes.add(index, element)
        notifyItemInserted(index)
    }

    fun add (elements: List<ThemeListGson.OthersBean>) {
        themes.addAll(elements)
        notifyItemRangeInserted(themes.size - elements.size - 1, elements.size)
    }

    fun removeAndAdd(elements: List<ThemeListGson.OthersBean>) {
        themes.clear()
        themes.addAll(elements)
        notifyDataSetChanged()
    }

    inner open class BaseViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {

        open fun bindView(position: Int) {}

        open fun onSelected(isSelected: Boolean) {}
    }

    /**
     * UserInfo Item
     */
    inner class UserInfoViewHolder(rootView: View): BaseViewHolder(rootView) {

        private val avatar: ImageView = rootView.findViewById(R.id.avatar) as ImageView
        private val favorites: ImageView = rootView.findViewById(R.id.iv_favorites) as ImageView
        private val downloads: ImageView = rootView.findViewById(R.id.iv_downloads) as ImageView

        override fun bindView (position: Int) {
            super.bindView(position)
            avatar.setOnClickListener{ userAvatarClick.invoke() }
            favorites.setOnClickListener{ favoritesClick.invoke() }
            downloads.setOnClickListener { downloadsClick.invoke() }
        }
    }

    /**
     * Main Item
     */
    inner class MainViewHolder(rootView: View): BaseViewHolder(rootView) {

        override fun bindView(position: Int) {
            super.bindView(position)

            itemView.setOnClickListener { v ->
                drawerItemClick.invoke(ThemeListAdapter.TAG_MAIN, 0, "首页")

                // change previous item background
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                // change current item background
                notifyItemChanged(selectedPosition)
            }
        }

        override fun onSelected(isSelected: Boolean) {
            super.onSelected(isSelected)
            if (isSelected) itemView?.setBackgroundResource(R.color.drawerItemSelected)
            else itemView?.setBackgroundResource(R.color.drawerItemNormal)
        }
    }

    /**
     * Others Item
     */
    inner class SimpleViewHolder(rootView: View): BaseViewHolder(rootView) {

        private val title: TextView = rootView.findViewById(R.id.title) as TextView

        override fun bindView(position: Int) {
            super.bindView(position)
            title.text = themes[position].name
//            title.setOnClickListener { drawerItemClick.invoke(position, themes[position].id, themes[position].name) }
            itemView.setOnClickListener { v ->
                // invoke listener from outside
                drawerItemClick.invoke(position, themes[position].id, themes[position].name)

                // change previous item background
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                // change current item background
                notifyItemChanged(selectedPosition)
            }
        }

        override fun onSelected(isSelected: Boolean) {
            super.onSelected(isSelected)
            if (isSelected) itemView?.setBackgroundResource(R.color.drawerItemSelected)
            else itemView?.setBackgroundResource(R.color.drawerItemNormal)
        }
    }
}