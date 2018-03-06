package com.photopick.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.zhangbiao.photopick.R

class LoadingDialog(context: Context) : Dialog(context, R.style.style_dialog) {

    init {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        window.setWindowAnimations(R.style.style_window)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_pick_dialog_loading)
    }

}