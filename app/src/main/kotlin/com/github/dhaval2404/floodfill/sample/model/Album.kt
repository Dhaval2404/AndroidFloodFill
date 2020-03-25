package com.github.dhaval2404.floodfill.sample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AlbumImages(
    val albums: List<Album>
)

@Parcelize
data class Album(
    val title: String,
    val images: List<Image>
) : Parcelable

@Parcelize
data class Image(
    val url: String,
    val copyright: String
) : Parcelable