package android.kotlin.training

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val notes: List<Note>, val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter <ListAdapter.ViewHolder>(){

    class ViewHolder(itemView: View ) : RecyclerView.ViewHolder(itemView){
        val cardview = itemView.findViewById<CardView>(R.id.card_view)
        val title = cardview.findViewById<TextView>(R.id.title)
        val excerpt = cardview.findViewById<TextView>(R.id.excerpt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val note = notes[position]
            holder.cardview.setOnClickListener(itemClickListener)
            holder.cardview.tag = position
            holder.title.text = note.title
            holder.excerpt.text = note.text

     }
    override fun getItemCount(): Int {
        return notes.size
    }
}