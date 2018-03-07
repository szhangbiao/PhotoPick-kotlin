package com.photopick.base

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.SparseArray
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by szhangbiao on 2018/3/1.
 */
class RViewHolder(itemView: View) : ViewHolder(itemView) {
    private var mViews: SparseArray<View> = SparseArray()
    /**
     * 通过id获取View
     */
    fun <T : View> findViewById(@IdRes viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view!! as T
    }

    /**
     * 通过id获取View
     */
    fun getView(@IdRes viewId: Int): View = findViewById(viewId)

    /**
     * 通过id获取TextView
     */
    fun getTextView(@IdRes viewId: Int): TextView = findViewById(viewId)

    /**
     * 通过id获取Button
     */
    fun getButton(@IdRes viewId: Int): Button = findViewById(viewId)

    /**
     * 通过id获取ImageView
     */
    fun getImageView(@IdRes viewId: Int): ImageView = findViewById(viewId)

    /**
     * 通过id获取ImageButton
     */
    fun getImageButton(@IdRes viewId: Int): ImageButton = findViewById(viewId)

    /**
     * 通过id获取EditText
     */
    fun getEditText(@IdRes viewId: Int): EditText = findViewById(viewId)

    /**
     * 给TextView设置值
     */
    fun setText(@IdRes viewId: Int, @NonNull value: String): RViewHolder {
        val text: TextView? = getTextView(viewId)
        text?.text = value
        return this
    }

    /**
     * 给View设置背景
     */
    fun setBackground(@IdRes viewId: Int, @DrawableRes drawableId: Int): RViewHolder {
        val view: View? = findViewById(viewId)
        view?.setBackgroundResource(drawableId)
        return this
    }

    /**
     * 给View设置点击事件
     */
    fun setOnClickListener(@IdRes viewId: Int, @NonNull listener: OnClickListener): RViewHolder {
        val view: View? = findViewById(viewId)
        view?.setOnClickListener(listener)
        return this
    }
}