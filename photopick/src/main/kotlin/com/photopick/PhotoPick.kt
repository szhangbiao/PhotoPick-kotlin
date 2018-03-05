package com.photopick

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.photopick.`interface`.IStarter
import com.photopick.config.PhotoPickConfig
import com.photopick.config.PickOption
import com.photopick.ui.activity.PickerActivity
import com.zhangbiao.photopick.R
/**
 * Created by szhangbiao on 2018/3/1.
 */
class PhotoPick :IStarter{
    companion object {
        val PICK_OPTION:String="PHOENIX_OPTION"
        lateinit var pickConfig: PhotoPickConfig
        fun config(): PhotoPickConfig {
            synchronized(javaClass) {
                if (pickConfig == null) {
                    pickConfig = PhotoPickConfig()
                }
            }
            return pickConfig
        }

        fun with():PickOption{
            return PickOption()
        }
    }

    override fun start(activity: Activity, option: PickOption, type: Int, requestCode: Int) {

    }

    override fun start(fragment: Fragment, option: PickOption, type: Int, requestCode: Int) {
        val intent = Intent(fragment.activity, PickerActivity::class.java)
        intent.putExtra(PICK_OPTION, option)
        fragment.startActivityForResult(intent, requestCode)
        fragment.activity.overridePendingTransition(R.anim.phoenix_activity_in, 0)
    }
}