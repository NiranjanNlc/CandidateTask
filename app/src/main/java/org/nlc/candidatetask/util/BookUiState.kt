package org.nlc.candidatetask.util

import org.nlc.candidatetask.data.Book


sealed class BookUiState {
    object Loading : BookUiState()
    data class Success(val data: List<Book>) : BookUiState()
    data class Empty(val message: String) : BookUiState()
    data class Error(val message: String) : BookUiState()
}