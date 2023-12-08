package com.example.sinyal

// NotesActivity.kt
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotesActivity : AppCompatActivity() {
    private lateinit var editTextNote: EditText
    private lateinit var textViewDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        editTextNote = findViewById(R.id.editTextNote)
        textViewDisplay = findViewById(R.id.textViewDisplay)
    }


}
