package eryaz.software.carParts.core

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import eryaz.software.carParts.data.di.appModuleApis
import eryaz.software.carParts.data.di.appModuleRepos
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class ZeusApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        init()
    }

    private fun init() {
        SessionManager.init(applicationContext)

            Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ZeusApp)
            androidFileProperties()
            modules(listOf(appModule, appModuleApis, appModuleRepos))
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}