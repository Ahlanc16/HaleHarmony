package com.example.haleharmony

import android.app.Application
import com.example.haleharmony.data.AppContainer
import com.example.haleharmony.data.AppDataContainer
class HaleHarmonyApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}