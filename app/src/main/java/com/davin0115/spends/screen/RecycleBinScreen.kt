package com.davin0115.spends.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.davin0115.spends.R
import com.davin0115.spends.model.Catatan
import com.davin0115.spends.ui.theme.MainColor
import com.davin0115.spends.ui.theme.SecondColor
import com.davin0115.spends.ui.theme.poppinsFamily
import com.davin0115.spends.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RecycleBinViewModel = viewModel(factory = factory)

    val deletedItems by viewModel.deletedItems.collectAsState()
    var showEmptyBinDialog by remember { mutableStateOf(false) }

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
                            text = "Recycle Bin",
                            fontFamily = poppinsFamily,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    // Empty recycle bin button
                    if (deletedItems.isNotEmpty()) {
                        IconButton(onClick = { showEmptyBinDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Empty Bin",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (deletedItems.isEmpty()) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recycle bin is empty",
                    fontFamily = poppinsFamily
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(deletedItems) { item ->
                    RecycleBinItem(
                        catatan = item,
                        onRestore = { viewModel.restoreItem(item.id) },
                        onDelete = { viewModel.permanentlyDeleteItem(item.id) }
                    )
                    HorizontalDivider()
                }
            }
        }

        // Dialog to confirm emptying the recycle bin
        if (showEmptyBinDialog) {
            AlertDialog(
                onDismissRequest = { showEmptyBinDialog = false },
                title = {
                    Text(text = "Empty Recycle Bin?")
                },
                text = {
                    Text(text = "This will permanently delete all items in the recycle bin. This action cannot be undone.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.emptyRecycleBin()
                            showEmptyBinDialog = false
                        }
                    ) {
                        Text("Empty Bin")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showEmptyBinDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun RecycleBinItem(
    catatan: Catatan,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = catatan.judul,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = catatan.catatan,
                fontFamily = poppinsFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = catatan.tanggal,
                fontFamily = poppinsFamily
            )
        }

        Row {
            IconButton(onClick = onRestore) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restore"
                )
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Permanently"
                )
            }
        }
    }

    // Confirmation dialog for permanent deletion
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = "Delete Permanently?")
            },
            text = {
                Text(text = "This item will be permanently deleted and cannot be recovered.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}