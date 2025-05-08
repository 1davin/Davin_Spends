package com.davin0115.spends.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davin0115.spends.model.Barang
import com.davin0115.spends.model.Catatan
import kotlinx.coroutines.flow.Flow

@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan): Long

    @Update
    suspend fun  update(catatan: Catatan)

    @Query("SELECT * FROM catatan ORDER BY tanggal DESC")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

    // Insert barang
    @Insert
    suspend fun insertBarang(barang: Barang)

    // Update barang
    @Update
    suspend fun updateBarang(barang: Barang)

    // Delete semua barang untuk catatan tertentu (optional)
    @Query("DELETE FROM barang WHERE catatanId = :catatanId")
    suspend fun deleteBarangByCatatanId(catatanId: Long)

    // Get semua barang berdasarkan catatanId
    @Query("SELECT * FROM barang WHERE catatanId = :catatanId")
    suspend fun getBarangByCatatanId(catatanId: Long): List<Barang>

}