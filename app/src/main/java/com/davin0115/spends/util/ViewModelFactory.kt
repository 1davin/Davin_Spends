package com.davin0115.spends.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davin0115.spends.screen.DetailViewModel
import com.davin0115.spends.screen.MainViewModel
import com.davin0115.spends.database.CatatanDb

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = CatatanDb.getInstance(context).dao
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(dao) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(dao) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

