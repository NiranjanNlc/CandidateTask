package org.nlc.candidatetask.ui.view

import ItemListItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: BookViewModel = hiltViewModel(),
    books : List<Book> = emptyList(),
    onItemClick: (Book) -> Unit={},
    onAddItem: () -> Unit={}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item List") },
                actions = {
                    IconButton(onClick = onAddItem) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Item")
                    }
                }
            )
        }
    ) { paddingValues ->
        ListOfBook(paddingValues, books, onItemClick)
    }
}

@Composable
private fun ListOfBook(
    paddingValues: PaddingValues,
    books: List<Book>,
    onItemClick: (Book) -> Unit
) {
    LazyColumn(
        contentPadding = paddingValues
    ) {
        items(books) { item ->
            ItemListItem(
                book = item,
                onItemClick = onItemClick,
                onEditClick = {}
            )
        }
    }
}

@Preview
@Composable
fun ItemListScreenPreview() {
    ItemListScreen(
        viewModel = hiltViewModel(),
        books = listOf(
            Book(
                id = 1,
                title = "Item 1",
                description = "Description 1",
                imageUrl = "https://example.com/image1.jpg"
            ),
            Book(
                id = 2,
                title = "Item 2",
                description = "Description 2",
                imageUrl = "https://example.com/image2.jpg"
            ),
            Book(
                id = 3,
                title = "Item 3",
                description = "Description 3",
                imageUrl = "https://example.com/image3.jpg"
            ),
            Book(
                id = 4,
                title = "Item 4",
                description = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            ),Book(
                id = 5,
                title = "Item r",
                description = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            ),
            Book(
                id = 67,
                title = "Itreyt",
                description = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            )
        ),
        onItemClick = {},
        onAddItem = {}
    )
}
