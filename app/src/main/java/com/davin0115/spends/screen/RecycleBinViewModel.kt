package com.davin0115.spends.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davin0115.spends.database.CatatanDao
import com.davin0115.spends.model.Catatan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecycleBinViewModel(private val dao: CatatanDao) : ViewModel() {

    val deletedItems: StateFlow<List<Catatan>> = dao.getDeletedCatatan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun restoreItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.restoreById(id)
        }
    }

    fun permanentlyDeleteItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.permanentDeleteById(id)
            // Delete associated barang items
            dao.deleteBarangByCatatanId(id)
        }
    }

    fun emptyRecycleBin() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.emptyRecycleBin()
        }
    }
}