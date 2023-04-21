package br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sscode.noticky.data.source.local.NotesSampleDataSourceImpl
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.domain.entity.extension.areItemTheSame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel : ViewModel() {

    private val dataSource = NotesSampleDataSourceImpl
    private var originalDataNote: NoteDomain? = null
    private var currentDataNote: NoteDomain? = null

    private var _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Introducing)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun performAction(action: UiAction) {
        when (action) {
            UiAction.LoadNewNote -> {
                setCurrentDataNote(NoteDomain())
                onLoadNoteSucceeded()
            }
            is UiAction.LoadNote -> {
                setOriginalDataNote(action.data)
                setCurrentDataNote(action.data)
                onLoadNoteSucceeded()
            }
            is UiAction.UpdateNote -> {
                updateNote(action.title, action.description)
            }
        }
    }

    private fun setOriginalDataNote(note: NoteDomain) {
        originalDataNote = note
    }

    private fun setCurrentDataNote(note: NoteDomain): NoteDomain {
        currentDataNote = note
        return note
    }

    private fun updateNote(title: String, description: String) {
        currentDataNote?.run {
            setCurrentDataNote(
                copy(
                    title = title,
                    description = description
                )
            ).let { currentDataNoteUpdated ->
                if (isNeededUpdateNote()) {
                    if (dataSource.updateOrInsert(currentDataNoteUpdated)) {
                        onLoadNoteSucceeded()
                    }
                }
            }
        }
    }

    private fun isNeededUpdateNote(): Boolean =
        currentDataNote?.let { currentDataNote ->
            originalDataNote?.let { originalDataNote ->
                !currentDataNote.areItemTheSame(originalDataNote)
            } ?: true
        } ?: false

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