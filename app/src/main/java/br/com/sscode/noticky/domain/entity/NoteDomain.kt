package br.com.sscode.noticky.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteDomain(
    var title: String = "",
    var description: String = "",
    var id: Long? = null
) : Parcelable

fun NoteDomain.isEmpty(): Boolean {
    return title.isEmpty() && description.isEmpty()
}