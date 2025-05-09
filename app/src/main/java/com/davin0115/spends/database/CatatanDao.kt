package com.davin0115.spends.database

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun insertBarang(barang: Barang)

    @Update
    suspend fun updateBarang(barang: Barang)

    @Delete
    suspend fun deleteBarang(barang: Barang)

    @Query("DELETE FROM barang WHERE catatanId = :catatanId")
    suspend fun deleteBarangByCatatanId(catatanId: Long)

    @Query("SELECT * FROM barang WHERE catatanId = :catatanId")
    suspend fun getBarangByCatatanId(catatanId: Long): List<Barang>

    @Query("SELECT * FROM catatan WHERE isDeleted = 0 ORDER BY tanggal DESC")
    fun getCatatan(): Flow<List<Catatan>>

    // Get deleted items for recycle bin
    @Query("SELECT * FROM catatan WHERE isDeleted = 1 ORDER BY tanggal DESC")
    fun getDeletedCatatan(): Flow<List<Catatan>>

    // Soft delete (move to recycle bin)
    @Query("UPDATE catatan SET isDeleted = 1 WHERE id = :id")
    suspend fun softDeleteById(id: Long)

    // Restore from recycle bin
    @Query("UPDATE catatan SET isDeleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)

    // Permanently delete item
    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun permanentDeleteById(id: Long)

    // Empty recycle bin (delete all soft-deleted items)
    @Query("DELETE FROM catatan WHERE isDeleted = 1")
    suspend fun emptyRecycleBin()


}