package com.aldroid.muzikashqipx.ui

import android.webkit.WebView
import com.aldroid.cryptomarketcap.ui.BaseSecondaryScreen
import com.aldroid.opencamera.R

class HelpScreen : BaseSecondaryScreen<Void>() {

    override fun getView(): Int {
        return R.layout.screen_help
    }

    override fun onEntered() {
        super.onEntered()

        val webview = mRootView.findViewById<WebView>(R.id.webview)
        webview.loadUrl("file:///android_asset/help.html")

    }

}