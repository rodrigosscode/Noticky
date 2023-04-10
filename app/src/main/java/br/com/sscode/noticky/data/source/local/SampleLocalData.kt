package br.com.sscode.noticky.data.source.local

import br.com.sscode.noticky.domain.entity.NoteDomain

val sampleNotes = listOf(
    NoteDomain(
        id = 1L,
        title = "Note Title",
        description = "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
    ),
    NoteDomain(
        id = 2L,
        title = "Note Title",
        description = "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages"
    ),
    NoteDomain(
        id = 3L,
        title = "Note Title",
        description = "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
    ),
    NoteDomain(
        id = 4L,
        title = "Note Title",
        description = "It was popularised in the 1960s with the release"
    ),
    NoteDomain(
        id = 5L,
        title = "Note Title",
        description = "It was popularised in the 1960s with the release of Letraset sheets containing"
    ),
    NoteDomain(
        id = 6L,
        title = "Note Title",
        description = "It was popularised in the 1960s"
    )
)