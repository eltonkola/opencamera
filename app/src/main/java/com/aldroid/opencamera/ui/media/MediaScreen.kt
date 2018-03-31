package com.aldroid.muzikashqipx.ui

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.DropboxManager
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.ui.media.FilesAdapter
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata


class MediaScreen : BaseScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_media
    }

    lateinit var filesAdapter : FilesAdapter

    override fun onEntered() {
        super.onEntered()
        bottomBar.selectedItemId = R.id.tab_media
        toolbar.title = "Media"


        val recyclerView = mRootView.findViewById(R.id.recyclerView) as RecyclerView
        filesAdapter = FilesAdapter(PicassoClient.getPicasso(), object : FilesAdapter.Callback{
            override fun onFolderClicked(folder: FolderMetadata?) {
                toast("onFolderClicked " + folder)
            }

            override fun onFileClicked(file: FileMetadata?) {
                toast("onFileClicked " + file)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = filesAdapter

        loadData()
    }

    val dropboxManager = DropboxManager()

    fun loadData(){
        val dialog = ProgressDialog(mContext)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setCancelable(false)
        dialog.setMessage("Loading")
        dialog.show()

        //load media folder
        dropboxManager.listFolder("/media/").subscribe({
            dialog.dismiss()
            filesAdapter.setFiles(it.entries)
        }, {
            dialog.dismiss()
            toast("An error has occurred")
        })
    }

}