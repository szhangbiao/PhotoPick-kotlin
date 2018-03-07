package com.photopick.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.photopick.adapter.FolderAdapter
import com.photopick.bean.FolderBean
import com.photopick.utils.ScreenUtil
import com.photopick.utils.StringUtils
import com.zhangbiao.photopick.R

/**
 * Created by szhangbiao on 2018/3/1.
 */
class FolderPopupWindow(private val mActivity: Activity) : PopupWindow() {

    private val window: View = LayoutInflater.from(mActivity).inflate(
        R.layout.photo_pick_window_folder, null)
    private var recyclerView: RecyclerView? = null
    private var adapter: FolderAdapter? = null
    private val animationIn: Animation
    private val animationOut: Animation
    private var isDismiss = false
    private var llRoot: LinearLayout? = null
    private var tvTitle: TextView? = null
    private val drawableUp: Drawable
    private val drawableDown: Drawable

    init {
        this.contentView = window
        this.width = ScreenUtil.getScreenWidth(mActivity)
        this.height = ScreenUtil.getScreenHeight(mActivity)
        this.animationStyle = R.style.style_window
        this.isFocusable = true
        this.isOutsideTouchable = true
        this.update()
        this.setBackgroundDrawable(ColorDrawable(Color.argb(123, 0, 0, 0)))
        drawableUp = ContextCompat.getDrawable(mActivity, R.drawable.photo_pick_arrow_up)
        drawableDown = ContextCompat.getDrawable(mActivity, R.drawable.photo_pick_arrow_down)
        animationIn = AnimationUtils.loadAnimation(mActivity, R.anim.phoenix_album_show)
        animationOut = AnimationUtils.loadAnimation(mActivity, R.anim.phoenix_album_dismiss)
        initView()
    }

    private fun initView() {
        llRoot = window.findViewById(R.id.id_ll_root) as LinearLayout
        adapter = FolderAdapter(mActivity)
        recyclerView = window.findViewById(R.id.folder_list) as RecyclerView
        recyclerView!!.layoutParams.height = (ScreenUtil.getScreenHeight(mActivity) * 0.6).toInt()
        recyclerView!!.addItemDecoration(
            RecycleViewDivider(mActivity, LinearLayoutManager.HORIZONTAL,
                ScreenUtil.dip2px(mActivity, 0f),
                ContextCompat.getColor(mActivity, R.color.transparent)))
        recyclerView!!.layoutManager = LinearLayoutManager(mActivity)
        recyclerView!!.adapter = adapter
        llRoot?.setOnClickListener({
            dismiss()
        })
    }

    fun bindFolder(folders: ArrayList<FolderBean>) {
//        adapter!!.setMimeType(mimeType)
        adapter!!.updateDataList(folders)
    }

    fun setPictureTitleView(picture_title: TextView) {
        this.tvTitle = picture_title
    }

    override fun showAsDropDown(anchor: View) {
        this.showAsDropDown(anchor, 0, 0)
    }

    override fun showAsDropDown(anchor: View, xOffset: Int, yOffset: Int) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, xOffset, yOffset)
        } else {
            // 适配 android 7.0
            val location = IntArray(2)
            anchor.getLocationOnScreen(location)
            // 7.1 版本处理
            if (Build.VERSION.SDK_INT >= 25) {
                //【note!】Gets the screen height without the virtual key
                val wm = mActivity
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val screenHeight = wm.defaultDisplay.height
                /*
                 * PopupWindow height for match_parent,
                 * will occupy the entire screen, it needs to do special treatment in Android 7.1
                */
                height = screenHeight - location[1] - anchor.height - yOffset
            }
            showAtLocation(anchor, Gravity.NO_GRAVITY, xOffset,
                location[1] + anchor.getHeight() + yOffset)
        }
    }

    fun setOnItemClickListener(onItemClickListener: FolderAdapter.OnItemClickListener) {
        adapter!!.setOnItemClickListener(onItemClickListener)
    }

    override fun dismiss() {
        if (isDismiss) {
            return
        }
        StringUtils.modifyTextViewDrawable(tvTitle!!, drawableDown, 2)
        isDismiss = true
        recyclerView!!.startAnimation(animationOut)
        dismiss()
        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {
                isDismiss = false
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    dismiss4Pop()
                } else {
                    super@FolderPopupWindow.dismiss()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    /**
     * 在android4.1.1和4.1.2版本关闭PopWindow
     */
    private fun dismiss4Pop() {
        Handler().post { super@FolderPopupWindow.dismiss() }
    }

}