package com.jasoncavinder.inpen.demo.login.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
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

    private val TAG by lazy { AddPenFragment::class.java.simpleName }

    var uuid = UUID.randomUUID().toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.jasoncavinder.inpen.demo.R.layout.fragment_add_pen, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "camera_view: $camera_view")
        Log.d(TAG, "packageName: $activity" + ".${activity?.packageName}")
        Log.d(TAG, "sim_bar_scn: ${R.raw.simulate_barcode_scan}")
        val simVidPath = "android.resource://" + activity!!.packageName + "/" + R.raw.simulate_barcode_scan
        Log.d(TAG, "simVidPath:  $simVidPath")
        val simVidUri = Uri.parse(simVidPath)
        Log.d(TAG, "uri(parsed): $simVidUri")
        camera_view.setVideoURI(simVidUri)
        simulate_scan.setOnClickListener {
            camera_view.setOnCompletionListener {
                button_add_pen.text = getString(R.string.added_pen).format(uuid)
                button_add_pen.isEnabled = true
            }
            camera_view.start()
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
