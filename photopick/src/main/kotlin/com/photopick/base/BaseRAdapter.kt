package com.photopick.base

import android.content.Context
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.photopick.`interface`.OnItemClickListener

/**
 * Created by szhangbiao on 2018/3/1.
 */
abstract class BaseRAdapter<T>(val mContext: Context,
    var mList: MutableList<T> = mutableListOf()) : Adapter<RViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RViewHolder {
        return RViewHolder(mInflater.inflate(getItemLayoutId(viewType), parent, false))
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        holder.itemView.setOnClickListener({
            onItemClickListener?.onItemClick(holder.itemView, holder.layoutPosition)
        })
        bindData(holder, position, mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    /**
     * 获得单个 T
     */
    fun getItem(position: Int): T {
        return mList[position]
    }

    /**
     * 特定position添加数据
     */
    fun add(pos: Int, item: T) {
        mList.add(pos, item)
        notifyItemInserted(pos)
    }

    /**
     * 刷新数据
     */
    fun updateDataList(newData: List<T>) {
        mList.clear()
        mList.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     */
    fun addDataList(newData: List<T>) {
        mList.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * 删除特定position的数据
     */
    fun delete(pos: Int) {
        mList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    /**
     * 綁定数据
     */
    abstract fun bindData(holder: RViewHolder, position: Int, item: T)

    /**
     * 返回布局id
     */
    abstract fun getItemLayoutId(viewType: Int): Int
}