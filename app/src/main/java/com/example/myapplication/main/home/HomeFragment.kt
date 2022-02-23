package com.example.myapplication.main.home

import android.Manifest
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.common.Constants
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.extension.toPx
import com.example.myapplication.main.folder.FolderFragment
import com.example.myapplication.main.qrscan.QrScanFragment
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    EasyPermissions.PermissionCallbacks {
    companion object {
        const val RC_CAMERA = 999
    }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        handleOpenCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
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
            replaceFragment(FolderFragment.newInstance(it))
        }
        binding?.run {
            imgQRScan.disableMultipleClick {
                handleOpenCamera()
            }
        }
    }

    private fun handleOpenCamera() {
        context?.let {
            if (EasyPermissions.hasPermissions(it, Manifest.permission.CAMERA)) {
                replaceFragment(QrScanFragment(), tagNameBackStack = Constants.TagName.HOME)
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.camera_rationale),
                    RC_CAMERA,
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun initObserver() {
        viewModel.getALLFoldersLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
