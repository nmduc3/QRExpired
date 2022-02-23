package com.example.myapplication.main.qrscan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentQrScanBinding
import com.example.myapplication.main.base.BaseFragment
import com.example.myapplication.main.extension.disableMultipleClick
import com.example.myapplication.main.qrscan.result.ResultFragment
import com.example.myapplication.main.qrscan.scan.QRScanner

class QrScanFragment: BaseFragment<FragmentQrScanBinding, QrScanViewModel>() {
    override fun getBinding(): (LayoutInflater, ViewGroup?, Boolean) -> FragmentQrScanBinding? =
        FragmentQrScanBinding::inflate

    override val viewModel by viewModels<QrScanViewModel>()

    private var qrScanner: QRScanner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isQrHasDetected = false
        initViews()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        qrScanner?.bindCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        qrScanner?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        qrScanner?.stop()
        qrScanner?.release()
    }

    private fun initViews() {
        binding?.run {
            qrScanner = QRScanner(root.context, this@QrScanFragment, ::onBarcodeSuccessReceived)
            binding?.previewView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    setupCamera()
                    binding?.previewView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    private fun setupCamera() {
        binding?.run {
            qrScanner?.setupCamera(
                preview = previewView,
                overlay = scannerOverlay,
                isFullScan = false
            )
        }
    }

    private fun initListeners() {
        binding?.imgBack?.disableMultipleClick {
            handleBackPressed()
        }
    }

    private fun onBarcodeSuccessReceived(barcodeString: String) {
        if (!viewModel.isQrHasDetected) {
            if (barcodeString.isNotEmpty()) {
                viewModel.isQrHasDetected = true
                //check barcode is Kris+ QR code
                val foodInfo = viewModel.handleQrData(barcodeString)
                if (foodInfo == null) {
                    replaceFragment(ResultFragment.newInstance(barcodeString))
                } else {
                    replaceFragment(ResultFragment.newInstance(foodInfo))
                }
            }
        }
    }
}
