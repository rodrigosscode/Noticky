package br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel

import androidx.lifecycle.ViewModel
import br.com.sscode.core.EMPTY_STRING
import br.com.sscode.noticky.data.source.local.NotesSampleDataSourceImpl
import br.com.sscode.noticky.domain.entity.NoteDomain

class NoteDetailViewModel : ViewModel() {

    private val dataSource = NotesSampleDataSourceImpl

    private var currentData = NoteDomain(EMPTY_STRING, EMPTY_STRING)

    fun loadNewNote() {

    }

    fun setNote(data: NoteDomain) {
        currentData = data
    }

    fun updateNote(noteTitle: String, noteDescription: String) {
        val newNoteItem = NoteDomain(noteTitle, noteDescription)
        dataSource.update(currentData, newNoteItem)
    }

    sealed class UiState {
        data class Loaded(val data: NoteDomain)
    }
}