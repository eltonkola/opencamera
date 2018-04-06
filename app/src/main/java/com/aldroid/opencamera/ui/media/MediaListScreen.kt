package com.aldroid.muzikashqipx.ui

import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.DropboxManager
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.ui.media.FilesAdapter
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata


class MediaListScreen : BaseTabScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_media
    }

    lateinit var filesAdapter : FilesAdapter

    override fun onEntered() {
        super.onEntered()
        bottomBar.selectedItemId = R.id.tab_media


        toolbar.addAction(R.drawable.ic_refresh_black_24dp, "Refresh")
        toolbar.setActionItemClickListener { position, actionItem -> loadData()}


        val recyclerView = mRootView.findViewById(R.id.recyclerView) as RecyclerView
        filesAdapter = FilesAdapter(PicassoClient.getPicasso(), object : FilesAdapter.Callback{
            override fun onFolderClicked(folder: FolderMetadata?) {
                toast("TODO - on folder click: " + folder)
            }

            override fun onFileClicked(file: FileMetadata?) {
               file.let {
                    if(file!!.name.endsWith("mp4")){
                        //open video
                        goTo(MainApp.PATH_MEDIA_SHOW_VIDEO, file!!)
                    }else{
                        //open image
                        goTo(MainApp.PATH_MEDIA_SHOW_IMAGE, file!!)
                    }

                }

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
        dropboxManager.listFolder(Utils.MEDIA_FOLDER).subscribe({
            dialog.dismiss()
            filesAdapter.setFiles(it.entries)
        }, {
            dialog.dismiss()
            toast("An error has occurred")
        })
    }

}