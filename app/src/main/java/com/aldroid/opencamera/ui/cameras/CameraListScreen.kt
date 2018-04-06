package com.aldroid.muzikashqipx.ui

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.coinhivesdk.model.IpCameraData
import com.aldroid.muzikashqipx.ui.adapter.IpCameraDataAdapter
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import io.reactivex.disposables.Disposable

class CameraListScreen : BaseTabScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_cams
    }

    val richCameraListManager = MainApp.instance.cameraListManager

    lateinit var disposable : Disposable

    lateinit var recyclerView : RecyclerView

    override fun onEntered() {
        super.onEntered()
        bottomBar.selectedItemId = R.id.tab_cameras
//        toolbar.title = "Cameras"


        toolbar.addAction(R.drawable.ic_add_circle_black_24dp, "Add")
        toolbar.setActionItemClickListener { position, actionItem -> goTo(MainApp.PATH_CAMERA_ADD)}

//        val button_add = toolbar.findViewById<Button>(R.id.button_new)
//        button_add.setOnClickListener {
//
//        }


        recyclerView = mRootView.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(mContext)

        disposable = richCameraListManager.cameraLits.subscribe({

            recyclerView.adapter = IpCameraDataAdapter(it, object : IpCameraDataAdapter.OnClick{

                override fun onClick(element: IpCameraData) {

                    if(element.online==null){
                        toast("Wait for camera status update..")
                    } else if(element.online == true){
                        goTo(MainApp.PATH_CAMERA_VIEW, element)
                    }else {
                        toast("Camera is offline!")
                    }
                }

                override fun onDelete(elemen: IpCameraData) {
                    toast("onDelete" + elemen.ipCamera.ip)
                    deleteCamera(elemen.ipCamera)
                }

            })

        }, {
            toast("error loading cameras: " + it.message)
            it.printStackTrace()
        })

    }

    fun deleteCamera(camera: IpCamera) {

        val dialog = ProgressDialog(mContext)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setCancelable(false)
        dialog.setMessage("Removing camera")
        dialog.show()

        richCameraListManager.cameraListManager.deleteCamera(camera).subscribe({
            dialog.dismiss()
            toast("Camera was removed!")
        },{
            dialog.dismiss()
            toast("Error removing camera: " + it.message)
        })

    }

    override fun onExit() {
        super.onExit()

        if(!disposable.isDisposed){
            disposable.dispose()
        }

    }
}