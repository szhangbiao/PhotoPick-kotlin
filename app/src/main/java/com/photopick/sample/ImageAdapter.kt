package com.photopick.sample

import android.app.Activity
import com.photopick.base.BaseRAdapter
import com.photopick.base.RViewHolder
import com.photopick.bean.PhotoBean

/**
 * Created by szhangbiao on 2018/3/6.
 */
class ImageAdapter(private val mActivity: Activity) : BaseRAdapter<PhotoBean>(mActivity) {
    private val TYPE_ADD = 1
    private val TYPE_MEDIA = 2

    private var mOnAddMediaListener: OnAddMediaListener? = null

    override fun getItemLayoutId(viewType: Int): Int {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindData(holder: RViewHolder, position: Int, item: PhotoBean) {
        TODO(
            "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return if (mList.size == 0) 1 else mList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size || mList.size == 0) {
            TYPE_ADD
        } else {
            TYPE_MEDIA
        }
    }

    interface OnAddMediaListener {
        fun onaddMedia()
    }
}