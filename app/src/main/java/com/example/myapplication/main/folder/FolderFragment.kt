package com.example.myapplication.main.folder

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFolderBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.extension.toPx

class FolderFragment : BaseFragment<FragmentFolderBinding, FolderViewModel>() {
    companion object {
        private const val KEY_DATA = "key_data"

        fun newInstance(folder: Folder): FolderFragment = FolderFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, folder)
            }
        }
    }

    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentFolderBinding? =
        FragmentFolderBinding::inflate

    override val viewModel by viewModels<FolderViewModel>()

    private val adapter = FoodAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.folder = arguments?.getParcelable(KEY_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        binding?.run {
            tvTitle.text = viewModel.folder?.name
            rvFood.let {
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
        binding?.run {
            imgBack.disableMultipleClick {
                handleBackPressed()
            }
        }
    }

    private fun initObservers() {
        viewModel.getAllFoodLD().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}