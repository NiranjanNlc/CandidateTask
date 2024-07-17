package org.nlc.candidatetask.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.data.FirebaseDataSource
import org.nlc.candidatetask.data.ItemDao
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val itemDao: ItemDao, private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun getAllItems(): List<Book> {
        return withContext(Dispatchers.IO) {
            val items = itemDao.getAllItems()
            if (items.isEmpty()) {
                val itemsFromFirebase = firebaseDataSource.getAllItems()
                for (item in itemsFromFirebase) {
                    itemDao.insert(item)
                }
                itemsFromFirebase
            } else {
                items
            }
        }
    }

    suspend fun saveItem(book: Book) {
        withContext(Dispatchers.IO) {
            // Save item to Firebase and get the unique id
            val firebaseId = firebaseDataSource.addItem(book)
            if (firebaseId != null) {
                // Set the Firebase id to the book and save it to local database
                book.bookId = firebaseId
                itemDao.insert(book)
            }
        }
    }

    suspend fun updateItem(book: Book) {
        withContext(Dispatchers.IO) {
            // Update item in both Firebase and local database
            if (firebaseDataSource.updateItem(book)) {
                itemDao.update(book)
            }
        }
    }

    suspend fun deleteItem(book: Book) {
        withContext(Dispatchers.IO) {
            // Delete item from both Firebase and local database
            if (firebaseDataSource.deleteItem(book.bookId)) {
                itemDao.delete(book)
            }
        }
    }
}
