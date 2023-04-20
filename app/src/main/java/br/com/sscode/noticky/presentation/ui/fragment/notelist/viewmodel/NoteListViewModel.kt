package br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sscode.core.extensions.anotherInstance
import br.com.sscode.noticky.data.source.local.NotesSampleDataSourceImpl
import br.com.sscode.noticky.domain.entity.NoteDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel : ViewModel() {

    private val dataSource = NotesSampleDataSourceImpl

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Introducing)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun performAction(uiAction: UiAction) = when (uiAction) {
        UiAction.LoadNotes -> loadNotes()
        is UiAction.RemoveNote -> removeNote(uiAction.noteDomain)
    }

    private fun removeNote(noteDomain: NoteDomain) {
        dataSource.delete(noteDomain)
        notifyRemoveEmptyNoteSuccess()
        loadNotes()
    }

    private fun notifyRemoveEmptyNoteSuccess() {
        onEmptyNoteRemoveSucceeded()
    }

    private fun loadNotes() {
        dataSource.all().anotherInstance().reversed().let {
            if (it.isEmpty()) {
                onLoadNoteListEmpty()
            } else {
                onLoadNoteListSucceeded(it)
            }
        }
    }

    private fun onEmptyNoteRemoveSucceeded() = viewModelScope.launch {
        _uiState.emit(UiState.Loaded(noteListUiState = NoteListUiState.RemoveEmptyNoteSuccess))
    }

    private fun onLoadNoteListSucceeded(notes: List<NoteDomain>) = viewModelScope.launch {
        _uiState.emit(UiState.Loaded(noteListUiState = NoteListUiState.Success(data = notes)))
    }

    private fun onLoadNoteListEmpty() = viewModelScope.launch {
        _uiState.emit(UiState.Loaded(noteListUiState = NoteListUiState.Empty))
    }

    fun isAlreadyIntroduced(): Boolean = _uiState.value != UiState.Introducing

    sealed class UiState {
        object Introducing : UiState()
        data class Loaded(val noteListUiState: NoteListUiState) : UiState()
    }

    sealed class NoteListUiState {
        object Loading : NoteListUiState()
        object Empty : NoteListUiState()
        data class Error(val throwable: Throwable) : NoteListUiState()
        data class Success(
            val data: List<NoteDomain> = emptyList()
        ) : NoteListUiState()
        object RemoveEmptyNoteSuccess : NoteListUiState()
    }

    sealed class UiAction {
        object LoadNotes : UiAction()
        data class RemoveNote(val noteDomain: NoteDomain) : UiAction()
    }
}