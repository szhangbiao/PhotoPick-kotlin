package com.photopick.adapter

import android.app.Activity
import com.photopick.base.BaseRAdapter
import com.photopick.base.RViewHolder
import com.photopick.bean.PhotoBean

/**
 * Created by szhangbiao on 2018/3/1.
 */
class PhotoAdapter(private val mActivity: Activity,
    mList: MutableList<PhotoBean>) : BaseRAdapter<PhotoBean>(mActivity, mList) {

    override fun getItemLayoutId(viewType: Int): Int {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindData(holder: RViewHolder, position: Int, item: PhotoBean) {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}