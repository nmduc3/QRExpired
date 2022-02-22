package com.example.myapplication.main.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentSplashBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by nmduc3 on 1/6/21
 */
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {

    override val viewModel by viewModels<SplashViewModel>()

    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding? =
        FragmentSplashBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null)
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                CoroutineScope(Dispatchers.Main).launch {
                    replaceFragment(HomeFragment(), isAddBackStack = false)
                }
            }
    }
}
