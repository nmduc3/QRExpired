package com.example.myapplication.main.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R

/**
 * Created by nmduc3 on 12/2/20
 */
abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    abstract fun getLayoutBinding(): V

    protected var binding: V? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        hideSystemUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = getLayoutBinding()
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    protected fun replaceFragment(
        fragment: Fragment,
        isAddToBackStack: Boolean = true,
        isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        supportFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
            }
            replace(R.id.container, fragment, fragment.javaClass.name)
            if (isAddToBackStack) {
                addToBackStack(tagNameBackStack)
            }
            commit()
        }
    }

    protected fun addFragment(
        fragment: Fragment,
        isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        supportFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
            }
            add(R.id.container, fragment, fragment.javaClass.name)
            addToBackStack(tagNameBackStack)
            commit()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }
}

