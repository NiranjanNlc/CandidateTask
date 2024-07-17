import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.nlc.candidatetask.R
import org.nlc.candidatetask.data.Book

@Composable
fun BookListItem(
    book: Book,
    onEditClick: (book: Book) -> Unit,
    onDeleteClick: (book: Book) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            })
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp))
    {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (book.imageUrl != null) {
                Box {
                    Image(
                        painter =  painterResource(id = R.drawable.book),
                        contentDescription = "Item Image",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 16.dp)
                    )
                }
            } else {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.book), // Replace `default_image` with your default image resource name
                        contentDescription = "Default Image",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 16.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    book.title, style = MaterialTheme.typography.bodyLarge
                )
                book.author?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        it, style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            IconButton(
                onClick = { onEditClick(book) },
                modifier = Modifier.size(40.dp)
            ) { // Increased icon size
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = { onDeleteClick(book) },
                modifier = Modifier.size(40.dp)
            ) { // Increased icon size
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

}


@Preview
@Composable
fun ItemListItemPreview() {
    BookListItem(book = Book(
        title = "Item Title",
        author = "Item Description",
        imageUrl = null // Replace `null` with your image URL
    ),
        onEditClick = {},
        onDeleteClick = {}
    )
}