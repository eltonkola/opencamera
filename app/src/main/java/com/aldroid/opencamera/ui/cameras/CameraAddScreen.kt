package com.aldroid.muzikashqipx.ui

import android.app.ProgressDialog
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.eltonkola.arkitekt.AppScreen

class CameraAddScreen :  AppScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_add_camera
    }

    val cameraListManager = MainApp.instance.cameraListManager

    lateinit var ipAddress : EditText
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var button_create : Button

    override fun onEntered() {
        super.onEntered()

        val toolbar = mRootView.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Add camera"


        ipAddress =  mRootView.findViewById(R.id.ipAddress)
        username =  mRootView.findViewById(R.id.username)
        password =  mRootView.findViewById(R.id.password)

        button_create =  mRootView.findViewById(R.id.button_create)

        button_create.setOnClickListener{
            addCamera()
        }

    }

    fun addCamera() {
        if(ipAddress.text.length < 2){
            toast("Please eneter ip address")
            return
        }
        if(username.text.length < 2){
            toast("Please enter username")
            return
        }

        if(password.text.length < 2){
            toast("Please enter password")
            return
        }

        //TODO - try to connect and validate the camera

        //now create the camera

        val dialog = ProgressDialog(mContext)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setCancelable(false)
        dialog.setMessage("Saving camera")
        dialog.show()

        val camera = IpCamera(ipAddress.text.toString(), username.text.toString(), password.text.toString())
        cameraListManager.addCamera(camera).subscribe({
            dialog.dismiss()
            toast("Camera was saved!")
            close()
        },{
            dialog.dismiss()
            toast("Error saving camera: " + it.message)
        })

    }


}