package com.photopick.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.photopick.PhotoPick
import com.photopick.base.FragmentActivity
import com.photopick.utils.PickConstant
import com.zhangbiao.photopick.R

/**
 * Created by szhangbiao on 2018/3/5.
 */
class PickerActivity: FragmentActivity() {

    override fun getContextViewId(): Int {
        return R.id.common_container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragName = intent.getStringExtra(PickConstant.FRAGMENT_SECOND)
            var fragment: Fragment? = null
            if (!TextUtils.isEmpty(fragName)){
                fragment=Fragment.instantiate(this, fragName)
            }
            if (fragment != null) {
                if (intent.extras != null) {
                    fragment.arguments = intent.extras
                }
                supportFragmentManager
                    .beginTransaction()
                    .add(getContextViewId(), fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
            } else {
                finish()
            }
        }

        if (PhotoPick.config().getImageLoader() == null) {
            throw IllegalArgumentException("The image loader should be set in application")
        }
    }
}