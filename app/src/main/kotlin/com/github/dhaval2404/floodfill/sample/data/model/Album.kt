package com.github.dhaval2404.floodfill.sample.data.model

import android.os.Parcelable
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import kotlinx.android.parcel.Parcelize

data class AlbumImages(
    val albums: List<Album>
)

@Parcelize
data class Album(
    val id:Long,
    val title: String,
    val images: List<Image>
) : Parcelable