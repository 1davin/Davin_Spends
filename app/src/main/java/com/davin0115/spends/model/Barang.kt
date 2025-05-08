package com.davin0115.spends.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "barang",
    foreignKeys = [ForeignKey(
        entity = Catatan::class,
        parentColumns = ["id"],
        childColumns = ["catatanId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Barang(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val catatanId: Long,
    val nama: String,
    val harga: Int
)
