package com.photopick.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * Created by szhangbiao on 2018/3/1.
 */
class CustomImageView : AppCompatImageView {
    private var onImageLoadListener: OnImageLoadListener? = null

    constructor(context: Context) : super(context)
    @JvmOverloads constructor(context: Context, attr: AttributeSet, defStyle: Int = 0) : super(
        context, attr, defStyle)

    override fun verifyDrawable(dr: Drawable?): Boolean {
        onImageLoadListener?.verifyDrawable(dr)
        return super.verifyDrawable(dr)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        onImageLoadListener?.onDraw(canvas)
    }

    override fun onStartTemporaryDetach() {
        super.onStartTemporaryDetach()
        onImageLoadListener?.onAttach()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onImageLoadListener?.onDetach()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onImageLoadListener?.onAttach()
    }

    override fun onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach()
        onImageLoadListener?.onDetach()
    }

    interface OnImageLoadListener {
        fun onDraw(canvas: Canvas?)
        fun verifyDrawable(drawable: Drawable?): Boolean
        fun onDetach()
        fun onAttach()
    }
}