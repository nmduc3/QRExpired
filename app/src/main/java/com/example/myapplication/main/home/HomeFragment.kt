package com.example.myapplication.main.home

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.extension.toPx

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding? =
        FragmentHomeBinding::inflate

    override val viewModel by viewModels<HomeViewModel>()

    private val adapter = FolderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObserver()
    }

    private fun initViews() {
        binding?.run {
            rvFolders.let {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = adapter
                it.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
                    ) {
                        outRect.top = 5.toPx()
                        outRect.bottom = 5.toPx()
                    }
                })
            }
        }
    }

    private fun initListeners() {
        adapter.onItemCLick = {
            Log.d("okMAPPP", "item click: ${it.name}")
        }
        binding?.run {
            imgQRScan.disableMultipleClick {
                Log.d("okMAPPP", "QRScan")
            }
        }
    }

    private fun initObserver() {
        viewModel.getALLFoldersLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
