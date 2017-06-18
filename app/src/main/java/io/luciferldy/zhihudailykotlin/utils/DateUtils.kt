package io.luciferldy.zhihudailykotlin.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucifer on 2017/6/18.
 */

class DateUtils {
    companion object {

        val dateFormat: DateFormat = SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINA)

        fun getFormatDate(date: Date): String {
            return dateFormat.format(date)
        }
    }
}
