package org.nlc.candidatetask.data


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.api.mockito.PowerMockito.`when`

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FirebaseBookDataSourceTest {

    @Mock
    private lateinit var mockDatabase: FirebaseDatabase

    @Mock
    private lateinit var mockDatabaseReference: DatabaseReference

    private lateinit var firebaseDataSource: FirebaseDataSource

    @Before
    fun setUp() {
        `when`(mockDatabase.reference).thenReturn(mockDatabaseReference)
        `when`(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference)
        firebaseDataSource = FirebaseDataSource(mockDatabase)
    }

    @Test
    fun `addItem should call setValue on database reference`() = runBlockingTest {
        val book = Book(1, "title", "description", "imageUrl")
        firebaseDataSource.addItem(book)
        verify(mockDatabaseReference).push()
        verify(mockDatabaseReference).setValue(book)
    }

    @Test
    fun `deleteItem should call removeValue on database reference`() = runBlockingTest {
        val itemId = "1"
        firebaseDataSource.deleteItem(itemId)
        verify(mockDatabaseReference).child(itemId)
        verify(mockDatabaseReference).removeValue()
    }
}