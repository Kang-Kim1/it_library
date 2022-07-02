package com.example.kanglibrary.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class LinedEditText : AppCompatEditText {
    private var mRect: Rect
    private var mPaint: Paint

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mRect = Rect()
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#38a3d1")
        this.setBackgroundColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        val height = height
        val line_height = lineHeight
        var count = height / line_height
        if (lineCount > count) count = lineCount
        val r: Rect = mRect
        val paint: Paint = mPaint
        var baseline = getLineBounds(0, r)
        for (i in 0 until count) {
            canvas.drawLine(
                r.left.toFloat(),
                (baseline + 2).toFloat(),
                r.right.toFloat(),
                (baseline + 2).toFloat(),
                paint
            )
            baseline += lineHeight
        }
        super.onDraw(canvas)
    }
}