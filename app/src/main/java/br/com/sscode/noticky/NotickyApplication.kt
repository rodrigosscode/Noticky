package br.com.sscode.noticky

import android.app.Application
import timber.log.Timber

class NotickyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        configureTimber()
    }

    private fun configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}