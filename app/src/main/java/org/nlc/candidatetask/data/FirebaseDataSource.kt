package org.nlc.candidatetask.data

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    database: FirebaseDatabase ) {

    private val database = database.reference.child("items")

    suspend fun addItem(book: Book): Boolean = withContext(Dispatchers.IO) {
        try {
            database.push().setValue(book).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllItems(): List<Book> = withContext(Dispatchers.IO) {
        val snapshot = database.get().await()
        snapshot.children.mapNotNull { it.getValue(Book::class.java) }
    }

    suspend fun updateItem(book: Book): Boolean = withContext(Dispatchers.IO) {
        try {
            book.id?.let {
                database.child(it.toString()).setValue(book).await()
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



