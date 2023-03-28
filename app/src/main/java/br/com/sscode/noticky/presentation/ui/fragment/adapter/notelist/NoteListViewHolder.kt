package br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.sscode.noticky.R
import br.com.sscode.noticky.databinding.ListNoteItemBinding
import br.com.sscode.noticky.domain.entity.NoteDomain

class NoteListViewHolder(
    private val binding: ListNoteItemBinding
) : ViewHolder(binding.root) {

    fun bind(
        noteDomain: NoteDomain,
        onNoteClick: (noteDomain: NoteDomain, noteView: View) -> Unit
    ) = with(binding) {
        configureDisplayFields(noteDomain)
        configureListeners(onNoteClick, noteDomain)
        configureTransitions()
    }

    private fun configureDisplayFields(noteDomain: NoteDomain) = with(binding) {
        title.text = noteDomain.title
        description.text = noteDomain.description
    }

    private fun configureListeners(
        onNoteClick: (noteDomain: NoteDomain, noteView: View) -> Unit,
        noteDomain: NoteDomain
    ) = with(binding) {
        root.setOnClickListener {
            onNoteClick(noteDomain, root)
        }
    }

    private fun configureTransitions() = with(binding) {
        root.resources.getString(R.string.note_transition_name).let { transitionName ->
            ViewCompat.setTransitionName(root, transitionName.plus(adapterPosition))
        }
    }

    companion object {

        fun create(viewGroupParent: ViewGroup): NoteListViewHolder = NoteListViewHolder(
            binding = ListNoteItemBinding.inflate(
                LayoutInflater.from(viewGroupParent.context),
                viewGroupParent,
                false
            )
        )
    }
}
