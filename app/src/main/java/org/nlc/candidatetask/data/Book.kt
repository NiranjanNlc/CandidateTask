package org.nlc.candidatetask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var bookId: String= " ",
    val title: String= " ",
    val author: String=" ",
    val imageUrl: String?=" "
)
