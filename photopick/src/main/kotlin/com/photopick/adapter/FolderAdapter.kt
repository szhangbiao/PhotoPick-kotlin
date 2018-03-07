package com.photopick.adapter

import android.app.Activity
import android.view.View
import com.photopick.PhotoPick
import com.photopick.base.BaseRAdapter
import com.photopick.base.RViewHolder
import com.photopick.bean.FolderBean
import com.photopick.bean.PhotoBean
import com.photopick.utils.ScreenUtil
import com.zhangbiao.photopick.R

/**
 * Created by szhangbiao on 2018/3/1.
 */

class FolderAdapter(private val mActivity: Activity) : BaseRAdapter<FolderBean>(mActivity) {
    private var mSelector: Int = 0
    private var onItemClickListener: OnItemClickListener? = null
    private val width = ScreenUtil.dip2px(mContext, 50.0f)
    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.photo_pick_item_folder
    }

    override fun bindData(holder: RViewHolder, position: Int, item: FolderBean) {
        holder.getTextView(R.id.tv_folder_name).text = item.folderName
        holder.getTextView(R.id.tv_folder_count).text = "${item.photoList.size} 张"
        holder.getImageView(
            R.id.iv_folder_indicator).visibility = if (mSelector == position) View.VISIBLE else View.GONE
        PhotoPick.config().getImageLoader()?.displayImage(mActivity, item.firstImagePath,
            holder.findViewById(R.id.iv_folder_image), width, width)

        holder.itemView.setOnClickListener({
            mSelector = position
            notifyDataSetChanged()
            onItemClickListener?.onItemClick(item.folderName, item.photoList)
        })
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(folderName: String, images: ArrayList<PhotoBean>)
    }
}
