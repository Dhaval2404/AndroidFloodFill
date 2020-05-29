package com.github.dhaval2404.floodfill.sample

import android.app.Application
import android.content.Context
import com.github.dhaval2404.floodfill.sample.data.AppDatabase
import com.github.dhaval2404.floodfill.sample.data.repository.AppPrefRepository
import com.github.dhaval2404.floodfill.sample.data.repository.AppPrefRepositoryImpl
import com.github.dhaval2404.floodfill.sample.data.repository.ImageRepository
import com.github.dhaval2404.floodfill.sample.data.repository.ImageRepositoryImpl
import com.github.dhaval2404.floodfill.sample.data.shared_pref.SharedPrefManager
import com.github.dhaval2404.floodfill.sample.screens.album_picker.AlbumPickerViewModel
import com.github.dhaval2404.floodfill.sample.screens.drawing_view.DrawingViewModel
import com.github.dhaval2404.floodfill.sample.screens.image_picker.ImagePickerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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
            viewModel {
                AlbumPickerViewModel()
            }

            viewModel {
                ImagePickerViewModel()
            }

            viewModel {
                DrawingViewModel()
            }
        }

        val database = module {
            single {
                AppDatabase.getAppDatabase(get())
            }
            single {
                (get() as AppDatabase).imageDAO()
            }
            single {
                SharedPrefManager(get(), (get() as Context).packageName)
            }
            single<ImageRepository> {
                ImageRepositoryImpl(get(), get())
            }
            single<AppPrefRepository> {
                AppPrefRepositoryImpl(get())
            }
        }

        startKoin {
            // Android context
            androidContext(this@KotlinFloodFillApp)
            // your modules
            modules(database + modules)
        }
    }

}
