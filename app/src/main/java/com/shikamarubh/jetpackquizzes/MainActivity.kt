package com.shikamarubh.jetpackquizzes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shikamarubh.jetpackquizzes.model.QuestionItem
import com.shikamarubh.jetpackquizzes.ui.theme.JetPackQuizzesTheme
import com.shikamarubh.jetpackquizzes.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackQuizzesTheme {
                val count = remember {
                    mutableStateOf(0)
                }
                val choiceSelected = remember {
                    mutableStateOf("")
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.main)
                ) {
                    val questionViewModel = viewModel<QuestionViewModel>()
                    body(
                        count,
                        questionViewModel,
                        choiceSelected)
                }
            }
        }
    }
}


@Composable
fun body(count: MutableState<Int>, questionViewModel: QuestionViewModel, choiceSelected: MutableState<String>) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (questionViewModel.data.value.loading == true) {
            loadingScreen()
        } else {
            counter(count, questionViewModel.getTotalQuestionCount())
            drawDashLine()
            question(questionViewModel.data.value.data!![count.value])
            choices(count, questionViewModel.data.value.data!![count.value], choiceSelected)
        }
    }
}

@Composable
fun loadingScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        Text(text = "Loading . . .",
            fontSize = 35.sp,
            style = MaterialTheme.typography.h5,
            color = Color.White,
            )
    }
}

@Composable
fun counter(count: MutableState<Int>, numberOfQuestion: Int) {
    Row(
        modifier = Modifier
            .padding(start = 30.dp, top = 30.dp, bottom = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Question ${count.value + 1}/",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.text),
        )
        Text(text = "$numberOfQuestion",
            color = colorResource(R.color.text),)
    }

}
@Composable
fun drawDashLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 9f), 0f)
    Canvas(
        Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        drawLine(
            color = Color(0xFFaeb7c8),
            strokeWidth = 3f,
            start = Offset(20f, 0f),
            end = Offset(size.width - 20, 0f),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun question(questionItem: QuestionItem) {
    Text(
        text = questionItem.question,
        modifier = Modifier
            .padding(start = 12.dp, top = 10.dp)
            .fillMaxWidth()
            .height(200.dp),
        color = Color.White,
        fontWeight = FontWeight.Bold)
}

@Composable
fun choices(count: MutableState<Int>, questionItem: QuestionItem,choiceSelected: MutableState<String>) {
    Column(modifier = Modifier
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        for (choice in questionItem.choices) {
            if (choiceSelected.value.isNotEmpty() && choice == questionItem.answer)
                outlinedRadioButton(choice, choiceSelected, Color.Green)
            else if (choiceSelected.value.isNotEmpty() && choice != questionItem.answer)
                outlinedRadioButton(choice, choiceSelected, Color.Red)
            else
                outlinedRadioButton(choice, choiceSelected, Color.White)
        }

        Button(
            onClick = {
                count.value++
                choiceSelected.value = "" },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.button))
        ) {
            Text(
                text = "Next",
                color = Color.White,
                fontSize = 19.sp)
        }
    }
}

@Composable
fun outlinedRadioButton(content: String, choiceSelected: MutableState<String>, color: Color) {
    OutlinedButton(onClick = {
        if (choiceSelected.value.isEmpty())
            choiceSelected.value = content },
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border= BorderStroke(3.dp, Color(0xff234c60)),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.main))) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)) {
            RadioButton(selected = content == choiceSelected.value, onClick = {
                if (choiceSelected.value.isEmpty())
                    choiceSelected.value = content  })
            Text(text = content, color = color)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetPackQuizzesTheme {
    }
}