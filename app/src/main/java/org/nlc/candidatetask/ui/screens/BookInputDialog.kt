package org.nlc.candidatetask.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nlc.candidatetask.data.Book


@Composable
fun BookInputDialog(
    operation : String,
    book: Book? = null,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var bookTitle by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }

    if (book != null) {
        bookTitle = book.title
        bookAuthor = book.author
    }

    var title = if (operation == "Add") "Add Book" else "Edit Book"
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = bookTitle,
                    onValueChange = { bookTitle = it },
                    label = { Text("Book Title") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = bookAuthor,
                    onValueChange = { bookAuthor = it },
                    label = { Text("Book Author") }
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                    onSave(bookTitle, bookAuthor)
                    onDismiss()
                }) {
                    Text(operation)
                }
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button( modifier = Modifier.fillMaxWidth(),
                    onClick = onDismiss) {
                    Text("Cancel")
                }

            }        }
    )
}

@Preview
@Composable
fun BookInputDialogPreview() {
    BookInputDialog(
        operation = "Add",
        onDismiss = {},
        onSave = { _, _ -> }
    )
}

@Preview
@Composable
fun BookUpdateDialogPreview() {
    BookInputDialog(
        operation = "Edit",
        onDismiss = {},
        onSave = { _, _ -> }
    )
}