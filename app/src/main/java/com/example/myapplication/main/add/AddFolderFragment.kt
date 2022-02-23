package com.example.myapplication.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentAddFolderBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.extension.visible

class AddFolderFragment: BaseFragment<FragmentAddFolderBinding, AddFolderViewModel>() {
    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddFolderBinding? =
        FragmentAddFolderBinding::inflate

    override val viewModel by viewModels<AddFolderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            tvAdd.disableMultipleClick {
                viewModel.handleSaveNewFolder(edtId.text.toString(), edtName.text.toString())
            }
        }
        viewModel.errorLiveData().observe(viewLifecycleOwner) {
            if (it) {
                binding?.tvErrorId?.visible()
            } else {
                handleBackPressed()
            }
        }
    }
}
