package br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.sscode.noticky.domain.entity.NoteDomain

class NoteListAdapter(
    private val onNoteClick: (noteDomain: NoteDomain, noteView: View) -> Unit
) : ListAdapter<NoteDomain, NoteListViewHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder.create(viewGroupParent = parent)

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        getItem(position).let { noteDomain ->
            holder.bind(noteDomain, onNoteClick)
        }
    }

    object NoteDiffCallback : DiffUtil.ItemCallback<NoteDomain>() {

        override fun areItemsTheSame(oldItem: NoteDomain, newItem: NoteDomain): Boolean =
            oldItem.title == newItem.title && oldItem.description == newItem.description

        override fun areContentsTheSame(oldItem: NoteDomain, newItem: NoteDomain): Boolean =
            oldItem == newItem
    }
}
