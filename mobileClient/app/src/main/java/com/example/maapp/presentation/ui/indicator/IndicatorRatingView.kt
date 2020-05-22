package com.example.maapp.presentation.ui.indicator

import android.app.ActionBar
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class IndicatorRatingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val START_X = 50f
    }

    private var indicatorPaint: Paint
    private var externalPaint: Paint

    private var startExternalY = 0f
    private var stopExternalY = 0f
    private var stopExternalX = 0f

    private var maxValueIndicator = 1000f
    private var indicatorProgress = 0f



    init {
        externalPaint = Paint().apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 24f
        }

        indicatorPaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 24f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(LinearLayout.LayoutParams.MATCH_PARENT, 34)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        startExternalY = height / 2f
        stopExternalX = width - START_X
        stopExternalY = height / 2f
        canvas.drawLine(START_X, startExternalY, stopExternalX, stopExternalY, externalPaint)

        val stopIndicatorX = stopExternalX * (indicatorProgress / maxValueIndicator)
        if(stopIndicatorX < 0.8) return

        //Отдельный случай если рузльтат меньше, чем начальный сдвиг
        if (stopIndicatorX < START_X){
            canvas.drawLine(START_X, startExternalY, START_X + stopIndicatorX, stopExternalY, indicatorPaint)
        }
        else{
            canvas.drawLine(START_X, startExternalY, stopIndicatorX, stopExternalY, indicatorPaint)
        }
    }

    fun setMaxValue(value : Float) {
        maxValueIndicator = value
    }

    fun setIndicatorProgress(value : Float) {
        indicatorProgress = value
        invalidate()
    }

}