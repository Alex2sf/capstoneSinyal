package com.example.sinyal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sinyal.R

class NotificationFragment : Fragment() {

    private var currentQuestionIndex = 0

    // Add your Question data class here
    private data class Question(
        val imageResourceId: Int,
        val correctAnswer: String,
        val options: List<String>
    )

    // Add your questions list here
    private val questions = listOf(
        Question(
            R.drawable.satu,
            "Senang",
            listOf("Senang", "nangis", "terhura", "HEHEHEHE")
        ),
        Question(
            R.drawable.dua,
            "nangis",
            listOf("Senang", "nangis", "terhura", "HEHEHEHE")
        ),
        Question(
            R.drawable.tiga,
            "terhura",
            listOf("Senang", "nangis", "terhura", "HEHEHEHE")
        ),
        Question(
            R.drawable.empat,
            "HEHEHEHE",
            listOf("Senang", "nangis", "terhura", "HEHEHEHE")
        ),
        Question(
            R.drawable.lima,
            "Senang",
            listOf("Senang", "nangis", "terhura", "HEHEHEHE")
        )
        // Add more questions here
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        // Initialize UI elements and set listeners
        val nextButton = view.findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            checkAnswer()
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                loadQuestion()
            } else {
                showQuizCompletedMessage()
            }
        }

        // Load the initial question
        loadQuestion()

        return view
    }

    private fun loadQuestion() {
        val currentQuestion = questions[currentQuestionIndex]

        val imageView = view?.findViewById<ImageView>(R.id.imageView)
        imageView?.setImageResource(currentQuestion.imageResourceId)

        val radioGroup = view?.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup?.removeAllViews()

        currentQuestion.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(requireContext())
            radioButton.text = option
            radioButton.id = index
            radioGroup?.addView(radioButton)
        }
    }

    private fun checkAnswer() {
        val selectedRadioButtonId = view?.findViewById<RadioGroup>(R.id.radioGroup)?.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = view?.findViewById<RadioButton>(selectedRadioButtonId!!)
            val userAnswer = selectedRadioButton?.text.toString()
            val correctAnswer = questions[currentQuestionIndex].correctAnswer

            if (userAnswer != correctAnswer) {
                Toast.makeText(
                    requireContext(),
                    "Jawaban Salah. Jawaban yang benar: $correctAnswer",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showQuizCompletedMessage() {
        Toast.makeText(requireContext(), "Kuis Selesai", Toast.LENGTH_SHORT).show()
    }
}
