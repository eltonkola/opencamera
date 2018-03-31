package com.aldroid.muzikashqipx.ui

import android.app.Activity
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.eltonkola.arkitekt.AppScreen

abstract class BaseScreen<T> : AppScreen<T>() {

    protected lateinit var bottomBar: BottomNavigationView
    protected lateinit var toolbar: Toolbar
    override fun onEntered() {
        super.onEntered()

        bottomBar = mRootView.findViewById(R.id.bottomBar)
        toolbar = mRootView.findViewById(R.id.toolbar)

        bottomBar.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.tab_cameras -> {
                    if (this is CamerasScreen) {
                        return@setOnNavigationItemSelectedListener false
                    }
                    goToAndClose(MainApp.PATH_CAMERA_LIST)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab_media-> {
                    if (this is MediaScreen) {
                        return@setOnNavigationItemSelectedListener false
                    }
                    goToAndClose(MainApp.PATH_MEDIA)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab_settings-> {
                    if (this is SettingsScreen) {
                        return@setOnNavigationItemSelectedListener false
                    }
                    goToAndClose(MainApp.PATH_SETTINGS)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> { // Note the block
                    print("?? wehere did you click??")
                }
            }

            false
        }

    }

    fun runOnUiThread(toRun: Runnable) {
        (mContext as Activity).runOnUiThread(toRun)
    }

}