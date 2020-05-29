package com.github.dhaval2404.floodfill.sample.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "album_id")
    var albumId: Long = 0,
    @ColumnInfo
    val url: String,
    @ColumnInfo
    val copyright: String,
    @ColumnInfo(name = "local_url")
    var localPath: String? = null,
    @ColumnInfo(name = "timestamp")
    var timestamp: Date = Date()
) : Parcelable