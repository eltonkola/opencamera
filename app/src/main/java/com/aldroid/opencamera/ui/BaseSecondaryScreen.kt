package com.aldroid.cryptomarketcap.ui

import android.app.Activity
import com.aldroid.opencamera.R
import com.eltonkola.arkitekt.AppScreen
import com.github.florent37.awesomebar.AwesomeBar

abstract class BaseSecondaryScreen<T> : AppScreen<T>() {

    protected lateinit var toolbar : AwesomeBar

    override fun onEntered() {
        super.onEntered()

        toolbar = mRootView.findViewById<AwesomeBar>(R.id.toolbar)
        toolbar.displayHomeAsUpEnabled(true)
        toolbar.setOnMenuClickedListener({
            close()
        })

    }

    fun runOnUiThread(toRun: Runnable){
        (mContext as Activity).runOnUiThread(toRun)
    }


}