package io.luciferldy.zhihudailykotlin.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import io.luciferldy.zhihudailykotlin.R
import io.luciferldy.zhihudailykotlin.model.ThemeDetailGson
import io.luciferldy.zhihudailykotlin.utils.CommonUtils

/**
 * Created by Lucifer on 2017/6/17.
 */
class ThemeInfoAdapter: RecyclerView.Adapter<ThemeInfoAdapter.BaseViewHolder>() {

    private val mThemeInfos: ArrayList<ThemeInfoData> = ArrayList()
    private val mEditors: ArrayList<EditorData> = ArrayList()
    private var mBackGround: BackGroundData? = null
    private var mClickCallback: ClickCallback? = null

    companion object {
        val LOG_TAG: String = ThemeInfoAdapter::class.java.simpleName

        val TYPE_BACKGROUND = -1
        val TYPE_EDITORS = -2
        val TYPE_THEMEINFO = -3
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        when (viewType) {
            TYPE_BACKGROUND -> return BackGroundHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.theme_info_intro, parent, false))
            TYPE_EDITORS -> return EditorHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.theme_info_editos, parent, false))
            TYPE_THEMEINFO -> return ThemeInfoHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.theme_info_content, parent, false))
            else -> return BaseViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.view_empty, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        if (holder is BackGroundHolder) {
            holder.onBind(mBackGround!!)
        } else if (holder is EditorHolder) {
            holder.onBind(mEditors)
        } else if (holder is ThemeInfoHolder) {
            holder.onBind(mThemeInfos[position])
        } else {
            // balabala
            Log.d(LOG_TAG, "onBindViewHolder type not match.")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mThemeInfos[position].innerType
    }

    override fun getItemCount(): Int {
        return mThemeInfos.size
    }

    fun add(data: ThemeDetailGson) {
        // BackGround
        mBackGround = BackGroundData(data.description, data.background)
        // Editor information
        mEditors.clear()
        data.editors.map {
            editorsBean -> mEditors.add(EditorData(editorsBean.id, editorsBean.avatar, editorsBean.name))
        }
        // Theme information, first set placeholder for BackGround and Editor
        mThemeInfos.clear()
        mThemeInfos.add(ThemeInfoData(innerType = TYPE_BACKGROUND))
        mThemeInfos.add(ThemeInfoData(innerType = TYPE_EDITORS))
        data.stories.map {
            storiesBean -> mThemeInfos.add(ThemeInfoData(storiesBean.id,
                storiesBean.type,
                storiesBean.title,
                storiesBean.isMultipic,
                storiesBean.images,
                TYPE_THEMEINFO))
        }

        notifyDataSetChanged()
    }

    fun setClickCallback(callback: ClickCallback) {
        mClickCallback = callback
    }

    open inner class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    inner class BackGroundHolder(itemView: View) : BaseViewHolder(itemView) {

        private var description = itemView.findViewById(R.id.description) as TextView
        private var image = itemView.findViewById(R.id.background) as SimpleDraweeView

        init {
        }

        fun onBind(data: BackGroundData) {
            description.text = data.description
            image.setImageURI(data.background)
        }
    }

    inner class EditorHolder(itemView: View) : BaseViewHolder(itemView) {

        fun onBind(datas: List<EditorData>) {

            // 这一行有些奇怪
            itemView as LinearLayout
            // 这一层判断可以删掉，断言 itemView 为 LinearLayout
            // LinearLayout 的高度
            val size = itemView.layoutParams.height
            itemView.removeViews(1, itemView.childCount - 1)
            for (data in datas) {
                val avatar = SimpleDraweeView(itemView.context)
                itemView.addView(avatar)
                val params = avatar.layoutParams as LinearLayout.LayoutParams
                params.width = size
                params.height = size
                params.rightMargin = CommonUtils.dip2px(itemView.context, 12f)
                avatar.layoutParams = params
                val roundingParams = RoundingParams()
                roundingParams.roundAsCircle = true
                avatar.hierarchy.roundingParams = roundingParams
                avatar.setImageURI(data.avatar)
            }
        }
    }

    inner class ThemeInfoHolder(itemView: View) : BaseViewHolder(itemView) {

        private val title = itemView.findViewById(R.id.title) as TextView
        private val image = itemView.findViewById(R.id.image) as SimpleDraweeView

        fun onBind(data: ThemeInfoData) {
            title.text = data.title
            if (data.multipic) {
                // balabala
            }
            image.setImageURI(data.images?.get(0)?:"")
            itemView.setOnClickListener {
                mClickCallback?.clickThemeInfo(data.id)
            }
        }
    }

    data class BackGroundData(val description: String, val background: String)

    data class EditorData(val id: Int, val avatar: String, val name: String)

    // 使用 ThemeInfoData 所有数据的载体了，把 Editor 的信息也封装在 innerType 里
    data class ThemeInfoData(val id: Int = 0,
                             val type: Int = 0,
                             val title: String? = null,
                             val multipic: Boolean = false,
                             val images: List<String>? = null,
                             val innerType: Int)

    interface ClickCallback {
        fun clickEditor()
        fun clickThemeInfo(infoId: Int)
    }

}