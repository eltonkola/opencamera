package com.aldroid.opencamera.dropbox

import android.os.Environment
import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.v2.files.FileMetadata
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.subjects.BehaviorSubject
import java.io.File
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import java.io.FileWriter


class CameraListManager {

    val dropboxManager = DropboxManager()

    val cameraLits : BehaviorSubject<List<IpCamera>> = BehaviorSubject.createDefault(arrayListOf())

    val gson = Gson()

    init{

        //ask for it all
        dropboxManager.listFolder("").subscribe({
            val camera_list = it.entries
                    .filter {it is FileMetadata}
                    .filter { it.name.contentEquals("camera_list.json") }.firstOrNull()
            if(camera_list!=null){
                dropboxManager.downloadFile(false, camera_list as FileMetadata).subscribe({
                    //we got the list from dropbox
                    readCameraFile(it)
                },{
                    //failed, ose local or blank..
                })
            }
        },{
            //failed, ose local or blank..
        })

    }

    fun readCameraFile(file: File){
        val inputString = file.bufferedReader().use { it.readText() }
        val cameraListData: List<IpCamera>  = gson.fromJson(inputString, object : TypeToken<List<IpCamera>>() {}.type)
        cameraLits.onNext(cameraListData)
        Utils.log("loaded camera list:" + cameraListData.size)
    }

    fun addCamera(camera: IpCamera) : Completable{
        //new list
       val cameras = mutableListOf<IpCamera>()
        cameras.addAll(cameraLits.value)
        cameras.add(camera)
        return saveResults(cameras)
    }

    fun deleteCamera(camera: IpCamera): Completable{
        val cameras = mutableListOf<IpCamera>()
        cameras.addAll(cameraLits.value)
        cameras.remove(camera)
        return saveResults(cameras)
    }



    private fun saveResults(cameras : List<IpCamera>) : Completable {

        return Completable.create { onDone ->

            val sdcardPath = Environment.getExternalStorageDirectory().toString()
            val appDirectory = File("$sdcardPath/opencamera")
            if (!appDirectory.exists()) {
                if (!appDirectory.mkdirs()) {
                    onDone.onError(RuntimeException("Unable to create directory: $appDirectory"))
                } else if (!appDirectory.isDirectory()) {
                    onDone.onError(IllegalStateException("Download path is not a directory: $appDirectory"))
                }
            }
            val filePath = appDirectory.absolutePath + "/camera_list.json"
            FileWriter(filePath).use({ writer ->
                val gson = GsonBuilder().create()
                gson.toJson(cameras, writer)

                //also upload it on dropbox

                dropboxManager.uploadFile(filePath, "").subscribe({
                    Utils.log("camera list saved on drobox")

                    cameraLits.onNext(cameras)

                    onDone.onComplete()
                },{
                    it.printStackTrace()
                    Utils.log("error savingfile on dropbox:" + it.message)

                    onDone.onError(it)
                })
            })

        }
    }


}