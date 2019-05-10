//package com.jasoncavinder.inpen.demo.onboarding.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import com.jasoncavinder.inpen.demo.R
//import com.jasoncavinder.inpen.demo.data.LoginViewModel
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import com.jasoncavinder.insulinpendemoapp.utilities.DemoAction
//import com.jasoncavinder.insulinpendemoapp.utilities.DemoActionListDialogFragment
//import kotlinx.android.synthetic.main.fragment_add_pen.*
//import java.util.*
//
//class AddPenFragment : Fragment(), DemoActionListDialogFragment.Listener {
//
//    companion object {
//        fun newInstance() = CreateUserFragment()
//    }
//
//    private lateinit var _loginViewModel: LoginViewModel
//    private lateinit var _navController: NavController
//
//    private var _uuid = UUID.randomUUID().toString()
//    private lateinit var _user: LiveData<User>
//    private lateinit var _addPenResult: LiveData<Boolean>
//
//    /* BEGIN: Required for Demo Actions */
//    private var _demoActions = arrayListOf(
//        DemoAction("Simulate scanning barcode", this::_simulateBarcodeScan)
//    )
//
//    override fun onDemoActionClicked(position: Int) {
//        _demoActions[position].action()
//    }
//    /* END: Required for Demo Actions */
//
//    private fun _simulateBarcodeScan() {
//        camera_view.stopPlayback()
//        camera_view.setVideoPath("android.resource://" + requireActivity().packageName + "/" + R.raw.simulate_barcode_scan)
//        camera_view.setOnPreparedListener { it.isLooping = false }
//        camera_view.start()
//        camera_view.setOnCompletionListener {
//            _loginViewModel.addPen(Pen(penID = _uuid, userID = _user.value!!.userID))
//        }
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_add_pen, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        _navController = findNavController()
//
//        _loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)
//
////        _newUser = _loginViewModel.newUser
//
////        _addPenResult = _loginViewModel.addPenResult
//
//        button_skip.setOnClickListener { _navController.navigate(R.id.action_addPenFragment_to_addProviderFragment) }
//
//        _loginViewModel.addPenResult.observe(this, Observer {
//            //            val addPenResult = it ?: return@Observer
//            when (it) {
//                false -> return@Observer
//                else -> {
//                    text_scan_status.text =
//                        getString(R.string.found_pen).format(_loginViewModel.newUser.value?.userID?.substring(0, 12))
//                    button_add_pen.text = getString(R.string.continue_btn)
//                    button_add_pen.isEnabled = true
//                    button_add_pen.setOnClickListener { _navController.navigate(R.id.action_addPenFragment_to_addProviderFragment) }
//                }
//            }
//        })
//
///*
//        _loginViewModel.createUserResult.observe(this, Observer {
//            val createUserResult = it ?: return@Observer
//
//            group_form_create_user.visibility = View.VISIBLE
//            loading_bar.visibility = View.GONE
//            createUserResult.error?.let { showCreateUserFailed(createUserResult.error) }
//            createUserResult.success?.let {
//                Toast.makeText(context, "Hello ${it.firstName}. Let's add your insulin penPoints.", Toast.LENGTH_LONG).show()
//                _navController.navigate(R.id.action_createUserFragment_to_addPenFragment)
//            }
//        })
//
//*/
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        camera_view?.setVideoPath("android.resource://" + activity!!.packageName + "/" + R.raw.simulate_camera)
//        camera_view?.setOnPreparedListener { it.isLooping = true }
//        camera_view?.start()
//
//        /* BEGIN: Required for Demo Actions */
//        fab_demo_actions.setOnClickListener {
//            DemoActionListDialogFragment.newInstance(_demoActions)
//                .show(childFragmentManager, "demoActionsDialog")
//        }
//        /* END: Required for Demo Actions */
//    }
//}
//
//
////private val CAMERA_REQUEST_CODE: Int = 1001
//
//
///*  For camera, but decided to use simulation video for demo app
//    private lateinit var cameraManager: CameraManager
//    private var cameraFacing: Int = 0
//    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
//
//    private var cameraDevice: CameraDevice? = null
//    private var cameraID: String = ""
//    private var previewSize: Size = Size(0, 0)
//
//    val stateCallback = object : CameraDevice.StateCallback() {
//        override fun onOpened(cameraDevice: CameraDevice) {
//            this@AddPenFragment.cameraDevice = cameraDevice
//            createPreviewSession()
//        }
//
//        override fun onDisconnected(cameraDevice: CameraDevice) {
//            cameraDevice.close()
//            this@AddPenFragment.cameraDevice = null
//        }
//
//        override fun onError(cameraDevice: CameraDevice, error: Int) {
//            cameraDevice.close()
//            this@AddPenFragment.cameraDevice = null
//        }
//    }
//
//    fun setupCamera() {
//        try {
//            for (cameraID in cameraManager.getCameraIdList()) {
//                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID)
//                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
//                    val streamConfigurationMap = cameraCharacteristics.get(
//                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
//                    )
//                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
//                    this.cameraID = cameraID
//                }
//            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun openCamera() {
//        try {
//            if (ActivityCompat.checkSelfPermission(this.context!!, android.Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                cameraManager.openCamera(cameraID, stateCallback, backgroundHandler)
//            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace();
//        }
//    }
//
//    fun closeCamera() {
//        cameraCaptureSession?.let { it.close(); cameraCaptureSession = null }
//        cameraDevice?.let {it.close(); cameraDevice = null }
//    }
//
////fun openBackgroundThread() {
////    GlobalScope.launch {  }
////    backgroundThread = new HandlerThread("camera_background_thread");
////    backgroundThread.start();
////    backgroundHandler = new Handler(backgroundThread.getLooper());
////}
//    // OR?
////    val runnable = Runnable { doJob() }
////
////    fun doJob() {
////        EventManager.post(BoutiqueRefreshTimerEvent())
////        handler.postDelayed(runnable, delayMs)
////    }
//
//
////fun closeBackgroundThread() {}
//
//
//    private fun createPreviewSession() {
//        try {
//            val surfaceTexture = textureView.getSurfaceTexture()
//            surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
//            val previewSurface = Surface(surfaceTexture)
//            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//            captureRequestBuilder.addTarget(previewSurface)
//
//            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
//                object : CameraCaptureSession.StateCallback() {
//
//                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
//                        if (cameraDevice ==
//                            null
//                        ) {
//                            return
//                        }
//
//                        try {
//                            captureRequest = captureRequestBuilder.build()
//                            this@CameraActivity.cameraCaptureSession = cameraCaptureSession
//                            this@CameraActivity.cameraCaptureSession.setRepeatingRequest(
//                                captureRequest,
//                                null,
//                                backgroundHandler
//                            )
//                        } catch (e: CameraAccessException) {
//                            e.printStackTrace()
//                        }
//
//                    }
//
//                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
//
//                    }
//                }, backgroundHandler
//            )
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//
//    }
//*/
//
///*
//BottomSheetDialog(requireContext())
//    .apply {
//        setContentView(
//            layoutInflater.inflate(
//                R.layout.demo_actions_add_pen,
//                null
//            )
//        )
//    }
//    .show()
//val viewManager = LinearLayoutManager(context)
//val viewAdapter = DemoActionMenuAdapter(demoActionsList)
//val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.demo_actions_recycler)
//    .apply {
//        setHasFixedSize(true)
//        layoutManager = viewManager
//        adapter = viewAdapter
//    }
//*/
//
///*
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            REQUEST_WRITE_PERMISSION ->
//                if (grantResults.isNotEmpty() &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//
//                }
//        }
//    }
//*/
