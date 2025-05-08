package com.davin0115.spends.Screen

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

    // Fungsi untuk menyimpan catatan baru
    fun insert(judul: String, isi: String) {
        val catatan = Catatan(
            tanggal = formatter.format(Date()),
            judul = judul,
            catatan = isi
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(catatan)
        }
    }

    fun insertWithBarangList(judul: String, isi: String, barangList: List<BarangInput>) {
        val catatan = Catatan(
            tanggal = formatter.format(Date()),
            judul = judul,
            catatan = isi // ← langsung dari input user
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

    // Fungsi untuk mengambil catatan berdasarkan ID
    suspend fun getCatatan(id: Long): Catatan? {
        return dao.getCatatanById(id)
    }

    // Fungsi untuk update catatan dan barang
    fun update(id: Long, judul: String, isi: String, barangList: List<BarangInput>) {
        val catatan = Catatan(
            id = id,
            tanggal = formatter.format(Date()),
            judul = judul,
            catatan = isi
        )

        viewModelScope.launch(Dispatchers.IO) {
            // Update catatan
            dao.update(catatan)

            // Hapus barang yang lama
            dao.deleteBarangByCatatanId(id)

            // Insert barang yang baru
            val barangToInsert = barangList.map {
                Barang(
                    catatanId = id,
                    nama = it.nama,
                    harga = it.harga.toIntOrNull() ?: 0
                )
            }

            // Insert barang menggunakan insertBarang (bukan insertBarangList)
            barangToInsert.forEach { dao.insertBarang(it) }
        }
    }

    // Fungsi untuk menghapus catatan
    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    // Fungsi untuk mendapatkan barang berdasarkan catatanId
    suspend fun getBarangList(catatanId: Long): List<Barang> {
        return dao.getBarangByCatatanId(catatanId)
    }
}



