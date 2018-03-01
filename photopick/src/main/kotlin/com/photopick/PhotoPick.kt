package com.photopick

import com.photopick.config.PhotoPickConfig

/**
 * Created by szhangbiao on 2018/3/1.
 */
class PhotoPick {
    companion object {
        lateinit var pickConfig: PhotoPickConfig
        fun config(): PhotoPickConfig {
            synchronized(javaClass) {
                if (pickConfig == null) {
                    pickConfig = PhotoPickConfig()
                }
            }
            return pickConfig
        }
    }
}