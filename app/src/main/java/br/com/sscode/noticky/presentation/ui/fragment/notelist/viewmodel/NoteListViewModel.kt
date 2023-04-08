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

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Introducing)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val dataSource = NotesSampleDataSourceImpl

    fun loadNotes() {
        onLoadNoteListSucceeded(dataSource.all().anotherInstance())
    }

    private fun onLoadNoteListSucceeded(data: List<NoteDomain>) = viewModelScope.launch {
        _uiState.emit(UiState.Loaded(noteListUiState = NoteListUiState.Success(data)))
    }

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
    }
}