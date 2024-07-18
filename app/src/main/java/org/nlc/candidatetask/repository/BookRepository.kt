package org.nlc.candidatetask.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.data.FirebaseDataSource
import org.nlc.candidatetask.data.ItemDao
import org.nlc.candidatetask.util.checkBookAvailableFromBookId
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val firebaseDataSource: FirebaseDataSource
) {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books

    suspend fun getAllItems(networkStatus: Boolean?): List<Book> {
        return withContext(Dispatchers.IO) {
                _books.value = itemDao.getNonDeletedItemBy()
                _books.value ?: emptyList()
        }
    }

    suspend fun saveItem(book: Book, networkStatus: Boolean?) {
        withContext(Dispatchers.IO) {
            if (networkStatus == true) {
                // Save item to Firebase and get the unique id
                val firebaseId = firebaseDataSource.addItem(book.copy(isSynchronized = true))
                if (firebaseId != null) {
                    // Set the Firebase id to the book and save it to local database
                    book.bookId = firebaseId
                    book.isSynchronized = true
                    itemDao.insert(book)
                }
            } else {
                itemDao.insert(book)
            }
            Log.d("BookRepository", "saveItem: ${book}")
            Log.d("BookRepository", "saveItem: ${itemDao.getAllItems()}")
        }
    }

    suspend fun updateItem(book: Book, networkStatus: Boolean?) {

        withContext(Dispatchers.IO) {
            book.lastModified = System.currentTimeMillis()
            if (networkStatus == true) {
                book.lastModified = System.currentTimeMillis()
                // Update item in Firebase
                firebaseDataSource.updateItem(book)
                itemDao.update(book)
            } else {
                itemDao.update(book)
            }
        }
    }

    suspend fun deleteItem(book: Book, networkStatus: Boolean?) {
        withContext(Dispatchers.IO) {
            Log.d("BookRepository", "deleteItem: ${book.bookId}")
            if (networkStatus == true) {
                firebaseDataSource.deleteItem(book.bookId)
                itemDao.delete(book)
            } else {
                // Mark item as deleted in room database if there is no network
                itemDao.update(book.copy(isDeleted = true))
                Log.d("BookRepository", "deleteItem: ${itemDao.getItemById(book.id)}")
            }
        }
    }

    suspend fun refesh() {
        Log.d("Sync", "refresh: ")
        val firebaseRecords = firebaseDataSource.getAllItems()
        val localRecords = itemDao.getAllItems()
        updateNewRecordsofFireBaseInLocalDatabase(firebaseRecords, localRecords)
        deleteRecordsNotInFirebase(localRecords, firebaseRecords)
    }

    suspend fun synchronize() {
        Log.d("Sync", "synchronize: ")
        val firebaseRecords = firebaseDataSource.getAllItems()
        val localRecords = itemDao.getAllItems()
        synchronizeUpdatedAndDeletedItmOFLocalDatabase(localRecords, firebaseRecords)
        // after the local databse is synchronmized ,
        // delete all the records from the local database that are not in the firebase database
        // update the new records of firebase to the local database
        /*

         */
    }

    private suspend fun synchronizeUpdatedAndDeletedItmOFLocalDatabase(
        localRecords: List<Book>,
        firebaseRecords: List<Book>
    ) = runBlocking {

        Log.d("Sync", "$firebaseRecords ")
        localRecords.forEach { localBook ->
            //   find whether the loocal book id exist in firebase records or not
            val firebaseBook = checkBookAvailableFromBookId(firebaseRecords, localBook.bookId)
            println("Sync $firebaseBook")
            Log.d("Sync", "$localBook same on both side: $firebaseBook")
            if (firebaseBook != null) {
                // Record exists in Firebase, resolve conflicts
                resolveConflictBetweenLocalAndFirebase(localBook, firebaseBook)
            }
            if (localBook.isSynchronized == false) {
                Log.d("Sync", "notsynchronize: $localBook")
                synchronizeUnsavedLocalBookToFireBase(localBook)
            }
            if (localBook.isDeleted == true) {
                Log.d("Sync", "is delete : $firebaseBook")
                Log.d("Sync", "is delete: $localBook")
                // Record is deleted locally, delete it from Firebase
                if (checkBookAvailableFromBookId(firebaseRecords, localBook.bookId) != null) {
                    //delete from both the firebase and local database
                    firebaseDataSource.deleteItem(localBook.bookId)
                    itemDao.delete(localBook)
                } else {
                    //delete from local database only since it was not synchronized
                    itemDao.delete(localBook)
                }
            }
        }
    }

    private suspend fun synchronizeUnsavedLocalBookToFireBase(
        localBook: Book
    ) {
        Log.d("Sync", "not  synchronize: $localBook")
        // Record does not exist in Firebase, upload it
        val firebaseId = firebaseDataSource.addItem(localBook.copy(isSynchronized = true))
        if (firebaseId != null) {
            localBook.bookId = firebaseId
            localBook.isSynchronized = true
            itemDao.update(localBook)
            Log.d("Sync", "not  synchronize: ${itemDao.getAllItems()}")
        }
    }

    private suspend fun resolveConflictBetweenLocalAndFirebase(
        localBook: Book,
        firebaseBook: Book
    ) {
        if (localBook.lastModified > firebaseBook.lastModified) {
            Log.d("Sync", "last modified different : ${localBook.lastModified}")
            Log.d("Sync", "last modified different : $firebaseBook")
            Log.d("Sync", "last modified different : $localBook")
            // Local record is newer, update Firebase
            firebaseDataSource.updateItem(localBook)
        } else if (localBook.lastModified < firebaseBook.lastModified) {
            Log.d("Sync", "synchronize for firebase book  : $firebaseBook")
            Log.d("Sync", "synchronize for firebase book : $localBook")
            // Firebase record is newer, so  update local database
            firebaseBook.lastModified = localBook.lastModified
            firebaseBook.isSynchronized = true
            itemDao.update(firebaseBook)
        }
    }


    private suspend fun updateNewRecordsofFireBaseInLocalDatabase(
        firebaseRecords: List<Book>,
        localRecords: List<Book>
    ) = runBlocking {
        Log.d("Sync", "updateNewRecordsofFireBaseInLocalDatabase: ")
        firebaseRecords.forEach { firebaseBook ->
            if (localRecords.find { it.bookId == firebaseBook.bookId } == null) {
                itemDao.insert(firebaseBook)
            }
        }
    }

    private suspend fun deleteRecordsNotInFirebase(
        localRecords: List<Book>,
        firebaseRecords: List<Book>
    ) = runBlocking {
        Log.d("Sync", "deleteRecordsNotInFirebase: ")
        localRecords.forEach { localBook ->
            if (firebaseRecords.find { it.bookId == localBook.bookId } == null) {
                itemDao.delete(localBook)
            }
        }
    }

}
