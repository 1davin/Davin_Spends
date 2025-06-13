package com.davin0115.spends.screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage
import coil.request.ImageRequest

import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.davin0115.spends.R
import com.davin0115.spends.model.Gallery
import com.davin0115.spends.network.GalleryApi
import com.davin0115.spends.ui.theme.poppinsFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateGalleryDialog(
    initialGallery: Gallery,
    onDismissRequest: () -> Unit,
    onUpdate: (id: String, judul: String, keterangan: String, bitmap: Bitmap?) -> Unit,
    onDelete: (id: String) -> Unit,
    onImageSelected: (bitmap: Bitmap?) -> Unit
) {
    var judul by remember { mutableStateOf(initialGallery.judul) }
    var keterangan by remember { mutableStateOf(initialGallery.keterangan) }
    var newBitmap: Bitmap? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        newBitmap = getCroppedImage(context.contentResolver, it)
        onImageSelected(newBitmap)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(
            text = "Edit Gallery Item",
            fontFamily = poppinsFamily
        ) },
        text = {
            Column {
                TextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text(
                        text = "Judul",
                        fontFamily = poppinsFamily
                    ) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                TextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text(
                        text = "Keterangan",
                        fontFamily = poppinsFamily
                    ) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        val options = CropImageContractOptions(
                            null, CropImageOptions(
                                imageSourceIncludeGallery = true,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                        launcher.launch(options)
                    }) {
                        Text(
                            text = "Change Image",
                            fontFamily = poppinsFamily
                        )
                    }
                    if (newBitmap != null) {
                        Image(
                            bitmap = newBitmap!!.asImageBitmap(),
                            contentDescription = "New Image Preview",
                            modifier = Modifier.size(64.dp)
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(GalleryApi.getGalleryUrl(initialGallery.gambar))
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(R.string.image, initialGallery.judul),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.baseline_broken_image_24),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    onDelete(initialGallery.id)
                    onDismissRequest()
                }) {
                    Text(
                        text = "Delete",
                        fontFamily = poppinsFamily,
                        color = Color.Red
                    )
                }
                TextButton(onClick = {
                    onUpdate(initialGallery.id, judul, keterangan, newBitmap)
                    onDismissRequest()
                }) {
                    Text(
                        text = "Update",
                        fontFamily = poppinsFamily
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "Cancel",
                    fontFamily = poppinsFamily
                )
            }
        }
    )
}