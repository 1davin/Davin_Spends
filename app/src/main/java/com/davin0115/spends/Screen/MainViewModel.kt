package com.davin0115.spends.Screen

import androidx.lifecycle.ViewModel
import com.davin0115.spends.model.Catatan

class MainViewModel: ViewModel() {

    val data = listOf(
        Catatan(
            1,
            "Kuliah Mobpro 17 Feb",
            "Kuliah hari pertama. Ternyata keren juga yang mau dipelajari.",
            "2025-02-17 12:34:56"
        ),
        Catatan(
            2,
            "Kuliah Mobpro 19 Feb",
            "Praktikum pertama: running modul. Alhamdulillah hari ini lancar.",
            "2025-02-19 12:34:56"
        ),
        Catatan(
            3,
            "Sepi",
            "Mahasiswa udah pada balik kampung, sepi banget kampus bos.",
            "2025-03-21 08:32:49"
        ),
        Catatan(
            4,
            "Membingungkan",
            "Gatau saya yang salah apa mereka yang salah, saya sangat bingung.",
            "2025-02-01 23:00:00"
        ),
        Catatan(
            5,
            "MMM ME MUSIC MALAM",
            "Hanya saya, musik, dan malam.",
            "2025-12-31 23:02:01"
        ),
        Catatan(
            6,
            "Kuliah Mobpro 05 Mar",
            "Praktikum kali ini bikin aplikasi Galeri Hewan" + "Klik tombol lanjut, maka foto dan nama hewannya berubah.",
            "2025-03-05 12:34:56"
        )
    )

    fun getCatatan(id: Long): Catatan? {
        return data.find { it.id == id }
    }

}