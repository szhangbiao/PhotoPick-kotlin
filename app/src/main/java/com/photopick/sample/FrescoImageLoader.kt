package com.photopick.sample

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.DraweeHolder
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.photopick.`interface`.IImageLoader
import com.photopick.widget.CustomImageView

/**
 * Created by szhangbiao on 2018/3/7.
 */
class FrescoImageLoader(mContext: Context) : IImageLoader {

    init {
        Fresco.initialize(mContext)
    }

    override fun displayImage(mActivity: Activity, path: String?, customImage: CustomImageView,
        width: Int, height: Int) {
        val hierarchy = GenericDraweeHierarchyBuilder(mActivity.getResources())
            .setFadeDuration(300)
//            .setPlaceholderImage(R.mipmap.gallery_pick_photo)   // 占位图
//            .setFailureImage(R.mipmap.gallery_pick_photo)       // 加载失败图
            .setProgressBarImage(ProgressBarDrawable())     // loading
            .build()
        val draweeHolder = DraweeHolder.create(hierarchy, mActivity)
        customImage.setOnImageLoaderListener(object : CustomImageView.OnImageLoadListener {
            override fun onDraw(canvas: Canvas?) {
                val drawable = draweeHolder.hierarchy.topLevelDrawable
                if (drawable == null) {
//                    customImage.setImageResource(R.mipmap.gallery_pick_photo)
                } else {
                    customImage.setImageDrawable(drawable)
                }
            }

            override fun verifyDrawable(drawable: Drawable?): Boolean {
                return drawable === draweeHolder.hierarchy.topLevelDrawable
            }

            override fun onDetach() {
                draweeHolder.onDetach()
            }

            override fun onAttach() {
                draweeHolder.onAttach()
            }
        })

        val uri = Uri.Builder()
            .scheme(UriUtil.LOCAL_FILE_SCHEME)
            .path(path)
            .build()
        val imageRequest = ImageRequestBuilder
            .newBuilderWithSource(uri)
            .setResizeOptions(ResizeOptions(width, height))
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(draweeHolder.controller)
            .setImageRequest(imageRequest)
            .build()
        draweeHolder.controller = controller
    }
}