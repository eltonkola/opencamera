package com.aldroid.muzikashqipx.ui

import android.os.Environment
import android.support.v7.widget.Toolbar
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.aldroid.coinhivesdk.model.IpCameraData
import com.aldroid.cryptomarketcap.ui.BaseSecondaryScreen
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.DropboxManager
import com.aldroid.opencamera.util.Utils
import com.eltonkola.arkitekt.AppScreen
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import io.reactivex.functions.Consumer
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import nl.bravobit.ffmpeg.FFtask
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraPlayScreen : BaseSecondaryScreen<IpCameraData>() {

    override fun getView(): Int {
        return R.layout.screen_camera_play
    }

    lateinit var vlcVideoLibrary: VlcVideoLibrary

    lateinit var surfaceView: SurfaceView
    lateinit var ffmpeg: FFmpeg

    lateinit var b_streenshot: Button
    lateinit var b_record: Button

    var currentTask: FFtask? = null
    lateinit var outputLayout: LinearLayout

    val dropboxManager = DropboxManager()
    val sdcardPath = Environment.getExternalStorageDirectory().toString()

    val dateFormatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")

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



        b_streenshot = mRootView.findViewById(R.id.b_streenshot);
        b_record = mRootView.findViewById(R.id.b_record);
        outputLayout = mRootView.findViewById(R.id.command_output);

        ffmpeg = FFmpeg.getInstance(mContext.getApplicationContext());

        if (!ffmpeg.isSupported()) {
            toast("!!ffmpeg not supported!!");
            b_streenshot.visibility = View.GONE
            b_record.visibility = View.GONE
        }

        surfaceView = mRootView.findViewById(R.id.surfaceView)
        vlcVideoLibrary = VlcVideoLibrary(mContext, object : VlcListener {
            override fun onComplete() {
                toast("onComplete")
                //vlcVideoLibrary.stop()
            }

            override fun onError() {
                toast("onError")

                vlcVideoLibrary.stop()
            }

        }, surfaceView)


        Utils.log("ip: '${mScreenParam.ipCamera.ip}' ")
        Utils.log("username: '${mScreenParam.ipCamera.username}' ")
        Utils.log("password: '${mScreenParam.ipCamera.password}' ")

        b_streenshot.setOnClickListener {
            takeScreenshot()
        }
        b_record.setOnClickListener {
            startStopRecording()
        }
        mScreenParam.streamingUrl.let {
            vlcVideoLibrary.play(it)
        }
    }

    var isRecording = false;
    private fun startStopRecording() {
        if (!isRecording && mScreenParam.streamingUrl != null) {

            val newFileName = "camera_video_${dateFormatter.format(Date())}.mp4"
            val cmd = "-y -i ${mScreenParam.streamingUrl} -vcodec copy -an $sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}/${newFileName}" //-t 900 timeout in sec

            execFFmpegBinary(cmd, Consumer {

                val savedFile = File("$sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}/${newFileName}")
                if (it || savedFile.length() > 0) {
                    dropboxManager.uploadFile("$sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}/${newFileName}", Utils.MEDIA_FOLDER).subscribe({
                        toast("Video uploaded on dropbox..")
                    }, {
                        toast("Error uploading video:" + it.message)
                    })

                    toast("Video saved, uploading to dropbox..")
                } else {
                    toast("Video not recorded!!")
                }
            })
            isRecording = true
            b_record.text = "Stop"

        } else {

            if (currentTask != null) {
                currentTask!!.sendQuitSignal()
            }
            isRecording = false
            b_record.text = "Record"
        }
    }

    private fun takeScreenshot() {

        mScreenParam.streamingUrl?.let {

            val newFileName = "camera_snapshot_${dateFormatter.format(Date())}.jpg"

            val cmd = "-y -i ${mScreenParam.streamingUrl} -vframes 1 $sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}/${newFileName}"


            execFFmpegBinary(cmd, Consumer {
                if (it) {
                    dropboxManager.uploadFile("$sdcardPath/${Utils.APP_FOLDER}/${Utils.MEDIA_FOLDER}/${newFileName}", Utils.MEDIA_FOLDER).subscribe({
                        toast("Scrrenshot uploaded on dropbox..")
                    }, {
                        toast("Error uploading screenshot:" + it.message)
                    })

                    toast("Screenshot saved, uploading to dropbox..")
                } else {
                    toast("Screenshot not recorded!!")
                }
            })
        }
    }

    private fun execFFmpegBinary(cmd: String, onComplete: Consumer<Boolean>) {
        val command = cmd.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        currentTask = ffmpeg.execute(command, object : ExecuteBinaryResponseHandler() {
            override fun onFailure(s: String?) {
                addTextViewToLayout("FAILED with output : " + s!!)
                onComplete.accept(false)
            }

            override fun onSuccess(s: String?) {
                addTextViewToLayout("SUCCESS with output : " + s!!)
                onComplete.accept(true)
            }

            override fun onProgress(s: String?) {
                Utils.log("Started command : ffmpeg " + s!!)
                addTextViewToLayout("progress : $s")
//                progressDialog.setMessage("Processing\n$s")
            }

            override fun onStart() {
                outputLayout.removeAllViews()

                Utils.log("Started command : ffmpeg ")
//                progressDialog.setMessage("Processing...")
//                progressDialog.show()
            }

            override fun onFinish() {
                Utils.log("Finished command : ffmpeg ")
//                progressDialog.dismiss()
            }
        })
    }

    private fun addTextViewToLayout(text: String) {
        Utils.log(text)
        val textView = TextView(mContext)
        textView.text = text
        outputLayout.addView(textView)
    }

    override fun onExit() {
        super.onExit()
        if (vlcVideoLibrary.isPlaying) {
            vlcVideoLibrary.stop()
        }
    }
}