package com.davin0115.spends.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.davin0115.spends.R
import com.davin0115.spends.model.Barang
import com.davin0115.spends.model.BarangInput
import com.davin0115.spends.ui.theme.MainColor
import com.davin0115.spends.ui.theme.SecondColor
import com.davin0115.spends.ui.theme.poppinsFamily
import com.davin0115.spends.util.ViewModelFactory

const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    val barangList = remember { mutableStateListOf(BarangInput()) }
    val barangToDelete = remember { mutableStateListOf<Barang>() } // Menyimpan barang yang dihapus

    var judul by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id != null) {
            val data = viewModel.getCatatan(id)
            if (data != null) {
                judul = data.judul
                catatan = data.catatan
                val barangFromDb = viewModel.getBarangList(id)
                barangList.clear()
                barangList.addAll(barangFromDb.map { BarangInput(it.nama, it.harga.toString()) })
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(MainColor, SecondColor)
                        )
                    )
                    .padding(top = 50.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = if (id == null)
                                stringResource(id = R.string.add_note)
                            else
                                stringResource(id = R.string.edit_note),
                            fontFamily = poppinsFamily,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (judul.isBlank() || barangList.any { it.nama.isBlank() || it.harga.isBlank() }) {
                                Toast.makeText(context, "Isi semua data barang!", Toast.LENGTH_LONG).show()
                                return@IconButton
                            }

                            if (id == null) {
                                viewModel.insertWithBarangList(judul, catatan, barangList)

                            } else {
                                viewModel.update(id, judul, catatan, barangList)
                            }



                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.save),
                                tint = Color.White
                            )
                        }
                        if (id != null) {
                            DeleteAction {
                                viewModel.delete(id)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }

    ) { padding ->
        FormCatatan(
            title = judul,
            onTitleChange = { judul = it },
            desc = catatan,
            onDescChange = { catatan = it },
            barangList = barangList,
            onBarangChange = { index, barang -> barangList[index] = barang },
            onTambahBarang = { barangList.add(BarangInput()) },
            onHapusBarang = { index ->
                if (barangList.size > 1) {
                    barangList.removeAt(index)
                } else {
                    val barang = barangList[0]
                    if (barang.nama.isNotBlank() || barang.harga.isNotBlank()) {
                        barangList.removeAt(0)
                    } else {
                        Toast.makeText(context, "Minimal 1 item harus ada.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FormCatatan(
    title: String,
    onTitleChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    barangList: SnapshotStateList<BarangInput>,
    onBarangChange: (Int, BarangInput) -> Unit,
    onTambahBarang: () -> Unit,
    onHapusBarang: (Int) -> Unit,
    modifier: Modifier
) {

    barangList.forEachIndexed { index, barang ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text(text = stringResource(R.string.title), fontFamily = poppinsFamily) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = desc,
                onValueChange = { onDescChange(it) },
                label = { Text(text = stringResource(R.string.note), fontFamily = poppinsFamily) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                stringResource(R.string.item_list),
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )

            barangList.forEachIndexed { index, barang ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = barang.nama,
                        onValueChange = {
                            onBarangChange(index, barang.copy(nama = it))
                        },
                        label = {
                            Text(
                                stringResource(R.string.item_name),
                                fontFamily = poppinsFamily
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = barang.harga,
                        onValueChange = {
                            onBarangChange(index, barang.copy(harga = it))
                        },
                        label = {
                            Text(
                                stringResource(R.string.price),
                                fontFamily = poppinsFamily
                            )
                        },
                        modifier = Modifier.width(100.dp)
                    )
                    IconButton(
                        onClick = { onHapusBarang(index) }

                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            }

            Button(
                onClick = onTambahBarang,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.add_item), fontFamily = poppinsFamily)
            }
        }
    }
}



@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.more),
            tint = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.delete))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}