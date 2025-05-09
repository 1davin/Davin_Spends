package com.davin0115.spends.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davin0115.spends.database.CatatanDao
import com.davin0115.spends.model.Barang
import com.davin0115.spends.model.BarangInput
import com.davin0115.spends.model.Catatan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: CatatanDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insertWithBarangList(judul: String, isi: String, barangList: List<BarangInput>) {
        val catatan = Catatan(
            tanggal = formatter.format(Date()),
            judul = judul,
            catatan = isi,
            isDeleted = false
        )

        viewModelScope.launch(Dispatchers.IO) {
            val newId = dao.insert(catatan)
            val barangToInsert = barangList.map {
                Barang(
                    catatanId = newId,
                    nama = it.nama,
                    harga = it.harga.toIntOrNull() ?: 0
                )
            }
            barangToInsert.forEach { dao.insertBarang(it) }
        }
    }

    suspend fun getCatatan(id: Long): Catatan? {
        return dao.getCatatanById(id)
    }

    fun update(id: Long, judul: String, isi: String, barangList: List<BarangInput>) {
        viewModelScope.launch(Dispatchers.IO) {
            // Get the current note to preserve isDeleted status
            val currentCatatan = dao.getCatatanById(id)

            val catatan = Catatan(
                id = id,
                tanggal = formatter.format(Date()),
                judul = judul,
                catatan = isi,
                isDeleted = currentCatatan?.isDeleted ?: false
            )

            dao.update(catatan)
            dao.deleteBarangByCatatanId(id)

            val barangToInsert = barangList.map {
                Barang(
                    catatanId = id,
                    nama = it.nama,
                    harga = it.harga.toIntOrNull() ?: 0
                )
            }

            barangToInsert.forEach { dao.insertBarang(it) }
        }
    }

    suspend fun getBarangList(catatanId: Long): List<Barang> {
        return dao.getBarangByCatatanId(catatanId)
    }

    // Implement soft delete function
    fun softDelete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.softDeleteById(id)
        }
    }
}