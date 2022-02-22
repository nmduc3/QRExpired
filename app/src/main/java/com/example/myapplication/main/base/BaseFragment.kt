package com.example.myapplication.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.myapplication.R

/**
 * Created by nmduc3 on 11/24/20
 */
abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel> : Fragment() {

    abstract fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> V?

    abstract val viewModel: VM

    protected var binding: V? = null

    private var callBackWhenBackPress: OnBackPressedCallback = object : OnBackPressedCallback(
        false
        /** true means that the callback is enabled */
    ) {
        override fun handleOnBackPressed() {
            handleBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = getBinding().invoke(inflater, container, false)
        setWindowInsets()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // note that you could enable/disable the callback here as well by setting callback.isEnabled = true/false
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBackWhenBackPress)
    }

    override fun onResume() {
        super.onResume()
        handleAddCallBack(true)
    }

    override fun onPause() {
        super.onPause()
        handleAddCallBack(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected open fun handleBackPressed() {
        if (parentFragmentManager.backStackEntryCount > 0)
            parentFragmentManager.popBackStack()
        else {
            (parentFragment as? BaseFragment<*, *>)?.handleBackPressed()
        }
    }

    private fun handleAddCallBack(isEnable: Boolean = true) {
        callBackWhenBackPress.isEnabled = isEnable
    }

    protected fun addFragment(
        fragment: Fragment, isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        if (parentFragment != null) {
            addInParentFragment(fragment, isEnableAnim, tagNameBackStack)
        } else {
            addInChildFragment(fragment, isEnableAnim, tagNameBackStack)
        }
    }

    protected fun addInChildFragment(
        fragment: Fragment, isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        childFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            add(R.id.container, fragment, fragment.javaClass.name)
            addToBackStack(tagNameBackStack)
            commit()
        }
    }

    protected fun addInParentFragment(
        fragment: Fragment, isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        (parentFragment as? BaseFragment<*, *>)?.addInChildFragment(
            fragment,
            isEnableAnim,
            tagNameBackStack
        )
    }

    protected fun replaceFragment(
        fragment: Fragment,
        isEnableAnim: Boolean = true,
        isAddBackStack: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        if (parentFragment != null) {
            replaceInParentFragment(fragment, isEnableAnim, isAddBackStack, tagNameBackStack)
        } else {
            replaceInChildFragment(fragment, isEnableAnim, isAddBackStack, tagNameBackStack)
        }
    }

    protected fun replaceInChildFragment(
        fragment: Fragment, isEnableAnim: Boolean = true,
        isAddBackStack: Boolean = true, tagNameBackStack: String? = null
    ) {
        childFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            add(R.id.container, fragment, fragment.javaClass.name)
            if (isAddBackStack) {
                addToBackStack(tagNameBackStack)
            }
            commit()
        }
    }

    protected fun replaceInParentFragment(
        fragment: Fragment, isEnableAnim: Boolean = true,
        isAddBackStack: Boolean = true, tagNameBackStack: String? = null
    ) {
        (parentFragment as? BaseFragment<*, *>)?.replaceInChildFragment(
            fragment,
            isEnableAnim,
            isAddBackStack,
            tagNameBackStack
        )
    }

    private fun setWindowInsets() {
        binding?.root?.findViewById<ViewGroup>(R.id.content)?.apply {
            clipToPadding = false
            ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updatePadding(
                    top = inset.top,
                    bottom = inset.bottom
                )
                insets
            }
        }
    }
}
