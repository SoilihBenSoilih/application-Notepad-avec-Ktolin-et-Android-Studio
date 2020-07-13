package android.kotlin.training

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NoteDetailsActivity : AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1
        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE_NOTE = "android.kotlin.training.notepad.actions.ACTION_SAVE_NOTE"
        val ACTION_DELETE_NOTE = "android.kotlin.training.notepad.actions.ACTION_DELETE_NOTE"
    }

    lateinit var note: Note
    var noteIndex: Int = -1

    lateinit var titleView : TextView
    lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById(R.id.title) as TextView
        textView = findViewById(R.id.text) as TextView

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                return true
            }
             R.id.action_delete -> {
                 showConfirmDeleteNoteDialog()
                 return true
             }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteNoteDialog() {
        val confirmFragment = ConfirmDeleteNoteDialogFragment(note.title)
        confirmFragment.listener = object : ConfirmDeleteNoteDialogFragment.confirmDialogDeleteListenner{
            override fun onDialogPositiveClick() {
                deleteNote()
            }

            override fun onDialogNegativeClick() { }

        }

        confirmFragment.show(supportFragmentManager,"confirmDeleteDialog")
    }

    fun saveNote(){
            note.title = titleView.text.toString()
            note.text = textView.text.toString()

            intent = Intent(ACTION_SAVE_NOTE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            setResult(Activity.RESULT_OK, intent)
            finish()
    }

    fun deleteNote(){
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
