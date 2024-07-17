package org.nlc.candidatetask.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.remote.ConnectivityMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.nlc.candidatetask.repository.BookRepository
import org.nlc.candidatetask.data.AppDatabase
import org.nlc.candidatetask.data.FirebaseDataSource
import org.nlc.candidatetask.data.ItemDao
import org.nlc.candidatetask.sync.NetworkStatusHelper
import org.nlc.candidatetask.ui.viewmodel.BookViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideItemDao(appDatabase: AppDatabase): ItemDao = appDatabase.itemDao()

    //provides for database
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    //provides for firebase data store
    @Singleton
    @Provides
    fun provideFirebaseItemDataSource(database: FirebaseDatabase): FirebaseDataSource =
        FirebaseDataSource(database)

    @Singleton
    @Provides
    fun provideItemRepository(itemDao: ItemDao, firebaseDataSource: FirebaseDataSource)
            : BookRepository = BookRepository(itemDao, firebaseDataSource)

    //provides for network status
    @Singleton
    @Provides
    fun provideNetworkStatus(@ApplicationContext context: Context): NetworkStatusHelper =
        NetworkStatusHelper(context)

    @Singleton
    @Provides
    fun provideBookViewModel(repository: BookRepository,networkStatusHelper: NetworkStatusHelper): BookViewModel = BookViewModel(repository, networkStatusHelper)
}
