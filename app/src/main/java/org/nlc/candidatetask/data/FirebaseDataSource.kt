package org.nlc.candidatetask.data

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    database: FirebaseDatabase
) {

    private val database = database.reference.child("items")

    suspend fun addItem(book: Book): String? = withContext(Dispatchers.IO) {
        try {
            val key = database.push().key // Generate a new unique key
            key?.let {
                book.bookId = it // Set the generated key as the id of the book
                database.child(it).setValue(book).await()
                it // Return the key as the result of the function
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllItems(): List<Book> = withContext(Dispatchers.IO) {
        val snapshot = database.get().await()
        snapshot.children.mapNotNull { it.getValue(Book::class.java) }
    }

    suspend fun updateItem(book: Book): Boolean = withContext(Dispatchers.IO) {
        try {
            book.bookId?.let {
                database.child(it).setValue(book).await()
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteItem(itemId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            database.child(itemId).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
