package com.photopick.config

import com.photopick.`interface`.IImageLoader

/**
 * Created by szhangbiao on 2018/3/1.
 */

class PhotoPickConfig {

    private var imageLoader: IImageLoader? = null

    fun getImageLoader(): IImageLoader? {
        return imageLoader
    }

    fun imageLoader(imageLoader: IImageLoader): PhotoPickConfig {
        this.imageLoader = imageLoader
        return this
    }
}