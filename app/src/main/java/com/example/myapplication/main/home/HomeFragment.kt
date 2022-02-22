package com.example.myapplication.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.main.base.BaseFragment

class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding? =
        FragmentHomeBinding::inflate

    override val viewModel by viewModels<HomeViewModel>()
}
