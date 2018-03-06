package com.photopick.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.photopick.utils.StatusBarUtil
import com.zhangbiao.photopick.R
import java.util.ArrayList

/**
 * Created by szhangbiao on 2018/2/28.
 */
abstract class FragmentActivity: AppCompatActivity() {
    private val TAG:String ="FragmentActivity"

    private var mFragmentContainer:FrameLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        StatusBarUtil.setStatusBarColor(this, R.color.color_black_6)
        mFragmentContainer=FrameLayout(this)
        mFragmentContainer?.id=getContextViewId()
        setContentView(mFragmentContainer)
    }
    abstract fun getContextViewId(): Int

    /**
     * replace fragment and add To Stack
     */
    open fun startFragment(fragment: BaseFragment) {
        Log.i(TAG, "startFragment")
        val transitionConfig = fragment.onFetchTransitionConfig()
        val tagName = fragment.javaClass.simpleName
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(transitionConfig.enter, transitionConfig.exit,transitionConfig.popenter, transitionConfig.popout)
            .replace(getContextViewId(), fragment, tagName)
            .addToBackStack(tagName)
            .commit()
    }

    /**
     * 退出当前的 Fragment。
     */
    open fun popBackStack() {
        Log.i(TAG,"popBackStack: getSupportFragmentManager().getBackStackEntryCount() = " + supportFragmentManager.backStackEntryCount)
        if (supportFragmentManager.backStackEntryCount <= 1) {
            val fragment = getCurrentFragment()
            if (fragment == null) {
                finish()
                return
            }
            val transitionConfig = fragment.onFetchTransitionConfig()
            val toExec = fragment.onLastFragmentFinish()
            if (toExec != null) {
                when (toExec) {
                    is BaseFragment -> {
                        val mFragment = toExec
                        startFragment(mFragment)
                    }
                    is Intent -> {
                        val intent = toExec
                        finish()
                        startActivity(intent)
                        overridePendingTransition(transitionConfig.popenter, transitionConfig.popout)
                    }
                    else -> throw Error("can not handle the result in onLastFragmentFinish")
                }
            } else {
                finish()
                overridePendingTransition(transitionConfig.popenter, transitionConfig.popout)
            }
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }
    /**
     * 获取当前的 Fragment。
     */
    private fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager.findFragmentById(getContextViewId()) as BaseFragment
    }
    /**
     * <pre>
     * 返回到clazz类型的Fragment，
     * 如 Home --> List --> Detail，
     * popBackStack(Home.class)之后，就是Home
     * 如果堆栈没有clazz或者就是当前的clazz（如上例的popBackStack(Detail.class)），就相当于popBackStack()</pre>
     *
     */
    open fun popBackStack(clazz: Class<out BaseFragment>) {
        supportFragmentManager.popBackStack(clazz.simpleName, 0)
    }
    /**
     * <pre>
     * 返回到非clazz类型的Fragment
     *
     * 如果上一个是目标clazz，则会继续pop，直到上一个不是clazz。
    </pre> *
     */
    open fun popBackStackInclusive(clazz: Class<out BaseFragment>) {
        supportFragmentManager.popBackStack(clazz.simpleName,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    open fun clearDisappearInfo(view: View?) {
        if (view != null) {
            try {
                val field = ViewGroup::class.java.getDeclaredField("mDisappearingChildren")
                field.isAccessible = true
                val o = field.get(mFragmentContainer)
                if (o != null && o is ArrayList<*>) {
                    if (o.contains(view)) {
                        Log.i(TAG, "ViewGroup.mDisappearingChildren contain the targetView")
                        o.remove(view)
                    }
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = getCurrentFragment()
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}
