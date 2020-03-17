package com.example.kotlintodoapp.helper

import android.util.Log
import com.example.kotlintodoapp.BuildConfig

object TodoLogger {
    private val TAG = TodoLogger::javaClass.name

    fun debug (tag: String = TAG , message: String) {
        if(BuildConfig.DEBUG){
            Log.d(tag, message)
        }
    }
}