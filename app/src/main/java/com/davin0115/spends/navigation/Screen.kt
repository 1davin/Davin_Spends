package com.davin0115.spends.navigation

import com.davin0115.spends.screen.KEY_ID_CATATAN

sealed class Screen (val route: String){
    data object Home: Screen("homeScreen")
    data object Gallery: Screen("galleryScreen")
    data object Main: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_CATATAN}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
    object Edit : Screen("edit_screen/{galleryId}") {
        fun createRoute(galleryId: String) = "edit_screen/$galleryId"
    }
    data object RecycleBinScreen: Screen("binScreen")

}