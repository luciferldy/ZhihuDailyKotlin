package io.luciferldy.zhihudailykotlin.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.luciferldy.zhihudailykotlin.R
import io.luciferldy.zhihudailykotlin.adapter.DailyInfoAdapter
import io.luciferldy.zhihudailykotlin.presenter.DailyInfoFragmentPresenter
import io.luciferldy.zhihudailykotlin.ui.activity.DetailsActivity

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DailyInfoFragment.ScrollCallback] interface
 * to handle interaction events.
 * Use the [DailyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 * About auto generated. Just for learn kotlin grammar.
 */
class DailyInfoFragment : Fragment() {

    private var mPresenter: DailyInfoFragmentPresenter = DailyInfoFragmentPresenter()
    private var mAdapter: DailyInfoAdapter? = null
    private var mScrollCallback: ScrollCallback? = null

    init {
//        mPresenter = DailyInfoFragmentPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root: View = inflater!!.inflate(R.layout.fragment_dailyinfo, container, false)
        if (root is SwipeRefreshLayout) {
            //
            root.isEnabled = false
        }
        val recyclerView: RecyclerView = root.findViewById(R.id.info_list) as RecyclerView
        val linerLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linerLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
                val des: String? = mAdapter?.getDescription(linerLayoutManager.findFirstVisibleItemPosition())
                if (!TextUtils.isEmpty(des)) {
                    mScrollCallback?.onTitleChanged(des!!)
                } else {
                    mScrollCallback?.onTitleChanged("首页")
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        mAdapter = DailyInfoAdapter(object: DailyInfoAdapter.FragmentCallback {
            override fun toActivity(id: String) {
                // this 的用法
                this@DailyInfoFragment.toActivity(id)
            }
        })
        recyclerView.adapter = mAdapter
        mPresenter.getLatestInfo(mAdapter!!)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun setScrollCallback(callback: ScrollCallback) {
        mScrollCallback = callback
    }

    fun toActivity(id: String) {
        val intent: Intent = Intent(activity, DetailsActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putString("id", id)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    interface ScrollCallback {
        fun onTitleChanged(text: String)
    }

    companion object {
        val TYPE_DAILYINFO = 1
        val TYPE_THEMEINFO = 2
        val FRAGMENT_TYPE = "fragment_type"

        fun newInstance():DailyInfoFragment {
            return DailyInfoFragment()
        }
    }
}
