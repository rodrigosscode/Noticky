package br.com.sscode.noticky.data.source.local

import br.com.sscode.noticky.domain.entity.NoteDomain

object NotesSampleDataSourceImpl {

    private val notes = ArrayList<NoteDomain>(sampleNotes)

    fun add(note: NoteDomain) {
        notes.add(note)
    }

    fun delete(note: NoteDomain) {
        notes.remove(note)
    }

    fun update(note: NoteDomain, newNoteItem: NoteDomain): Boolean = try {
        notes.find { it.title == note.title && it.description == note.description }?.let {
            notes.indexOf(it).let { position ->
                notes[position] = newNoteItem
                return true
            }
        } ?: false
    } catch (exception: Exception) {
        false
    }

    fun all(): List<NoteDomain> = notes
}