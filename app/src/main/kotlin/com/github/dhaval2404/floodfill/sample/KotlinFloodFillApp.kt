package com.github.dhaval2404.floodfill.sample

import android.app.Application
import com.github.dhaval2404.floodfill.sample.di.dataModule
import com.github.dhaval2404.floodfill.sample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author Dhaval Patel
 * @version 1.0
 * @since 22 Nov 2019
 */
class KotlinFloodFillApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            // Android context
            androidContext(this@KotlinFloodFillApp)
            // your modules
            modules(dataModule + viewModelModule)
        }
    }

}
