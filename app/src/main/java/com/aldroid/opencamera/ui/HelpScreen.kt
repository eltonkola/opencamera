package com.aldroid.muzikashqipx.ui

import android.support.v7.widget.Toolbar
import android.webkit.WebView
import com.aldroid.opencamera.R
import com.eltonkola.arkitekt.AppScreen

class HelpScreen : AppScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_help
    }

    override fun onEntered() {
        super.onEntered()

        val toolbar = mRootView.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "About/ Info"

        val webview = mRootView.findViewById<WebView>(R.id.webview)
        webview.loadUrl("file:///android_asset/help.html")

    }


}