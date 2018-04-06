package com.aldroid.muzikashqipx.ui

import android.widget.Button
import com.aldroid.muzikashqipx.utils.PrefManager
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.R

class SettingsScreen : BaseTabScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_settings
    }

    val prefManager = PrefManager()

    override fun onEntered() {
        super.onEntered()
        bottomBar.selectedItemId = R.id.tab_settings
//        toolbar.title = "Settings"

        mRootView.findViewById<Button>(R.id.open_help).setOnClickListener({
            goTo(MainApp.PATH_HELP)
        })

        mRootView.findViewById<Button>(R.id.button_logout).setOnClickListener({
           prefManager.logout()
            close()
        })

    }

}