package com.aldroid.muzikashqipx.ui

import android.os.Environment
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.aldroid.cryptomarketcap.ui.BaseSecondaryScreen
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.v2.files.FileMetadata
import com.eltonkola.arkitekt.AppScreen
import java.io.File

class ShowImageScreen :  BaseSecondaryScreen<FileMetadata>() {

    override fun getView(): Int {
        return R.layout.screen_show_image
    }

    val cameraListManager = MainApp.instance.cameraListManager

    val sdcardPath = Environment.getExternalStorageDirectory().toString()

    lateinit var loading: ProgressBar
    lateinit var imageview: ImageView

    override fun onEntered() {
        super.onEntered()

        loading =  mRootView.findViewById(R.id.loading)
        imageview  =  mRootView.findViewById(R.id.show_image)

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


        val localFile = File("${appDirectory}/${mScreenParam.name}")
        if(localFile.exists()) {
            loading.visibility = View.GONE
            PicassoClient.getPicasso().load(localFile).into(imageview)


            toolbar.addAction(R.drawable.ic_share_white_24dp, "Share")
            toolbar.setActionItemClickListener { position, actionItem -> Utils.share(mContext, localFile)}

        }else {


            cameraListManager.cameraListManager.dropboxManager.downloadFile(true, mScreenParam).subscribe({
                loading.visibility = View.GONE

                it.let {
                    PicassoClient.getPicasso().load(it!!).into(imageview)

                    toolbar.addAction(R.drawable.ic_share_white_24dp, "Share")
                    toolbar.setActionItemClickListener { position, actionItem -> Utils.share(mContext, it)}

                }

            }, {
                toast("Error loading image ${mScreenParam.name}!")
                close()
            })
        }


    }


}