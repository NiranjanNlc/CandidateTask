package org.nlc.candidatetask.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.data.FirebaseDataSource
import org.nlc.candidatetask.data.ItemDao
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getAllItems(networkStatus: Boolean?): List<Book> {
        return withContext(Dispatchers.IO) {
            if (networkStatus == true) {
                firebaseDataSource.getAllItems()
            } else {
                itemDao.getNonDeletedItemBy()
            }
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
            if (networkStatus == true) {
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

    suspend fun synchronize() {
        Log.d("BookRepository", "synchronize: ")
        val firebaseRecords = firebaseDataSource.getAllItems()
        val localRecords = itemDao.getAllItems()
        synchronizeUpdatedAndDeletedItmOFLocalDatabase(localRecords, firebaseRecords)
        // after the local databse is synchronmized ,
        // delete all the records from the local database that are not in the firebase database
        // update the new records of firebase to the local database
        /*
        it is assumed that the user have complete control and
        database is not shared with any other user
        so records will not be updated and deleted excepted by the user
         */
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

    private suspend fun synchronizeUpdatedAndDeletedItmOFLocalDatabase(
        localRecords: List<Book>,
        firebaseRecords: List<Book>
    ) = runBlocking {
        localRecords.forEach { localBook ->

            //  Compare local records with Firebase records
            val firebaseBook = firebaseRecords.find { it.bookId == localBook.bookId }
            Log.d("Sync", "same on both side: $firebaseBook")

            if (firebaseBook != null) {
                // Record exists in Firebase, resolve conflicts
                if (localBook.lastModified > firebaseBook.lastModified) {
                    Log.d("Sync", "last modified different : ${localBook.lastModified}")
                    Log.d("Sync", "last modified different : $firebaseBook")
                    Log.d("Sync", "last modified different : $localBook")
                    // Local record is newer, update Firebase
                    firebaseDataSource.updateItem(localBook)
                } else {
                    Log.d("Sync", "synchronize for firebase book  : $firebaseBook")
                    Log.d("Sync", "synchronize for firebase book : $localBook")
                    // Firebase record is newer, so  update local database
                    itemDao.update(firebaseBook)
                }
            }
            if (localBook.isSynchronized == false) {
                Log.d("Sync", "not synchronize: $firebaseBook")
                Log.d("Sync", "not  synchronize: $localBook")
                // Record does not exist in Firebase, upload it
                val firebaseId = firebaseDataSource.addItem(localBook)
                if (firebaseId != null) {
                    localBook.bookId = firebaseId
                    localBook.isSynchronized = true
                    itemDao.update(localBook)
                }
            }
            if (localBook.isDeleted == true) {
                Log.d("Sync", "is delete : $firebaseBook")
                Log.d("Sync", "is delete: $localBook")
                // Record is deleted locally, delete it from Firebase
                firebaseDataSource.deleteItem(localBook.bookId)
                itemDao.delete(localBook)
            }
        }
    }
}
