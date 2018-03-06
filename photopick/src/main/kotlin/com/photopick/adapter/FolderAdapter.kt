package com.photopick.adapter

import android.app.Activity
import android.view.View
import com.photopick.PhotoPick
import com.photopick.base.BaseRAdapter
import com.photopick.base.RViewHolder
import com.photopick.bean.FolderBean
import com.zhangbiao.photopick.R

/**
 * Created by szhangbiao on 2018/3/1.
 */
class FolderAdapter(private val mActivity: Activity,
    mList: MutableList<FolderBean>) : BaseRAdapter<FolderBean>(mActivity, mList) {

    private var mSelector: Int = 0

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.photo_pick_item_folder
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.getTextView(R.id.tv_folder_name).text = "所有图片"
                holder.getTextView(R.id.tv_folder_count).text = "${getTotalImageSize()} 张"
                holder.getImageView(
                    R.id.iv_folder_indicator).visibility = if (mSelector == 0) View.VISIBLE else View.GONE
                PhotoPick.config().getImageLoader()?.displayImage(mActivity, mList[0].folderPath,
                    holder.findViewById(R.id.iv_folder_image), 50, 50)
            }
            else -> bindData(holder, position, mList[position - 1])
        }
        holder.itemView.setOnClickListener({
            mSelector = position
            onItemClickListener?.onItemClick(holder.itemView, position)
        })
    }

    private fun getTotalImageSize(): Int {
        var count = 0
        mList.forEach { count += it.photoList.size }
        return count
    }

    override fun bindData(holder: RViewHolder, position: Int, item: FolderBean) {
        holder.getTextView(R.id.tv_folder_name).text = item.folderName
        holder.getTextView(R.id.tv_folder_count).text = "${item.photoList.size} 张"
        holder.getImageView(
            R.id.iv_folder_indicator).visibility = if (mSelector == position) View.VISIBLE else View.GONE
        PhotoPick.config().getImageLoader()?.displayImage(mActivity, item.firstImagePath,
            holder.findViewById(R.id.iv_folder_image), 50, 50)
    }
}
