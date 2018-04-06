package com.aldroid.muzikashqipx.ui

import android.Manifest
import android.app.Activity
import android.support.v7.widget.Toolbar
import android.widget.Button
import com.aldroid.muzikashqipx.utils.PrefManager
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.aldroid.opencamera.dropbox.DropboxClientFactory
import com.aldroid.opencamera.dropbox.PicassoClient
import com.aldroid.opencamera.util.Encryptor
import com.aldroid.opencamera.util.Utils
import com.dropbox.core.android.Auth
import com.eltonkola.arkitekt.AppScreen
import com.tbruyelle.rxpermissions2.RxPermissions


class SplashScreen : AppScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_splash
    }

    val prefManager = PrefManager()

    override fun onEntered() {
        super.onEntered()

        val loginButton = mRootView.findViewById(R.id.login_button) as Button

        loginButton.setOnClickListener {
            Auth.startOAuth2Authentication(mContext, Utils.APP_KEY)
        }

        val key = "eltonkola" // 128 bit key (16 char string)
        val initVector = "varikarinsd;slfsdjflsjdlfjsldkf" // 16 bytes IV


        Utils.log("-----encryptor text----")
        val origjinali = "this is some encryped text, will use it later to encrypt ipcameras password"
        val enkryped = Encryptor.encrypt(key, initVector, origjinali)
        val dekripted = Encryptor.decrypt(key, initVector, enkryped)
        Utils.log("origjinali: " + origjinali)
        Utils.log("enkryped: " + enkryped)
        Utils.log("dekripted: " + dekripted)

        Utils.log("-----encryptor text----")

    }


    override fun onResume() {
        super.onResume()

        var accessToken = prefManager.getAccessToken()
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token()
            if (accessToken != null) {
                prefManager.setAccessToken(accessToken)
                MainApp.instance.getCameraListManager()
                gotToApp()

            }
        } else {
            gotToApp()
        }

        val uid = Auth.getUid()
        val storedUid = prefManager.getUserId()
        if (uid != null && uid != storedUid) {
            prefManager.setUserId(uid)
        }
    }

    fun gotToApp(){
        val rxPermissions = RxPermissions(mContext as Activity)

        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe{ granted ->
                    if (granted) {
                        goToAndClose(MainApp.PATH_CAMERA_LIST)
                    } else {
                        toast("We need the permission to use this app!")
                        close()
                    }
                }
    }


}