package com.aldroid.muzikashqipx.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R
import com.eltonkola.arkitekt.AppScreen
import com.github.florent37.awesomebar.AwesomeBar

abstract class BaseTabScreen<T> : AppScreen<T>() {

    protected lateinit var bottomBar: BottomNavigationView
    protected lateinit var toolbar: AwesomeBar

    protected lateinit var left_drawer : NavigationView
    protected lateinit var drawer_layout : DrawerLayout


    override fun onEntered() {
        super.onEntered()

        bottomBar = mRootView.findViewById(R.id.bottomBar)
        toolbar = mRootView.findViewById(R.id.toolbar)


        left_drawer= mRootView.findViewById(R.id.left_drawer)
//        val header = left_drawer.getHeaderView(0)
        drawer_layout= mRootView.findViewById(R.id.drawer_layout)

        left_drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_code-> {
                    mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/eltonkol/opencamera")))
                }

                R.id.nav_more-> {

                    try {
                        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Al%20Droid")))
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Al%20Droid")))
                    }

                }

                R.id.nav_rate-> {
                    val appPackageName = mContext.getPackageName()
                    try {
                        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
                    }
                }

                R.id.nav_share-> {
                    val appPackageName = mContext.getPackageName()
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out this app at: https://play.google.com/store/apps/details?id=" + appPackageName)
                    sendIntent.type = "text/plain"
                    mContext.startActivity(sendIntent)
                }
            }

            return@setNavigationItemSelectedListener true
        }

        toolbar.setOnMenuClickedListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                drawer_layout.openDrawer(Gravity.START)
            }
        })


        bottomBar.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.tab_cameras -> {
                    if (this is CameraListScreen) {
                        return@setOnNavigationItemSelectedListener false
                    }
                    goToAndClose(MainApp.PATH_CAMERA_LIST)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab_media-> {
                    if (this is MediaListScreen) {
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
                    //print("?? wehere did you click??")
                }
            }

            false
        }

    }


    override fun onOrientationChange(inPortraitMode: Boolean) {
        super.onOrientationChange(inPortraitMode)
    }


    fun runOnUiThread(toRun: Runnable) {
        (mContext as Activity).runOnUiThread(toRun)
    }

}