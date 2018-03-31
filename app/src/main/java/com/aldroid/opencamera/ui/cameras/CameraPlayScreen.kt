package com.aldroid.muzikashqipx.ui

import android.support.v7.widget.Toolbar
import android.view.SurfaceView
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.opencamera.R
import com.aldroid.opencamera.util.Utils
import com.eltonkola.arkitekt.AppScreen
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import com.rvirin.onvif.onvifcamera.*

class CameraPlayScreen :  AppScreen<IpCamera>() {

    override fun getView(): Int {
        return R.layout.screen_camera_play
    }

    lateinit var vlcVideoLibrary: VlcVideoLibrary

    lateinit var surfaceView : SurfaceView
    lateinit var toolbar : Toolbar

    override fun onEntered() {
        super.onEntered()

        toolbar= mRootView.findViewById(R.id.toolbar)
        toolbar.title = "Play: " + mScreenParam.ip


        surfaceView =  mRootView.findViewById(R.id.surfaceView)
        vlcVideoLibrary = VlcVideoLibrary(mContext, object: VlcListener{
            override fun onComplete() {
                toast("onComplete")
                //vlcVideoLibrary.stop()
            }

            override fun onError() {
                toast("onError")

                vlcVideoLibrary.stop()
            }

        } , surfaceView)


        Utils.log("ip: '${mScreenParam.ip}' ")
        Utils.log("username: '${mScreenParam.username}' ")
        Utils.log("password: '${mScreenParam.password}' ")

        currentDevice = OnvifDevice(mScreenParam.ip, mScreenParam.username, mScreenParam.password)
        currentDevice.listener = object: OnvifListener {
            override fun requestPerformed(response: OnvifResponse) {

                if (!response.success) {
                    toast("⛔️ Request failed: ${response.request.type}")
                    Utils.log("error: " + response.error)
                    Utils.log("parsingUIMessage: " + response.parsingUIMessage)
                    Utils.log("result: " + response.result)

                    close()

                }else if (response.request.type == OnvifRequest.Type.GetServices) {

                    Utils.log("result: " + response.result)
                    Utils.log("parsingUIMessage: " + response.parsingUIMessage)

                    currentDevice.getDeviceInformation()
                } else if (response.request.type == OnvifRequest.Type.GetDeviceInformation) {
                    toast("Device information retrieved 👍")

                    Utils.log("result: " + response.result)
                    Utils.log("parsingUIMessage: " + response.parsingUIMessage)

                    currentDevice.getProfiles()
                }
                // if GetProfiles have been completed, we request the Stream URI
                else if (response.request.type == OnvifRequest.Type.GetProfiles) {
                    val profilesCount = currentDevice.mediaProfiles.count()
                    toast("$profilesCount profiles retrieved 😎")

                    Utils.log("result: " + response.result)
                    Utils.log("parsingUIMessage: " + response.parsingUIMessage)
                    currentDevice.getStreamURI()

                }
                // if GetStreamURI have been completed, we're ready to play the video
                else if (response.request.type == OnvifRequest.Type.GetStreamURI) {
                    toast("Stream URI retrieved,\nready for the movie 🍿")

                    Utils.log("result: " + response.result)
                    Utils.log("parsingUIMessage: " + response.parsingUIMessage)

                    currentDevice.rtspURI?.let { uri ->

                        Utils.log(">>>>>>>>>> streaming url:" + uri)

                        vlcVideoLibrary.play(uri)

                    }?: run {
                        toast("RTSP URI haven't been retrieved")
                    }
                }


            }

        }
        currentDevice.getServices()

    }

    override fun onExit() {
        super.onExit()
        if(vlcVideoLibrary.isPlaying){
            vlcVideoLibrary.stop()
        }
    }
}