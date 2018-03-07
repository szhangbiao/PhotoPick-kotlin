package com.photopick.sample

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
        return R.layout.item_sample_adapter
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ADD -> {
                holder.getImageView(R.id.ivPicture).setImageResource(R.drawable.ic_add_media)
                holder.getImageView(R.id.ivPicture).setOnClickListener({
                    mOnAddMediaListener?.onaddMedia()
                })
                holder.getView(R.id.llDelete).visibility = View.INVISIBLE
            }
            else -> {
                bindData(holder, position, mList[position])
            }
        }
    }

    override fun bindData(holder: RViewHolder, position: Int, item: PhotoBean) {
        holder.getView(R.id.llDelete).visibility = View.VISIBLE
        holder.getView(R.id.llDelete).setOnClickListener({
            val index = holder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                mList.removeAt(index)
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, mList.size)
            }
        })
        val path = item.photoPath
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.color.color_f6)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(holder.itemView.context)
            .load(path)
            .apply(options)
            .into(holder.getImageView(R.id.ivPicture))
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

    fun setOnAddMediaListener(mOnAddMediaListener: OnAddMediaListener) {
        this.mOnAddMediaListener = mOnAddMediaListener
    }
    interface OnAddMediaListener {
        fun onaddMedia()
    }
}