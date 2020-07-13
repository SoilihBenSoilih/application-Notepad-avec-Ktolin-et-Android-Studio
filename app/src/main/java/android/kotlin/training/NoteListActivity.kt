package android.kotlin.training

import android.app.Activity
import android.content.Intent
import android.kotlin.training.utils.deleteNote
import android.kotlin.training.utils.loadNotes
import android.kotlin.training.utils.persistNote
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class NoteListActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notes: MutableList<Note>
    lateinit var adapter: ListAdapter
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        val toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)

        //findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this)

        notes = loadNotes(this)

        adapter = ListAdapter(notes, this)

        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =  adapter

        coordinatorLayout = findViewById(R.id.coodinator_layout) as CoordinatorLayout

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                createNewNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View) {
        if(view.tag != null){
            showNoteDetails(view.tag as Int)
        }
       /* else {
            when(view.id){
               R.id.create_note_fab -> createNewNote()
            }
        }*/
    }


     fun createNewNote() {
         showNoteDetails(-1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode != Activity.RESULT_OK || data == null){
            return
        }
        when(requestCode){
            NoteDetailsActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun processEditNoteResult(data: Intent) {
            val noteIndex = data.getIntExtra(NoteDetailsActivity.EXTRA_NOTE_INDEX, -1)

            when(data.action){
                NoteDetailsActivity.ACTION_SAVE_NOTE -> {
                    val note = data.getParcelableExtra<Note>(NoteDetailsActivity.EXTRA_NOTE)
                    saveNote(note, noteIndex)
                }
                NoteDetailsActivity.ACTION_DELETE_NOTE -> {
                    deleteNote(noteIndex)
                }
            }

    }

    private fun saveNote(note: Note, noteIndex: Int){
        persistNote(this, note)
        if (noteIndex < 0) {
            notes.add(0, note)
        }
        else {
            notes[noteIndex] = note
        }
        adapter.notifyDataSetChanged()
    }

    private fun deleteNote(noteIndex: Int) {
            if(noteIndex < 0){
                return
            }
        val note = notes.removeAt(noteIndex)
        deleteNote(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.title} supprimé", Snackbar.LENGTH_SHORT).show()
    }

    fun showNoteDetails(noteIndex: Int){
        val note = if(noteIndex < 0) Note() else  notes[noteIndex]

        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra(NoteDetailsActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailsActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailsActivity.REQUEST_EDIT_NOTE)
    }
}
