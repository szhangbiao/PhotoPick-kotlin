package com.photopick.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import com.photopick.config.TransitionConfig
import com.photopick.widget.LoadingDialog
import com.zhangbiao.photopick.R

/**
 * Created by szhangbiao on 2018/2/28.
 */
abstract class BaseFragment : Fragment() {
    private val TAG: String = "BaseFragment"
    private val SLIDE_TRANSITION_CONFIG: TransitionConfig = TransitionConfig(
        R.anim.slide_in_right, R.anim.slide_out_left,
        R.anim.slide_in_left, R.anim.slide_out_right)

    private val SCALE_TRANSITION_CONFIG: TransitionConfig = TransitionConfig(
        R.anim.scale_enter, R.anim.slide_still,
        R.anim.slide_still, R.anim.scale_exit)
    var mBaseView: View? = null

    protected val loadingDialog: LoadingDialog by lazy { LoadingDialog(activity) }

    @RequiresApi(VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = onCreateView()
        initWidgets(view)
        view.fitsSystemWindows = true
        mBaseView = view
        requestApplyInsets(getFragmentActivity().window)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        (mBaseView?.parent as ViewGroup).removeView(mBaseView)
        getFragmentActivity().clearDisappearInfo(mBaseView)
        return mBaseView
    }

    /**
     * onDetach
     */
    override fun onDetach() {
        super.onDetach()
        mBaseView=null
    }

    @SuppressLint("NewApi")
    fun requestApplyInsets(window: Window) {
        if (Build.VERSION.SDK_INT in 19..20) {
            window.decorView.requestFitSystemWindows()
        } else if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.requestApplyInsets()
        }
    }

    /**
     * getActivity()
     */
    open fun getFragmentActivity(): FragmentActivity {
        return activity as FragmentActivity
    }

    /**
     * fragment is Attached to FragmentActivity
     */
    open fun isAttachedToActivity(): Boolean {
        return !isRemoving && mBaseView != null
    }

    fun startFragment(fragment:BaseFragment){
        val fragmentActivity:FragmentActivity?=getFragmentActivity()
        if(isAttachedToActivity()){
            fragmentActivity?.startFragment(fragment)
        }
    }

    fun popBackStack(){
        getFragmentActivity().popBackStack()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (!enter && parentFragment != null && parentFragment.isRemoving) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            val doNothingAnim = AlphaAnimation(1f, 1f)
            doNothingAnim.duration = R.integer.photo_pick_anim_duration.toLong()
            return doNothingAnim
        }

        // bugfix: 使用scale enter时看不到效果， 因为两个fragment的动画在同一个层级，被退出动画遮挡了
        // http://stackoverflow.com/questions/13005961/fragmenttransaction-animation-to-slide-in-over-top#33816251
        if (nextAnim != R.anim.scale_enter || !enter) {
            return super.onCreateAnimation(transit, enter, nextAnim)
        }
        try {
            val nextAnimation = AnimationUtils.loadAnimation(context, nextAnim)
            nextAnimation.setAnimationListener(object : Animation.AnimationListener {
                var mOldTranslationZ: Float = 0.0f
                override fun onAnimationStart(animation: Animation) {
                    if (view != null) {
                        mOldTranslationZ = ViewCompat.getTranslationZ(view)
                        ViewCompat.setTranslationZ(view, 100f)
                    }
                }
                override fun onAnimationEnd(animation: Animation) {
                    view?.postDelayed({
                        //延迟回复z-index,如果退出动画更长，这里可能会失效
                        ViewCompat.setTranslationZ(view, mOldTranslationZ)
                    }, 100)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            return nextAnimation
        } catch (ignored: Exception) {

        }
        return null
    }

    /**
     * 如果是最后一个Fragment，finish后执行的方法
     */
    fun onLastFragmentFinish(): Any? {
        return null
    }
    /**
     * 转场动画控制
     */
    fun onFetchTransitionConfig(): TransitionConfig {
        return SLIDE_TRANSITION_CONFIG
    }

    /**
     * initWidgets
     */
    abstract fun initWidgets(view: View)
    /**
     * onCreateView
     */
    abstract fun onCreateView(): View


    //BaseFragment
    protected fun <T : View> findView(layoutView: View, @IdRes resId: Int): T {
        return layoutView.findViewById<View>(resId) as T
    }
    /**
     * 显示键盘
     */
    protected fun showKeyBoard() {
        val imm = activity.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethod.SHOW_FORCED)
    }

    /**
     * 隐藏键盘
     */
    protected fun hideKeyBoard(): Boolean {
        //		return mBaseActivityImpl.hideKeyBoard();
        val imm = activity.applicationContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(activity.findViewById<View>(android.R.id.content)
            .windowToken, 0)
    }

    /**
     * show loading loadingDialog
     */
    protected fun showLoadingDialog() {
        if (!isDetached) {
            dismissLoadingDialog()
            loadingDialog.show()
        }
    }

    /**
     * dismiss loading loadingDialog
     */
    protected fun dismissLoadingDialog() {
        try {
            if (loadingDialog.isShowing) {
                loadingDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}