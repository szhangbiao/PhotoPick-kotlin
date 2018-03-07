package com.photopick.`interface`

import android.app.Activity
import android.support.v4.app.Fragment
import com.photopick.base.BaseFragment
import com.photopick.config.PickOption

/**
 * Created by szhangbiao on 2018/3/5.
 */
interface IStarter{
    fun start(fragment: Fragment, option: PickOption, type: Int, requestCode: Int)
    fun start(activity: Activity, option: PickOption, type: Int, requestCode: Int)
}