package br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sscode.noticky.data.source.local.NotesSampleDataSourceImpl
import br.com.sscode.noticky.domain.entity.NoteDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel : ViewModel() {

    private val dataSource = NotesSampleDataSourceImpl

    private var _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Introducing)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var currentDataNote: NoteDomain? = null

    fun performAction(action: UiAction) {
        when (action) {
            UiAction.LoadNewNote -> {
                setCurrentDataNote(NoteDomain())
                onLoadNoteSucceeded()
            }
            is UiAction.LoadNote -> {
                setCurrentDataNote(action.data)
                onLoadNoteSucceeded()
            }
            is UiAction.UpdateNote -> {
                updateNote(action.title, action.description)
            }
        }
    }

    private fun setCurrentDataNote(note: NoteDomain) {
        currentDataNote = note
    }

    private fun updateNote(title: String, description: String) {
        currentDataNote?.run {
            setCurrentDataNote(copy(title = title, description = description))
            currentDataNote?.let { note ->
                if (dataSource.updateOrInsert(note)) {
                    onLoadNoteSucceeded()
                }
            }
        }
    }

    private fun onLoadNoteSucceeded() = viewModelScope.launch {
        currentDataNote?.let { note ->
            _uiState.emit(UiState.Loaded(data = note))
        }
    }

    fun isIntroducing(): Boolean = _uiState.value == UiState.Introducing

    sealed class UiState {
        object Introducing : UiState()
        data class Loaded(val data: NoteDomain) : UiState()
    }

    sealed class UiAction {
        object LoadNewNote : UiAction()
        data class LoadNote(val data: NoteDomain) : UiAction()
        data class UpdateNote(val title: String, val description: String) : UiAction()
    }
}