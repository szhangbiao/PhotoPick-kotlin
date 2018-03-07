package com.photopick.bean

import java.io.Serializable

/**
 * Created by szhangbiao on 2018/3/1.
 */
data class PhotoBean(var photoPath: String, var photoName: String?,
    var addTime: Long) : Serializable