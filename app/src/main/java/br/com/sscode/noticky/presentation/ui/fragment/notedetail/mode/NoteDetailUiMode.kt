package br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode

import android.os.Parcelable
import br.com.sscode.noticky.domain.entity.NoteDomain
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class NoteDetailUiMode : Parcelable {
    object Create : NoteDetailUiMode()
    data class Edit(val note: NoteDomain) : NoteDetailUiMode()
}