package com.photopick.`interface`

import android.app.Activity
import com.photopick.widget.CustomImageView

/**
 * Created by szhangbiao on 2018/3/1.
 */
interface IImageLoader {
    fun displayImage(mActivity: Activity, path: String?, customImage: CustomImageView, width: Int,
        height: Int)
}