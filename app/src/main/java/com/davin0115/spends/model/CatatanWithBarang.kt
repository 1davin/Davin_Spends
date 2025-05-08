package com.davin0115.spends.model

import androidx.room.Embedded
import androidx.room.Relation

data class CatatanWithBarang(
    @Embedded val catatan: Catatan,
    @Relation(
        parentColumn = "id",
        entityColumn = "catatanId"
    )
    val barangList: List<Barang>
)
