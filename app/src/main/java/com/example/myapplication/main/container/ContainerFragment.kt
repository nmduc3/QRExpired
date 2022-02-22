package com.example.myapplication.main.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentContainerBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.splash.SplashFragment

/**
 * Created by nmduc3 on 12/30/20
 */
class ContainerFragment : BaseFragment<FragmentContainerBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel = BaseViewModel()

    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentContainerBinding? =
        FragmentContainerBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null)
            replaceFragment(SplashFragment(), isEnableAnim = false, isAddBackStack = false)
    }
}
