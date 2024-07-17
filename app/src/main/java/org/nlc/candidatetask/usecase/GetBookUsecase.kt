package org.nlc.candidatetask.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.nlc.candidatetask.data.Book
import org.nlc.candidatetask.repository.BookRepository
import javax.inject.Inject


class GetBookUsecase @Inject constructor (configuration: Configuration,
                                          private val repository: BookRepository,
) : UseCase<GetBookUsecase.Request, GetBookUsecase.Response>(configuration) {

    data class Request(val query:String) : UseCase.Request
    data class Response(val bookLisst: List<Book>) : UseCase.Response

    override suspend fun process(request: Request): Flow<Response> =
        flowOf(
            Response(repository.getAllItems())
        )
}