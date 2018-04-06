package com.aldroid.muzikashqipx.ui

import android.net.Uri
import android.os.Environment
import android.support.v7.widget.Toolbar
import android.view.SurfaceView
import android.view.View
import android.widget.ProgressBar
import com.aldroid.cryptomarketcap.ui.BaseSecondaryScreen
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.v2.files.FileMetadata
import com.eltonkola.arkitekt.AppScreen
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import java.io.File

class ShowVideoScreen :  BaseSecondaryScreen<FileMetadata>() {

    override fun getView(): Int {
        return R.layout.screen_show_video
    }

    val cameraListManager = MainApp.instance.cameraListManager

    lateinit var loading: ProgressBar
    lateinit var surfaceView: SurfaceView

    lateinit var vlcVideoLibrary: VlcVideoLibrary

    val sdcardPath = Environment.getExternalStorageDirectory().toString()

    var localFileUrl :String ? = null

    override fun onEntered() {
        super.onEntered()

        val appDirectory = File("$sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}")
        if (!appDirectory.exists()) {
            if (!appDirectory.mkdirs()) {
                toast("Cant create app folder!")
                close()
            } else if (!appDirectory.isDirectory()) {
                toast("Not a valid app folder!")
                close()
            }
        }


        loading =  mRootView.findViewById(R.id.loading)
        surfaceView  =  mRootView.findViewById(R.id.surfaceView)
        vlcVideoLibrary = VlcVideoLibrary(mContext, object: VlcListener {
            override fun onComplete() {
                toast("Video completed!")
                localFileUrl?.let{
                    vlcVideoLibrary.play(localFileUrl)
                }
            }

            override fun onError() {
                toast("Error playing video..")
                vlcVideoLibrary.stop()
                close()
            }

        } , surfaceView)


        val localFile = File("${appDirectory}/${mScreenParam.name}")
        if(localFile.exists()) {

            toast("Play local Filename: ${mScreenParam.name} ")

            loading.visibility = View.GONE
            vlcVideoLibrary.play((Uri.fromFile(localFile).toString()))

            toolbar.addAction(R.drawable.ic_share_white_24dp, "Share")
            toolbar.setActionItemClickListener { position, actionItem -> Utils.share(mContext, localFile)}

        }else {

            cameraListManager.cameraListManager.dropboxManager.downloadFile(true, mScreenParam).subscribe({
                loading.visibility = View.GONE

                it.let {
                    localFileUrl = it.absolutePath
                    vlcVideoLibrary.play(localFileUrl)

                    toolbar.addAction(R.drawable.ic_share_white_24dp, "Share")
                    toolbar.setActionItemClickListener { position, actionItem -> Utils.share(mContext, localFile)}
                }

            }, {
                toast("Error loading video ${mScreenParam.name}!")
                close()
            })

        }

    }


}