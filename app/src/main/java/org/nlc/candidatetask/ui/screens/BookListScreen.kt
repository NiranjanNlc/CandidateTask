package org.nlc.candidatetask.ui.screens

import BookListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.nlc.candidatetask.R
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.util.BookUiState
import org.nlc.candidatetask.ui.viewmodel.BookViewModel
import org.nlc.candidatetask.util.imageUrls
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    viewModel: BookViewModel = hiltViewModel(),
) {
    val bookUiState by viewModel.bookUiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var showAddDiaolog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BookTopBar(
                topBarTitle = R.string.app_name,
                onAddItem = {
                    showAddDiaolog = true
                }
            )
        }
    ) { paddingValues ->

        if (showAddDiaolog) {
            BookInputDialog(
                operation = "Add",
                onDismiss = { showAddDiaolog = false },
                onSave = { title, author ->
                    // Handle the saved book details here
                    println("Book Title: $title, Book Author: $author")
                    viewModel.addBook(
                        Book(
                            id = Random.nextInt(1,1000),
                            title = title,
                            author = author,
                            imageUrl = imageUrls.random()
                        )
                    )
                    showAddDiaolog = false
                }
            )
        }
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (bookUiState) {
                is BookUiState.Loading -> {
                    LoadingScreen()
                }
                is BookUiState.Success -> {
                    val books = (bookUiState as BookUiState.Success).data
                    ListOfBook(
                        viewModel = viewModel,
                        books = books
                    )
                }
                is BookUiState.Empty -> {
                    EmptyContentScreen(
                        modifier = Modifier,
                        title =(bookUiState as BookUiState.Empty).message,
                        subTitle = "Please add some data and refresh  again",
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconImageVector = Icons.Rounded.Search
                    )
                }
                is BookUiState.Error -> {
                    ErrorScreenContent(
                        modifier = Modifier,
                        title = (bookUiState as BookUiState.Error).message,
                        subTitle = "Please check your connection or try again",
                        onClickRetry = { viewModel.refresh()}
                    )
                 }
            }
        }
    }
}

@Composable
private fun ListOfBook(
    viewModel: BookViewModel = hiltViewModel(),
    books: List<Book> = emptyList()
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedBook: Book? by remember { mutableStateOf(null) }

    LazyColumn(
        contentPadding =  PaddingValues(2.dp)
    ) {
        items(books) { item ->
            BookListItem(
                book = item,
                onEditClick = {
                    selectedBook = it
                    showEditDialog = true
                },
                onDeleteClick = {
                    selectedBook = it
                    showDeleteDialog = true
                }
            )
        }
    }
    if (showEditDialog) {
        BookInputDialog(
            operation = "Edit ",
            book = selectedBook,
            onDismiss = { showEditDialog = false },
            onSave = { title, author ->
                // Handle the saved book details here
                println("Book Title: $title, Book Author: $author")
                selectedBook = selectedBook?.copy(title = title, author = author)
                selectedBook?.let { viewModel.updateBook(it) }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog = false
                    selectedBook?.let { viewModel.deleteBook(it) }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            })
    }
}

@Preview
@Composable
fun ItemListScreenPreview() {
    ListOfBook(
        books = listOf(
            Book(
                id = 1,
                title = "Item 1",
                author = "Description 1",
                imageUrl = "https://example.com/image1.jpg"
            ),
            Book(
                id = 2,
                title = "Item 2",
                author = "Description 2",
                imageUrl = "https://example.com/image2.jpg"
            ),
            Book(
                id = 3,
                title = "Item 3",
                author = "Description 3",
                imageUrl = "https://example.com/image3.jpg"
            ),
            Book(
                id = 4,
                title = "Item 4",
                author = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            ),Book(
                id = 5,
                title = "Item r",
                author = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            ),
            Book(
                id = 67,
                title = "Itreyt",
                author = "Description 4",
                imageUrl = "https://example.com/image4.jpg"
            )
        )
    )
}
