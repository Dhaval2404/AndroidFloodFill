package com.github.dhaval2404.floodfill.sample.di

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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
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

val dataModule = module {
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