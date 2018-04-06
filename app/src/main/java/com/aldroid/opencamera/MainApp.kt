package com.aldroid.opencamera

import com.aldroid.muzikashqipx.ui.*
import com.aldroid.muzikashqipx.utils.PrefManager
import com.aldroid.opencamera.dropbox.CameraListManager
import com.aldroid.opencamera.dropbox.DropboxClientFactory
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.dropbox.RichCameraListManager
import com.eltonkola.arkitekt.ArkitektApp

class MainApp : ArkitektApp() {

    companion object {

        val PATH_SPLASH = "/"
        val PATH_CAMERA_LIST = "/camera"
        val PATH_CAMERA_ADD = "/camera/add"
        val PATH_CAMERA_VIEW = "/camera/view"
        val PATH_MEDIA = "/media"
        val PATH_MEDIA_SHOW_IMAGE = "/media/show_image"
        val PATH_MEDIA_SHOW_VIDEO = "/camera/show_video"
        val PATH_SETTINGS = "/settings"
        val PATH_HELP = "/settings/help"

        private lateinit var INSTANCE: MainApp
        val instance: MainApp get() = INSTANCE

    }
    override fun routeConfig() {

        addScreen(PATH_SPLASH, SplashScreen::class.java)
        addScreen(PATH_CAMERA_LIST, CameraListScreen::class.java)
        addScreen(PATH_MEDIA, MediaListScreen::class.java)
        addScreen(PATH_MEDIA_SHOW_IMAGE, ShowImageScreen::class.java)
        addScreen(PATH_MEDIA_SHOW_VIDEO, ShowVideoScreen::class.java)
        addScreen(PATH_CAMERA_ADD, CameraAddScreen::class.java)
        addScreen(PATH_CAMERA_VIEW, CameraPlayScreen::class.java)
        addScreen(PATH_SETTINGS, SettingsScreen::class.java)
        addScreen(PATH_HELP, HelpScreen::class.java)
    }

    lateinit var prefManager : PrefManager
    lateinit var cameraListManager : RichCameraListManager


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        prefManager = PrefManager()

        getCameraListManager()
    }

    fun getCameraListManager(){
        if (prefManager.getAccessToken() != null){
            DropboxClientFactory.init(prefManager.getAccessToken())
            PicassoClient.init(MainApp.instance, DropboxClientFactory.getClient())
            cameraListManager = RichCameraListManager(CameraListManager())
        }
    }

}
