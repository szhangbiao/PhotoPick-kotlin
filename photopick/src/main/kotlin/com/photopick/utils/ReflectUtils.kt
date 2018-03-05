package com.photopick.utils

import com.photopick.`interface`.IStarter

/**
 * Created by szhangbiao on 2018/3/5.
 */
class ReflectUtils{
    companion object {
        /**
         *
         */
        val Picker = "com.photopick.PhotoPick"

        fun loadStarter(className: String): IStarter? {
            try {
                val clazz = Class.forName(className)
                return clazz.newInstance() as IStarter
            } catch (e: Throwable) {
                e.printStackTrace()
                return null
            }
        }
    }
}