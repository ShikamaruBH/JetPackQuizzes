package com.shikamarubh.jetpackquizzes.repository

import com.shikamarubh.jetpackquizzes.data.DataOrException
import com.shikamarubh.jetpackquizzes.model.Question
import com.shikamarubh.jetpackquizzes.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException =
        DataOrException<Question, Boolean, Exception>()

    suspend fun getAllQuestions() : DataOrException<Question, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
            }
        } catch (exception: Exception) {
            dataOrException.e = exception
        }
        return dataOrException
    }
}