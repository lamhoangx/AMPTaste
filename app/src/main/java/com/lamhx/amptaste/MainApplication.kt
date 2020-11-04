package com.lamhx.amptaste

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    companion object {
        private lateinit var mainAppContext: Context
        @JvmStatic
        fun getAppContext(): Context {
            return mainAppContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mainAppContext = applicationContext
    }
}