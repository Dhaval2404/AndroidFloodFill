package com.github.dhaval2404.floodfill.sample.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.dhaval2404.floodfill.sample.data.entity.Image
import kotlinx.coroutines.flow.Flow


/**
 * Image DAO
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 05 April 2020
 */
@Dao
interface ImageDAO {

    @Query("SELECT * FROM image")
    fun getAll(): LiveData<List<Image>>

    @Query("SELECT * FROM image where album_id=:albumId")
    fun getByAlbumId(albumId: Long): Flow<List<Image>>

    @Query("SELECT * FROM image where album_id=:albumId order by timestamp desc limit 1")
    fun getLastModifiedImage(albumId: Long): Image

    @Query("SELECT * FROM image where id=:id")
    fun getById(id: Long): Image

    @Query("SELECT count(*) FROM image")
    fun getCount(): Int

    @Insert
    fun insert(images: List<Image>)

    @Update
    fun update(image: Image)

    @Delete
    fun delete(image: Image)

}