package br.com.sscode.noticky.domain.entity.extension

import br.com.sscode.noticky.domain.entity.NoteDomain

fun NoteDomain.isEmpty(): Boolean =
    title.isEmpty() && description.isEmpty()

fun NoteDomain.areItemTheSame(other: NoteDomain): Boolean =
    title == other.title && description == other.description

fun NoteDomain.areContentTheSame(other: NoteDomain): Boolean =
    this == other