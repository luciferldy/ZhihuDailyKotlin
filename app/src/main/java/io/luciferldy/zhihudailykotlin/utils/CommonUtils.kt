package io.luciferldy.zhihudailykotlin.utils

import android.content.Context

/**
 * Created by Lucifer on 2017/7/10.
 */

class CommonUtils {

    companion object {

        var destiny = 0f

        /**
         * dip to px
         * @param context
         * *
         * @param dipValue
         * *
         * @return
         */
        fun dip2px(context: Context, dipValue: Float): Int {
            if (destiny < 0.000001f) {
                destiny = context.resources.displayMetrics.density
            }
            return (dipValue * destiny + 0.5f).toInt()
        }
    }

}
