 import androidx.compose.foundation.Image
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import org.nlc.candidatetask.data.Item
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ItemListItem(item: Item, onItemClick: (Item) -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item.imageUrl?.let {
                Box {
                    Image(
                        painter = rememberImagePainter(data = it),
                        contentDescription = "Item Image",
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.bodySmall
                )
                item.description?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun ItemListItemPreview() {
    ItemListItem(
        item = Item(
            title = "Item Title",
            description = "Item Description",
            imageUrl = "https://www.example.com/image.jpg"
        ),
        onItemClick = {},
        onEditClick = {},
        onDeleteClick = {}
    )
}