package com.davin0115.spends.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davin0115.spends.R
import com.davin0115.spends.model.Gallery
import com.davin0115.spends.network.GalleryApi
import com.davin0115.spends.ui.theme.MainColor
import com.davin0115.spends.ui.theme.SecondColor
import com.davin0115.spends.ui.theme.poppinsFamily

@Composable
fun GalleryScreen(navController: NavHostController) {
    Scaffold (
        topBar = {
            GradientTopBarGallery(
                title = "Gallery",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        GalleryContent(Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun GalleryContent(modifier: Modifier, navController: NavHostController){
    val viewModel: GalleryViewModel = viewModel()
    val data by viewModel.data

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize().padding(4.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(data) { ListGallery(gallery = it) }
    }


}

@Composable
fun ListGallery(gallery: Gallery) {
    Box (
        modifier = Modifier.padding(4.dp).border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(GalleryApi.getGalleryUrl(gallery.gambar))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.image, gallery.judul),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
        Column (
            modifier = Modifier.fillMaxWidth().padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
        ){
            Text(
                text = gallery.judul,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = poppinsFamily
            )
            Text(
                text = gallery.keterangan,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.White,
                fontFamily = poppinsFamily
            )
        }
    }
}

@Composable
fun GradientTopBarGallery(
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MainColor,
                        SecondColor
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
