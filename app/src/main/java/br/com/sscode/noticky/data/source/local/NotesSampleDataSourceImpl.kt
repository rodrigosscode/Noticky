package br.com.sscode.noticky.data.source.local

import br.com.sscode.noticky.domain.entity.NoteDomain

object NotesSampleDataSourceImpl {

    private val notes = ArrayList<NoteDomain>(sampleNotes)

    fun delete(note: NoteDomain) {
        notes.remove(note)
    }

    fun updateOrInsert(note: NoteDomain): Boolean = try {
        note.id?.let { noteId ->
            update(noteId, note)
        } ?: run {
            insert(note)
        }
    } catch (exception: Exception) {
        false
    }

    private fun insert(note: NoteDomain): Boolean {
        note.id = notes.count().toLong().plus(1)
        notes.add(note)
        return true
    }

    private fun update(noteId: Long, note: NoteDomain): Boolean {
        notes.find { it.id == noteId }?.let {
            val position = notes.indexOf(it)
            notes[position] = note
            return true
        } ?: return false
    }

    fun all(): List<NoteDomain> = notes
}