package com.example.a8notes.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.a8notes.R
import com.example.a8notes.model.Note
import com.example.a8notes.util.EXTRA_NOTE
import com.example.a8notes.util.closeKeyboard
import com.example.a8notes.util.formatDate
import kotlinx.android.synthetic.main.add_edit_note.*
import java.util.*

class AddEditNote : AppCompatActivity() {

    var noteId = -1
    lateinit var view: View
    lateinit var editTextTitle: EditText
    lateinit var spinnerPriority: Spinner
    lateinit var editTextDate: EditText
    lateinit var imageViewEdit: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_note)

        initViews()
    }

    override fun onStart() {
        super.onStart()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun initViews() {
        view = view_priority
        editTextTitle = edit_text_title
        spinnerPriority = spinner_priority
        editTextDate = edit_text_date
        imageViewEdit = image_view_edit

        editTextDate.inputType = InputType.TYPE_NULL
        editTextDate.setOnClickListener {
            setupDatePicker()
            closeKeyboard(this)
        }

        imageViewEdit.setOnClickListener { saveNote() }

        intent.getParcelableExtra<Note>(EXTRA_NOTE)?.let {
            when (it.priority) {
                resources.getStringArray(R.array.priorities)[0] -> {
                    spinnerPriority.setSelection(0)
                    view_priority.setBackgroundColor(Color.GREEN)
                }
                resources.getStringArray(R.array.priorities)[1] -> {
                    spinnerPriority.setSelection(1)
                    view_priority.setBackgroundColor(Color.RED)
                }
                else -> {
                    spinnerPriority.setSelection(2)
                    view_priority.setBackgroundColor(Color.YELLOW)
                }
            }
            editTextTitle.setText(it.title)
            editTextDate.setText(it.date)
            noteId = it.id
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener {
                    datePicker, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar
                    .getInstance()
                    .apply {set(year, monthOfYear, dayOfMonth) }
                updateEditTextDate(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateEditTextDate(calendar:Calendar) {
        editTextDate.setText(formatDate(calendar))
    }

    private fun saveNote() {
        val note = Note(editTextTitle.text.toString(),
            spinnerPriority.selectedItem.toString(),
            editTextDate.text.toString())
            note.id = noteId
        val data = Intent().putExtra(EXTRA_NOTE, note)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}