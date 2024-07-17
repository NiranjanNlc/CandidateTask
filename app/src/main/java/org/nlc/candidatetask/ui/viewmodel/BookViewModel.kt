package org.nlc.candidatetask.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.repository.BookRepository
import org.nlc.candidatetask.util.BookUiState
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _bookUiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val bookUiState: StateFlow<BookUiState> = _bookUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()


    init {
        getBooks()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            getBooks()
            _isRefreshing.emit(false)
        }
    }

    private fun getBooks() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Loading
            delay(2000)
            val books =  repository.getAllItems()
            _bookUiState.value = if (books.isEmpty()) {
                BookUiState.Empty("No books found")
            } else {
                BookUiState.Success(books)
            }
        }
        Log.i("BookViewModel", "getBooks: ${_bookUiState.value} ")
    }
    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.saveItem(book)
            getBooks()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteItem(book)
            getBooks()
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateItem(book)
            getBooks()
        }
    }

    private fun fetchSampleListOfBook(): List<Book> {
        return listOf(
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
            ), Book(
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
    }
}