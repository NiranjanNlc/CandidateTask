package org.nlc.candidatetask.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseItemDataSource(
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("items")
) {
    fun getAllItems(callback: (List<Item>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Item>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    item?.let { items.add(it) }
                }
                callback(items)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun saveItem(item: Item, callback: () -> Unit) {
        database.child(item.id.toString()).setValue(item)
            .addOnSuccessListener { callback() }
            .addOnFailureListener { // Handle error }
            }
    }
        fun deleteItem(item: Item, callback: () -> Unit) {
            database.child(item.id.toString()).removeValue()
                .addOnSuccessListener { callback() }
                .addOnFailureListener { // Handle error }
                }
        }
    }
