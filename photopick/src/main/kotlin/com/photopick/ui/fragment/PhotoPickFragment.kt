package com.photopick.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.photopick.PhotoPick
import com.photopick.adapter.PhotoAdapter
import com.photopick.base.BaseFragment
import com.photopick.bean.FolderBean
import com.photopick.bean.PhotoBean
import com.photopick.config.PickOption
import com.photopick.utils.MediaLoader
import com.zhangbiao.photopick.R
import java.util.ArrayList

/**
 * Created by szhangbiao on 2018/3/5.
 */
class PhotoPickFragment:BaseFragment(){

    private lateinit var pickRlTitle: RelativeLayout
    private lateinit var pickTvBack:TextView
    private lateinit var pickTvTitle:TextView
    private lateinit var pickTvCancel:TextView
    private lateinit var rvContainer:RecyclerView
    private lateinit var rlBottom:RelativeLayout
    private lateinit var tvPreview:TextView
    private lateinit var llComplete:LinearLayout
    private lateinit var tvComplete:TextView
    private lateinit var tvNumber:TextView

    private lateinit var tvEmpty:TextView
    /**
     * 选项
     */
    private lateinit var option: PickOption
    /**
     * 图片加载
     */
    private lateinit var mediaLoader: MediaLoader


    private var animation: Animation? = null

    private lateinit var photoAdapter:PhotoAdapter

    private var allMediaList: MutableList<PhotoBean> = ArrayList()
    private var allFolderList: MutableList<FolderBean> = ArrayList()

    override fun onCreateView(): View {
        return LayoutInflater.from(getFragmentActivity()).inflate(R.layout.photo_pick_activity_picker,null)
    }

    override fun initWidgets(view: View) {
        option = if (arguments.getParcelable<PickOption>(PhotoPick.PICK_OPTION) == null) PickOption() else arguments.getParcelable(PhotoPick.PICK_OPTION)
        initView(view)
        setupView()
        setupData()
    }

    private fun initView(view: View) {
        pickRlTitle=findView(view,R.id.pickRlTitle)
        pickTvBack=findView(view,R.id.pickTvBack)
        pickTvTitle=findView(view,R.id.pickTvTitle)
        pickTvCancel=findView(view,R.id.pickTvCancel)
        rvContainer=findView(view,R.id.rv_photo_container)
        rvContainer=findView(view,R.id.rv_photo_container)
        rlBottom=findView(view,R.id.rl_bottom)
        tvPreview=findView(view,R.id.tv_photo_preview)
        llComplete=findView(view,R.id.ll_photo_complete)
        tvComplete=findView(view,R.id.tv_photo_complete)
        tvNumber=findView(view,R.id.tv_photo_number)
        tvEmpty=findView(view,R.id.tv_photo_empty)
    }

    private fun setupView() {

    }

    private fun setupData() {

    }


    /**
     * none number style
     */
    @SuppressLint("StringFormatMatches")
    private fun isNumberComplete() {
        tvComplete.text = getString(R.string.photo_pick_please_select)
        animation = AnimationUtils.loadAnimation(getFragmentActivity(), R.anim.phoenix_window_in)
    }
}