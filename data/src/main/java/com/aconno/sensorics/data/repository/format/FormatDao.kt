package com.aconno.sensorics.data.repository.format

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface FormatDao {

    @Query("SELECT contentJson FROM formats")
    fun getAllFormats(): List<String>

    @Query("SELECT id FROM formats")
    fun getAllFormatIds(): List<String>

    @Query("SELECT timestamp FROM formats WHERE id = :formatId")
    fun getTimestamp(formatId: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(formatEntity: FormatEntity)

    @Query("DELETE FROM formats WHERE id = :formatId")
    fun delete(formatId: String)
}