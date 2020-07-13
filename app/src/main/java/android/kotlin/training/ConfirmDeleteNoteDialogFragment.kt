package android.kotlin.training

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDeleteNoteDialogFragment(val noteTitle : String="") : DialogFragment() {

    interface confirmDialogDeleteListenner{
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    var listener: confirmDialogDeleteListenner? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setMessage("Êtes vous sûr(e) de vouloir supprimer la note\"$noteTitle\" ?")
            .setPositiveButton("Supprimer",
                                DialogInterface.OnClickListener { dialog, id ->  listener?.onDialogPositiveClick()})
            .setNegativeButton("Annuler",
                DialogInterface.OnClickListener { dialog, id ->  listener?.onDialogNegativeClick()})

        return builder.create()
    }

}