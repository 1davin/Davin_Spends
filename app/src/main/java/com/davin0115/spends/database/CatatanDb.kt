package com.davin0115.spends.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davin0115.spends.model.Barang
import com.davin0115.spends.model.Catatan

@Database(
    entities = [Catatan::class, Barang::class],
    version = 1,
    exportSchema = false
)
abstract class CatatanDb : RoomDatabase() {

    abstract val dao: CatatanDao
    abstract val barangDao: BarangDao

    companion object {
        @Volatile
        private var INSTANCE: CatatanDb? = null

        fun getInstance(context: Context): CatatanDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CatatanDb::class.java,
                        "catatan.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
