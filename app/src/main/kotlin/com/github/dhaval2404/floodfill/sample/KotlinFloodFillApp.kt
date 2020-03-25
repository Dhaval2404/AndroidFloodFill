package com.github.dhaval2404.floodfill.sample

import android.app.Application
import com.github.dhaval2404.floodfill.sample.screens.drawing.DrawingViewModel
import com.github.dhaval2404.floodfill.sample.screens.image_category.MainViewModel
import com.github.dhaval2404.floodfill.sample.screens.image_picker.ImagePickerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

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
        val modules = module {
            viewModel  {
                MainViewModel()
            }
            viewModel  {
                ImagePickerViewModel()
            }
            viewModel  {
                DrawingViewModel()
            }
        }

        startKoin {
            // Android context
            androidContext(this@KotlinFloodFillApp)
            // your modules
            modules(modules)
        }
    }

}
