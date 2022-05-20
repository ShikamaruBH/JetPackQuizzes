package com.shikamarubh.jetpackquizzes.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shikamarubh.jetpackquizzes.data.DataOrException
import com.shikamarubh.jetpackquizzes.model.Question
import com.shikamarubh.jetpackquizzes.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
    @Inject constructor(private val repository: QuestionRepository) : ViewModel() {
    val data : MutableState<DataOrException<Question, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
            }
        }
    }
    fun getTotalQuestionCount() : Int {
        return data.value.data?.toMutableList()?.size!!
    }
}