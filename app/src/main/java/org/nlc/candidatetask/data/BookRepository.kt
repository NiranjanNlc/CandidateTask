package org.nlc.candidatetask.data

import javax.inject.Inject

class BookRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getAllItems(): List<Book> {
        return itemDao.getAllItems()
    }

    suspend fun saveItem(book: Book) {
        itemDao.insert(book)
        firebaseDataSource.addItem(book)
    }

    suspend fun updateItem(book: Book) {
        itemDao.update(book)
        firebaseDataSource.updateItem(book)
    }

    suspend fun deleteItem(book: Book) {
        itemDao.delete(book)
        firebaseDataSource.deleteItem(book.id.toString())
    }
}