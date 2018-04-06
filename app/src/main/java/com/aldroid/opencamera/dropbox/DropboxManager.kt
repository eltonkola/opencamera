package com.aldroid.opencamera.dropbox

import android.os.Environment
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.DbxException
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ListFolderResult
import com.dropbox.core.v2.files.WriteMode
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class DropboxManager(val dbxClientV2 : DbxClientV2 = DropboxClientFactory.getClient()) {

    val sdcardPath = Environment.getExternalStorageDirectory().toString()

    fun downloadFile(mediaFile: Boolean, metadata: FileMetadata) : Single<File> {
        return Single.create<File>({
            try {


                val appDirectory: File
                if (mediaFile) {
                    appDirectory = File("$sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}")
                } else {
                    appDirectory = File("$sdcardPath/${Utils.APP_FOLDER}")
                }

                if (!appDirectory.exists()) {
                    if (!appDirectory.mkdirs()) {
                        throw RuntimeException("Unable to create directory: $appDirectory")
                    } else if (!appDirectory.isDirectory()) {
                        throw  IllegalStateException("Download path is not a directory: $appDirectory")
                    }
                }

                val file = File(appDirectory, metadata.getName())
                FileOutputStream(file).use({
                    dbxClientV2.files().download(metadata.getPathLower(), metadata.getRev()).download(it)
                })

                it.onSuccess(file)

            } catch (e: DbxException) {
                e.printStackTrace()
                it.onError(e)
            } catch (e: IOException) {
                e.printStackTrace()
                it.onError(e)
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun listFolder(folderPath: String) : Single<ListFolderResult> {
        return Single.create<ListFolderResult>({
            try{
                it.onSuccess(dbxClientV2.files().listFolder(folderPath))
            }catch (error: Exception){
                it.onError(error)
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun uploadFile(localFile: String, remoteFolderPath: String) : Single<FileMetadata> {
        return Single.create<FileMetadata>({
            Utils.log("upload local file:"  + localFile)
            Utils.log("remoteFolderPath:"  + remoteFolderPath)
            val localFileElement = File(localFile)
            if (localFile != null) {
                // Note - this is not ensuring the name is a valid dropbox file name
                val remoteFileName = localFileElement!!.getName()
                try {
                    FileInputStream(localFile).use({ inputStream ->
                        val fileMetadata =  dbxClientV2.files().uploadBuilder(remoteFolderPath + "/" + remoteFileName)
                                .withMode(WriteMode.OVERWRITE)
                                .uploadAndFinish(inputStream)
                        it.onSuccess(fileMetadata)
                    })
                } catch (e: DbxException) {
                    e.printStackTrace()
                    it.onError(e)
                } catch (e: IOException) {
                    e.printStackTrace()
                    it.onError(e)
                }

            }else{
                it.onError(Exception("path donet exist"))
            }
        }) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}