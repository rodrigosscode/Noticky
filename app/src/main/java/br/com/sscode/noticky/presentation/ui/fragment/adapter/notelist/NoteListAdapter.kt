package br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.domain.entity.extension.areContentTheSame
import br.com.sscode.noticky.domain.entity.extension.areItemTheSame
import timber.log.Timber

class NoteListAdapter(
    private val onNoteClick: (noteDomain: NoteDomain, noteView: View) -> Unit,
    private val onEmptyNoteIdentified: (noteDomain: NoteDomain) -> Unit,
    private val onRequestPositionAtTop: () -> Unit
) : ListAdapter<NoteDomain, NoteListViewHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder.create(viewGroupParent = parent)

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        try {
            getItem(position).let { noteDomain ->
                holder.bind(noteDomain, onEmptyNoteIdentified, onNoteClick)
            }
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<NoteDomain>,
        currentList: MutableList<NoteDomain>
    ) {
        try {
            super.onCurrentListChanged(previousList, currentList)
            if (isAddedNewItem(currentList, previousList) or
                isFirstItemChanged(currentList, previousList)
            ) {
                onRequestPositionAtTop()
            }
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    private fun isFirstItemChanged(
        currentList: MutableList<NoteDomain>,
        previousList: MutableList<NoteDomain>
    ) = currentList.size == previousList.size && !NoteDiffCallback.areItemsTheSame(
        previousList.first(),
        currentList.first()
    )

    private fun isAddedNewItem(
        currentList: MutableList<NoteDomain>,
        previousList: MutableList<NoteDomain>
    ) = currentList.size > previousList.size

    object NoteDiffCallback : DiffUtil.ItemCallback<NoteDomain>() {

        override fun areItemsTheSame(oldItem: NoteDomain, newItem: NoteDomain): Boolean =
            oldItem.areItemTheSame(newItem)

        override fun areContentsTheSame(oldItem: NoteDomain, newItem: NoteDomain): Boolean =
            oldItem.areContentTheSame(newItem)
    }
}
