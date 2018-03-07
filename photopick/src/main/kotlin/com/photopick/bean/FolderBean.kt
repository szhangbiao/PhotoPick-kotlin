package com.photopick.bean

/**
 * Created by szhangbiao on 2018/3/1.
 */
data class FolderBean(var folderName: String, var folderPath: String, var firstImagePath: String,
    var photoList: ArrayList<PhotoBean>)