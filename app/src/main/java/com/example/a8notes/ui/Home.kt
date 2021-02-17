package com.example.a8notes.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a8notes.R
import com.example.a8notes.adapter.NoteAdapter
import com.example.a8notes.db.NoteRepository
import com.example.a8notes.model.Note
import com.example.a8notes.util.EXTRA_NOTE
import com.example.a8notes.util.closeKeyboard
import com.example.a8notes.util.formatDate
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home.spinner_priority
import java.util.*

const val REQUEST_CODE_EDIT = 2

class Notes : AppCompatActivity() {

    lateinit var editTextAddNote: EditText
    lateinit var spinnerPriority: Spinner
    lateinit var imageViewDate: ImageView

    lateinit var noteAdapter: NoteAdapter
    lateinit var noteRepository: NoteRepository
    var selectedDate: String = formatDate(Calendar.getInstance())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        noteRepository = NoteRepository(this)

        setViews()
        buildNotesRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            noteRepository.updateNote(data?.getParcelableExtra(EXTRA_NOTE)!!)
            noteAdapter.swapList(noteRepository.getAllNotes())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_note -> {
                saveNote()
                resetViews()
                true
            }
            R.id.action_delete_all -> {
                noteRepository.deleteAllNotes()
                noteAdapter.swapList(noteRepository.getAllNotes())
                true
            }
            else -> false
        }
    }

    private fun setViews() {
        editTextAddNote = edit_text_add_note
        spinnerPriority = spinner_priority
        imageViewDate = image_view_date
        imageViewDate.setOnClickListener {setupDatePicker()}
        /*editTextAddNote.setOnEditorActionListener{
            _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard(this)
                    true
                }
            false

        }*/
        /*val editTextAddNote = edit_text_add_note
        editTextAddNote.setOnKeyListener{_,keyCode, event ->
            return@setOnKeyListener if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                saveNote()
                Log.e("mmmmm", true.toString())
                true
            } else {
                Log.e("mmmmm", false.toString())
                false
            }

        }

         */
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener {
                    datePicker, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar
                    .getInstance()
                    .apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                selectedDate = formatDate(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun buildNotesRecyclerView() {
        val notes=noteRepository.getAllNotes()
        noteAdapter = NoteAdapter(notes)
        noteAdapter.onItemClickListener = object: NoteAdapter.OnItemClickListener{
            override fun onItemClick(note: Note) {
                startActivityForResult(Intent(this@Notes, AddEditNote::class.java)
                    .putExtra(EXTRA_NOTE, note), REQUEST_CODE_EDIT)
            }

        }

        recycler_view_notes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@Notes)
            setHasFixedSize(true)


            ItemTouchHelper(object :
                SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    noteRepository.deleteNote(noteAdapter.notes?.get(viewHolder.adapterPosition)!!)
                    noteAdapter.swapList(noteRepository.getAllNotes())
                }

            }).attachToRecyclerView(this)
        }
    }

    private fun resetViews(){
        editTextAddNote.text.clear()
        editTextAddNote.clearFocus()
        spinnerPriority.setSelection(0)
        closeKeyboard(this)
    }

    private fun saveNote() {
        val note = Note(editTextAddNote.text.toString(),
            spinner_priority.selectedItem.toString(),
            selectedDate)
        noteRepository.addNote(note)
        noteAdapter.swapList(noteRepository.getAllNotes())
    }
}