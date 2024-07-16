package org.nlc.candidatetask.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.nlc.candidatetask.data.BookRepository
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {

     init {

     }
}