package com.davin0115.spends.database

import androidx.room.*
import com.davin0115.spends.model.Barang

@Dao
interface BarangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barang: Barang)

    @Query("SELECT * FROM barang WHERE catatanId = :catatanId")
    suspend fun getBarangForCatatan(catatanId: Long): List<Barang>

    @Query("DELETE FROM barang WHERE catatanId = :catatanId")
    suspend fun deleteByCatatanId(catatanId: Long)
}
