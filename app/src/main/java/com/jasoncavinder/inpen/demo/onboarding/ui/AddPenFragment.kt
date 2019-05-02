package com.jasoncavinder.inpen.demo.onboarding.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jasoncavinder.inpen.demo.R
import kotlinx.android.synthetic.main.fragment_add_pen.*
import java.util.*


//private val CAMERA_REQUEST_CODE: Int = 1001


class AddPenFragment : Fragment() {

/*  Camera, but decided to use simulation video for demo app
    private lateinit var cameraManager: CameraManager
    private var cameraFacing: Int = 0
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener

    private var cameraDevice: CameraDevice? = null
    private var cameraID: String = ""
    private var previewSize: Size = Size(0, 0)

    val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            this@AddPenFragment.cameraDevice = cameraDevice
            createPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
            this@AddPenFragment.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraDevice.close()
            this@AddPenFragment.cameraDevice = null
        }
    }

    fun setupCamera() {
        try {
            for (cameraID in cameraManager.getCameraIdList()) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )
                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
                    this.cameraID = cameraID
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.context!!, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                cameraManager.openCamera(cameraID, stateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }

    fun closeCamera() {
        cameraCaptureSession?.let { it.close(); cameraCaptureSession = null }
        cameraDevice?.let {it.close(); cameraDevice = null }
    }

//fun openBackgroundThread() {
//    GlobalScope.launch {  }
//    backgroundThread = new HandlerThread("camera_background_thread");
//    backgroundThread.start();
//    backgroundHandler = new Handler(backgroundThread.getLooper());
//}
    // OR?
//    val runnable = Runnable { doJob() }
//
//    fun doJob() {
//        EventManager.post(BoutiqueRefreshTimerEvent())
//        handler.postDelayed(runnable, delayMs)
//    }


//fun closeBackgroundThread() {}


    private fun createPreviewSession() {
        try {
            val surfaceTexture = textureView.getSurfaceTexture()
            surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
            val previewSurface = Surface(surfaceTexture)
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(previewSurface)

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (cameraDevice ==
                            null
                        ) {
                            return
                        }

                        try {
                            captureRequest = captureRequestBuilder.build()
                            this@CameraActivity.cameraCaptureSession = cameraCaptureSession
                            this@CameraActivity.cameraCaptureSession.setRepeatingRequest(
                                captureRequest,
                                null,
                                backgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {

                    }
                }, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }
*/

//    private val TAG by lazy { AddPenFragment::class.java.simpleName }

    private var uuid = UUID.randomUUID().toString()

    fun simulateBarcodeScan() {
        camera_view.stopPlayback()
        camera_view.setVideoPath("android.resource://" + activity!!.packageName + "/" + R.raw.simulate_barcode_scan)
        camera_view.setOnPreparedListener { it.isLooping = false }
        camera_view.start()
        camera_view.setOnCompletionListener {
            text_scan_status.text = getString(R.string.found_pen).format(uuid.substring(0, 12))
            button_add_pen.text = getString(R.string.continue_btn)
            button_add_pen.isEnabled = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_pen, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onResume() {
        super.onResume()

        camera_view?.setVideoPath("android.resource://" + activity!!.packageName + "/" + R.raw.simulate_camera)
        camera_view?.setOnPreparedListener { it.isLooping = true }
        camera_view?.start()

        fab_demo_actions.setOnClickListener {

            /*
            BottomSheetDialog(requireContext())
                .apply {
                    setContentView(
                        layoutInflater.inflate(
                            R.layout.demo_actions_add_pen,
                            null
                        )
                    )
                }
                .show()
            val viewManager = LinearLayoutManager(context)
            val viewAdapter = DemoActionMenuAdapter(demoActionsList)
            val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.demo_actions_recycler)
                .apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
*/
        }
    }


/*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_WRITE_PERMISSION ->
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                }
        }
    }
*/
}
