package br.com.sscode.noticky.data.source.local

import br.com.sscode.noticky.domain.entity.NoteDomain

object NotesSampleDataSourceImpl {

    private val notes = ArrayList<NoteDomain>(emptyList())
//    private val notes = ArrayList<NoteDomain>(sampleNotes)

    fun delete(note: NoteDomain): Boolean {
        notes.find { it.id == note.id }?.let {
            val position = notes.indexOf(it)
            notes.removeAt(position)
            return true
        } ?: return false
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
        note.id = notes.sumOf { it.id ?: 0 }.inc()
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