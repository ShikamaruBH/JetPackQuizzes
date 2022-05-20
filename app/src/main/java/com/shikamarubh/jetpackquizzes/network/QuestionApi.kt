package com.shikamarubh.jetpackquizzes.network

import com.shikamarubh.jetpackquizzes.model.Question
import retrofit2.http.GET

interface QuestionApi {
    @GET("itmmckernan/triviaJSON/master/world.json")
    suspend fun getAllQuestions(): Question
}