package br.com.sscode.noticky.presentation.ui.fragment.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.sscode.noticky.R
import br.com.sscode.noticky.data.source.local.sampleNotes
import br.com.sscode.noticky.databinding.FragmentNoteListBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist.NoteListAdapter
import com.google.android.material.transition.MaterialElevationScale
import timber.log.Timber

class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var noteAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetSharedElementTransitionState(view)
        configureNoteAdapter()
        configureNotesRecyclerView()
        updateNoteListData(sampleNotes)
    }

    private fun resetSharedElementTransitionState(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun configureNoteAdapter() {
        noteAdapter = NoteListAdapter { noteClicked, noteView ->
            Timber.i("Note Clicked: $noteClicked")
            navigateToNoteDetail(noteClicked, noteView)
        }
    }

    private fun configureNotesRecyclerView() = binding.notes.apply {
        layoutManager = StaggeredGridLayoutManager(
            SPAN_COUNT_NOTE_LIST,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapter = noteAdapter
    }

    private fun updateNoteListData(notes: List<NoteDomain>) {
        if (::noteAdapter.isInitialized) {
            noteAdapter.submitList(notes)
        }
    }

    private fun navigateToNoteDetail(note: NoteDomain, noteView: View) {
        NoteListFragmentDirections.actionToNoteDetail(note).let { navDirection ->
            configureSharedElementTransitions()
            FragmentNavigatorExtras(noteView to getString(R.string.note_transition_name)).let { extras ->
                findNavController().navigate(navDirection, extras)
            }
        }
    }

    private fun configureSharedElementTransitions() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    companion object {
        private const val SPAN_COUNT_NOTE_LIST = 2
    }
}