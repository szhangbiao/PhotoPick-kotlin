package com.photopick

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.photopick.`interface`.IStarter
import com.photopick.base.BaseFragment
import com.photopick.bean.PhotoBean
import com.photopick.config.PhotoPickConfig
import com.photopick.config.PickOption
import com.photopick.ui.activity.PickerActivity
import com.photopick.utils.PickConstant
import com.zhangbiao.photopick.R
/**
 * Created by szhangbiao on 2018/3/1.
 */
class PhotoPick :IStarter{
    companion object {
        val PHOENIX_RESULT = "PHOENIX_RESULT"
        val PICK_OPTION = "PHOENIX_OPTION"

        @Volatile
        private var pickConfig: PhotoPickConfig? = null

        fun config(): PhotoPickConfig {
            synchronized(javaClass) {
                if (pickConfig == null) {
                    pickConfig = PhotoPickConfig()
                }
            }
            return pickConfig!!
        }

        fun with():PickOption{
            return PickOption()
        }

        fun result(intent: Intent?): ArrayList<PhotoBean>? {
            return intent?.getSerializableExtra(PHOENIX_RESULT) as ArrayList<PhotoBean>
        }
    }

    override fun start(activity: Activity, option: PickOption, type: Int, requestCode: Int) {
        val intent = Intent(activity, PickerActivity::class.java)
        intent.putExtra(PickConstant.FRAGMENT_SECOND, PickConstant.FRAGMENT_PATH)
        val bundle = Bundle()
        bundle.putSerializable(PICK_OPTION, option)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestCode)
        activity.overridePendingTransition(R.anim.phoenix_activity_in, 0)
    }

    override fun start(fragment: Fragment, option: PickOption, type: Int, requestCode: Int) {
        val intent = Intent(fragment.activity, PickerActivity::class.java)
        intent.putExtra(PickConstant.FRAGMENT_SECOND, PickConstant.FRAGMENT_PATH)

        val bundle = Bundle()
        bundle.putSerializable(PICK_OPTION, option)
        intent.putExtras(bundle)

        fragment.startActivityForResult(intent, requestCode)
        fragment.activity.overridePendingTransition(R.anim.phoenix_activity_in, 0)
    }
}