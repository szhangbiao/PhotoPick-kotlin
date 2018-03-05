package com.photopick.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import com.photopick.PhotoPick
import com.photopick.base.BaseRAdapter
import com.photopick.base.RViewHolder
import com.photopick.bean.PhotoBean
import com.photopick.config.PickOption
import com.photopick.utils.AnimationLoader
import com.zhangbiao.photopick.R
import java.util.ArrayList

/**
 * Created by szhangbiao on 2018/3/1.
 */
class PhotoAdapter(private val mActivity: Activity,private val config:PickOption) : BaseRAdapter<PhotoBean>(mActivity) {
    private val TYPE_CAMERA = 1
    private val TYPE_PICTURE = 2

    private var enableMultiSelected:Boolean
    private var enableCamera = false
    private var onPicktChangedListener: OnPickChangedListener? = null
    private val maxSelectNum: Int
    private val pickMediaList: MutableList<PhotoBean> = ArrayList()
    private val overrideWidth: Int
    private val overrideHeight: Int

    private val animation: Animation by lazy { AnimationLoader.loadAnimation(mContext, R.anim.phoenix_window_in) }
    private val zoomAnim: Boolean
    var isExceedMax: Boolean = false

    init {
        this.enableMultiSelected = config.enableMultiSelect
        this.enableCamera = config.enableCamera
        this.maxSelectNum = config.maxPickNumber
        this.overrideWidth = config.thumbnailWidth
        this.overrideHeight = config.thumbnailHeight
        this.zoomAnim = config.enableAnimation
    }

    fun setPickMediaList(medias: MutableList<PhotoBean>){
        pickMediaList.clear()
        pickMediaList.addAll(medias)
        if (onPicktChangedListener != null) {
            onPicktChangedListener!!.onChange(pickMediaList)
        }
    }

    fun getPickMediaList(): MutableList<PhotoBean> {
        return pickMediaList
    }

    override fun getItemViewType(position: Int): Int {
        return if (enableCamera && position == 0) {
            TYPE_CAMERA
        } else {
            TYPE_PICTURE
        }
    }

    override fun getItemCount(): Int {
        return if (enableCamera) mList.size + 1 else mList.size
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return if (viewType == TYPE_CAMERA) {
            R.layout.photo_pick_item_camera
        } else {
            R.layout.photo_pick_item_media
        }
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE_CAMERA ->{ holder.itemView.setOnClickListener({
                if(onPicktChangedListener!=null){
                    onPicktChangedListener!!.onTakePhoto()
                }
            })}
            else ->{
                bindData(holder,position,mList[if (enableCamera) position - 1 else position])
            }
        }
    }
    override fun bindData(holder: RViewHolder, position: Int, item: PhotoBean) {
        selectImage(holder, isSelected(item),false)
        PhotoPick.config().getImageLoader()?.displayImage(mActivity, item.photoPath,holder.findViewById(R.id.iv_folder_image), 50, 50)
        holder.itemView.setOnClickListener {
            changeCheckboxState(holder, item)
        }
    }

    /**
     * 改变图片选中状态
     * @param viewHolder viewHolder
     * @param image image
     */
    @SuppressLint("StringFormatMatches")
    private fun changeCheckboxState(viewHolder: RViewHolder, image: PhotoBean) {
        val isChecked = viewHolder.getTextView(R.id.tv_check).isSelected
        if (isChecked) {
            for (mediaEntity in pickMediaList) {
                if (mediaEntity.photoPath == image.photoPath) {
                    pickMediaList.remove(mediaEntity)
//                    subSelectPosition()
                    disZoom(viewHolder.getImageView(R.id.iv_picture))
                    break
                }
            }
        } else {
            if (isExceedMax) {
                notifyDataSetChanged()
                Toast.makeText(mContext,"最多可以选择${maxSelectNum}张",Toast.LENGTH_LONG).show()
                return
            }
            pickMediaList.add(image)
            zoom(viewHolder.getImageView(R.id.iv_picture))
        }

        //通知点击项发生了改变
        isExceedMax = pickMediaList.size >= maxSelectNum && maxSelectNum != 0
        if (isExceedMax || pickMediaList.size == maxSelectNum - 1) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(viewHolder.adapterPosition)
            selectImage(viewHolder, !isChecked, false)
        }

        if (onPicktChangedListener != null) {
            onPicktChangedListener!!.onChange(pickMediaList)
        }
    }

    private fun selectImage(viewHolder: RViewHolder, isChecked: Boolean, isAnim: Boolean) {
        viewHolder.getTextView(R.id.tv_check).isSelected = isChecked
        if (isChecked) {
            if (isAnim) {
                viewHolder.getTextView(R.id.tv_check).startAnimation(animation)
            }
            viewHolder.getImageView(R.id.iv_picture).setColorFilter(ContextCompat.getColor(mContext, R.color.color_black_4), PorterDuff.Mode.SRC_ATOP)
        } else {
            if (isExceedMax) {
                viewHolder.getImageView(R.id.iv_picture).setColorFilter(ContextCompat.getColor(mContext, R.color.color_transparent_white), PorterDuff.Mode.SRC_ATOP)
            } else {
                viewHolder.getImageView(R.id.iv_picture).setColorFilter(ContextCompat.getColor(mContext, R.color.color_black_5), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun isSelected(image: PhotoBean): Boolean {
        for (mediaEntity in pickMediaList) {
            if (TextUtils.isEmpty(mediaEntity.photoPath) || TextUtils.isEmpty(image.photoPath)) {
                return false
            }
            if (mediaEntity.photoPath == image.photoPath){
                return true
            }
        }
        return false
    }

    private fun zoom(iv_img: ImageView) {
        if (zoomAnim) {
            val set = AnimatorSet()
            set.playTogether(
                ObjectAnimator.ofFloat(iv_img, "scaleX", 1f, 1.12f),
                ObjectAnimator.ofFloat(iv_img, "scaleY", 1f, 1.12f)
            )
            set.duration = DURATION.toLong()
            set.start()
        }
    }

    private fun disZoom(iv_img: ImageView) {
        if (zoomAnim) {
            val set = AnimatorSet()
            set.playTogether(
                ObjectAnimator.ofFloat(iv_img, "scaleX", 1.12f, 1f),
                ObjectAnimator.ofFloat(iv_img, "scaleY", 1.12f, 1f)
            )
            set.duration = DURATION.toLong()
            set.start()
        }
    }

    companion object {
        private val DURATION = 450
    }

    interface OnPickChangedListener {
        fun onTakePhoto()
        fun onChange(selectImages: List<PhotoBean>)
    }

    fun setOnPickChangedListener(onPickChangedListener: OnPickChangedListener) {
        this.onPicktChangedListener = onPickChangedListener
    }
}