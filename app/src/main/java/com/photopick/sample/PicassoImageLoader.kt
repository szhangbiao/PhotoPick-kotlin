package com.photopick.sample

import android.app.Activity
import com.photopick.`interface`.IImageLoader
import com.photopick.widget.CustomImageView
import com.squareup.picasso.Picasso

/**
 * Created by szhangbiao on 2018/3/7.
 */
class PicassoImageLoader : IImageLoader {
    override fun displayImage(mActivity: Activity, path: String?, customImage: CustomImageView,
        width: Int, height: Int) {
        Picasso.with(mActivity)
            .load("file://$path")
            .resize(width, height)
            .error(R.mipmap.ic_launcher)
            .into(customImage)
    }
}