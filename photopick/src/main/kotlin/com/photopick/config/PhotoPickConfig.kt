package com.photopick.config

import com.photopick.`interface`.IImageLoader

/**
 * Created by szhangbiao on 2018/3/1.
 */

class PhotoPickConfig {

    private var imageLoader: IImageLoader? = null
    private var provider: String? = null

    fun getImageLoader(): IImageLoader? {
        return imageLoader
    }

    fun getConfigProvider(): String? {
        return provider
    }
    fun imageLoader(imageLoader: IImageLoader): PhotoPickConfig {
        this.imageLoader = imageLoader
        return this
    }

    fun configProvider(provider: String): PhotoPickConfig {
        this.provider = provider
        return this
    }
}