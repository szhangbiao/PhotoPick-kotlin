package com.photopick.sample

import android.app.Activity
import com.photopick.`interface`.IImageLoader
import com.photopick.widget.CustomImageView

/**
 * Created by szhangbiao on 2018/3/7.
 */
class GlideImageLoader : IImageLoader {
    override fun displayImage(mActivity: Activity, path: String?, customImage: CustomImageView,
        width: Int, height: Int) {

    }
}