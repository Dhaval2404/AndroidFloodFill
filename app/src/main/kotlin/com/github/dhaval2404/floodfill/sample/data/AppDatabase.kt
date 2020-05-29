package com.github.dhaval2404.floodfill.sample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.dhaval2404.floodfill.sample.data.converter.DateConverter
import com.github.dhaval2404.floodfill.sample.data.dao.ImageDAO
import com.github.dhaval2404.floodfill.sample.data.entity.Image

@Database(entities = [Image::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        fun getAppDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "flood_fill")
                .allowMainThreadQueries()
                .build()
        }

    }

    abstract fun imageDAO(): ImageDAO

}