package org.nlc.candidatetask.ui.viewmodel

import android.util.Log
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
import androidx.lifecycle.ViewModel
import org.nlc.candidatetask.sync.NetworkStatusHelper


@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository,
    private val networkStatusHelper: NetworkStatusHelper
) : ViewModel() {

    val networkStatus = networkStatusHelper.networkStatus

    val bookList: StateFlow<List<Book>> = repository.books

    private val _bookUiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val bookUiState: StateFlow<BookUiState> = _bookUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()


    init {
        getBooks()
        networkStatusHelper.startNetworkCallback()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
             repository.getAllItems(networkStatus.value)
            if (bookList.value.isEmpty()) {
                _bookUiState.value = BookUiState.Empty("No books found")
            }
            else {
                _bookUiState.value = BookUiState.Success(bookList.value)
            }
            delay(500)
            _isRefreshing.emit(false)
        }
    }

    private fun getBooks() {
        viewModelScope.launch {
            _bookUiState.value = BookUiState.Loading
            delay(2000)
            val books =  repository.getAllItems(networkStatus.value)
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
            repository.saveItem(book,networkStatus.value)
            refresh()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteItem(book,networkStatus.value)
            refresh()
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateItem(book,networkStatus.value)
        }
        refresh()
    }


    override fun onCleared() {
        super.onCleared()
        networkStatusHelper.stopNetworkCallback()
    }
}