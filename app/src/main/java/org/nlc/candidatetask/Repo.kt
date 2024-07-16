package org.nlc.candidatetask

import org.nlc.candidatetask.data.FirebaseItemDataSource
import org.nlc.candidatetask.data.Item
import org.nlc.candidatetask.data.ItemDao

class ItemRepository(
    private val itemDao: ItemDao,
    private val firebaseItemDataSource: FirebaseItemDataSource
) {
    suspend fun getAllItems(): List<Item> {
        return itemDao.getAllItems()
    }

    suspend fun saveItem(item: Item) {
        itemDao.insert(item)
        firebaseItemDataSource.saveItem(item) {}
    }

    suspend fun updateItem(item: Item) {
        itemDao.update(item)
        firebaseItemDataSource.saveItem(item) {}
    }

    suspend fun deleteItem(item: Item) {
        itemDao.delete(item)
        firebaseItemDataSource.deleteItem(item) {}
    }
}