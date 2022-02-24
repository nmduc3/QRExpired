package com.example.myapplication.main.qrscan.result

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentResultBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.common.Constants
import com.example.myapplication.main.common.EventObserver
import com.example.myapplication.main.data.database.model.FoodInfo
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.extension.gone
import com.example.myapplication.main.extension.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultFragment: BaseFragment<FragmentResultBinding, ResultViewModel>() {
    companion object {
        private const val KEY_DATA = "key_data"
        private const val KEY_ERROR = "key_error"
        fun newInstance(food: FoodInfo) = ResultFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, food)
            }
        }

        fun newInstance(errorString: String) = ResultFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ERROR, errorString)
            }
        }
    }

    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentResultBinding? =
        FragmentResultBinding::inflate

    override val viewModel by viewModels<ResultViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.errorString = arguments?.getString(KEY_ERROR)
            viewModel.foodInfo = arguments?.getParcelable(KEY_DATA)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        binding?.run {
            if (viewModel.foodInfo == null) {
                tvErrorLabel.visible()
                tvErrorString.visible()
                edtName.gone()
                tvSave.gone()
                layoutFoodPreview.root.gone()
                tvErrorString.text = viewModel.errorString
            } else {
                edtName.visible()
                tvSave.visible()
                tvErrorLabel.gone()
                tvErrorString.gone()
                layoutFoodPreview.root.visible()
                layoutFoodPreview.let {
                    it.tvId.text = viewModel.foodInfo?.foodId
                    it.tvDateStart.text = viewModel.foodInfo?.dateStart
                    it.tvDateExpired.text = viewModel.foodInfo?.getDateExpiredInString()
                    viewModel.getFolderName()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding?.run {
            imgBack.disableMultipleClick {
                parentFragmentManager.popBackStack(Constants.TagName.HOME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            tvSave.disableMultipleClick {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.saveFoodInfo(edtName.text.toString())
                    launch(Dispatchers.Main) {
                        parentFragmentManager.popBackStack(Constants.TagName.HOME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                }
            }
            edtName.doAfterTextChanged {
                layoutFoodPreview.tvName.text = it
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        viewModel.run {
            folderNameLD().observe(viewLifecycleOwner) {
                binding?.layoutFoodPreview?.tvId?.text = "${it.name} ID : ${viewModel.foodInfo?.foodId}"
            }
            expiredFoodLD().observe(viewLifecycleOwner, EventObserver { date ->
                context?.let {
                    AlertDialog.Builder(it)
                        .setMessage(getString(R.string.alarm_fail, date))
                        .show()
                }
            })
        }
    }

    override fun handleBackPressed() {
        parentFragmentManager.popBackStack(Constants.TagName.HOME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
