package com.davin0115.spends.Screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.davin0115.spends.R
import com.davin0115.spends.ui.theme.poppinsFamily


@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = { Text (text = stringResource(R.string.delete_message),) },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    text = stringResource(R.string.delete_button),
                    fontFamily = poppinsFamily,
                    )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = stringResource(R.string.cancel_button),
                    fontFamily = poppinsFamily)
            }
        },
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview
@Composable
fun DisplayAlertDialogPreview (){
    DisplayAlertDialog(onDismissRequest = {},
        onConfirmation = {}
    )
}