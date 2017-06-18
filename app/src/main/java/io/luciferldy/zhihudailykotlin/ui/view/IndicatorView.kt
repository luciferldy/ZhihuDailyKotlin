package io.luciferldy.zhihudailykotlin.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import io.luciferldy.zhihudailykotlin.R

/**
 * Created by Lucifer on 2017/6/14.
 */
class IndicatorView(context: Context, attrs: AttributeSet): View(context, attrs) {

    companion object {
        val LOG_TAG: String = IndicatorView::class.java.simpleName
    }

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mPaint: Paint = Paint()
    private var mPoints: ArrayList<PointF> = ArrayList()
    private var dotRadius: Float = 0f
    private var dotDistance: Int = 0
    private var dotCount: Int = 0
    private var normalColor: Int = 0
    private var selectedColor: Int = 0
    private var cursorPointF: PointF = PointF()

    init {
        Log.d(LOG_TAG, "init")

        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView, 0, 0)
        try {
            dotRadius = a.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_radius, 20) * 0.5f
            dotDistance = a.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_distance, 20)
            dotCount = a.getInt(R.styleable.IndicatorView_indicator_count, 5)
            normalColor = a.getColor(R.styleable.IndicatorView_indicator_normal_color, resources.getColor(R.color.md_white))
            selectedColor = a.getColor(R.styleable.IndicatorView_indicator_selected_color, resources.getColor(R.color.md_grey_700))
        } finally {
            a.recycle()
        }

        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                viewTreeObserver.removeOnPreDrawListener(this)
                initPoints()
                return true
            }
        })

        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth = w
        this.mHeight = h
        Log.d(LOG_TAG, "width: $mWidth, height: $mHeight")
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        mPaint.color = normalColor
        for (pointF in mPoints) {
            canvas!!.drawCircle(pointF.x, pointF.y, dotRadius, mPaint)
        }
        mPaint.color = selectedColor
        canvas!!.drawCircle(cursorPointF.x, cursorPointF.y, dotRadius + 1, mPaint)
    }

    /**
     * 设置当前的位置
     */
    fun setCurrentPosition(position: Int) {
        if (position >= mPoints.size)
            return
        cursorPointF.x = mPoints[position].x
        invalidate()
    }

    /**
     * 移动 Indicator
     * @param position PointF 位置的 index
     * @param positionOffset 偏移的比例
     * @param positionOffsetPixels 偏移的像素
     */
    fun moveIndicator(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position >= mPoints.size)
            return
        cursorPointF.x = mPoints[position].x + positionOffset * (dotDistance + dotRadius * 2)
        invalidate()
    }

    private fun initPoints() {

        val left = (mWidth - dotRadius * 2 * dotCount - (dotCount - 1) * dotDistance) * 0.5f
        for (i in 0 until dotCount) {
            val pointF: PointF = PointF()
            pointF.x = left + dotRadius * 2 * i + dotDistance * i + dotRadius
            pointF.y = mHeight - dotRadius - 2 // 减去 2 Pixel
            mPoints.add(pointF)
        }

        cursorPointF.x = mPoints[0].x
        cursorPointF.y = mPoints[0].y

        invalidate()
    }
}