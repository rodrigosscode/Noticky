package br.com.sscode.noticky.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteDomain(
    val title: String,
    val description: String
) : Parcelable
