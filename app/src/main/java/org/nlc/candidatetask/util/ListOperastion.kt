package org.nlc.candidatetask.util

import org.nlc.candidatetask.data.Book


fun checkBookAvailableFromBookId(
    bookRecords: List<Book>,
    searchId: String
): Book? {
    val book = bookRecords.firstOrNull { it.bookId == searchId }
    return book
}