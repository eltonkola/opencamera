package com.aldroid.muzikashqipx.ui

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.muzikashqipx.ui.adapter.IpcameraAdapter
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.ui.media.FilesAdapter
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import io.reactivex.disposables.Disposable

class CamerasScreen : BaseScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_cams
    }

    val cameraListManager = MainApp.instance.cameraListManager

    lateinit var disposable : Disposable

    lateinit var recyclerView : RecyclerView

    override fun onEntered() {
        super.onEntered()
        bottomBar.selectedItemId = R.id.tab_cameras
        toolbar.title = "Cameras"


        val button_add = toolbar.findViewById<Button>(R.id.button_new)
        button_add.setOnClickListener {
            goTo(MainApp.PATH_CAMERA_ADD)
        }


        recyclerView = mRootView.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(mContext)

        disposable = cameraListManager.cameraLits.subscribe({



            recyclerView.adapter = IpcameraAdapter(it, object : IpcameraAdapter.OnClick{

                override fun onClick(element: IpCamera) {
                    goTo(MainApp.PATH_CAMERA_VIEW, element)
                }

                override fun onDelete(elemen: IpCamera) {
                    toast("onDelete" + elemen.ip)
                    deleteCamera(elemen)
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

        cameraListManager.deleteCamera(camera).subscribe({
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