package com.example.myapplication.main

import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.main.base.BaseActivity
import com.example.myapplication.main.container.ContainerFragment

class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun getLayoutBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            replaceFragment(ContainerFragment(), isAddToBackStack = false, isEnableAnim = false)
        }
    }
}
