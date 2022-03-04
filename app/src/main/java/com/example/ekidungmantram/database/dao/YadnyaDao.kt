package com.example.ekidungmantram.database.dao

import androidx.room.*
import com.example.ekidungmantram.database.data.Yadnya

@Dao
interface YadnyaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmarkedYadnya(yadnya: Yadnya)

    @Delete
    suspend fun deleteBookmarkedYadnya(yadnya: Yadnya)

    @Query("DELETE FROM yadnyabookmarked WHERE id_yadnya = :id")
    suspend fun deleteById(id: Int)

    @Query("Select * from yadnyabookmarked where id_yadnya = :id")
    suspend fun fetch(id: Int) : Yadnya

    @Query("Select * from yadnyabookmarked")
    suspend fun getAllBookmarkedYadnya() : List<Yadnya>

}