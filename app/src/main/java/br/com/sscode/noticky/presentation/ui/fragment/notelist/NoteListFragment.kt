package br.com.sscode.noticky.presentation.ui.fragment.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.sscode.core.FIRST_POSITION
import br.com.sscode.noticky.R
import br.com.sscode.noticky.R.string.alert_remove_empty_note
import br.com.sscode.noticky.databinding.FragmentNoteListBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist.NoteListAdapter
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.NoteListUiState
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.NoteListUiState.*
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiAction.LoadNotes
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiAction.RemoveNote
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiState.Introducing
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiState.Loaded
import br.com.sscode.ui.extension.*
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class NoteListFragment : Fragment() {

    private var _noteListBinding: FragmentNoteListBinding? = null
    private val noteListBinding: FragmentNoteListBinding
        get() = _noteListBinding ?: throw UninitializedPropertyAccessException()

    private val noteListViewModel: NoteListViewModel by viewModels()

    private var isViewBindingReusable: Boolean = false

    private val noteListAdapter: NoteListAdapter by lazy {
        with(this@NoteListFragment) {
            NoteListAdapter(
                onNoteClick = ::navigateToNoteDetailEdit,
                onEmptyNoteIdentified = ::removeEmptyNote,
                onRequestPositionAtTop = ::positionNotesAtTop
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) = try {
        super.onCreate(savedInstanceState)
        prepareEnterExitWithLargeScaleTransitions()
    } catch (exception: Exception) {
        Timber.e(exception)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = try {
        _noteListBinding?.run {
            isViewBindingReusable = true
            this
        } ?: run {
            isViewBindingReusable = false
            _noteListBinding = FragmentNoteListBinding.inflate(inflater, container, false)
        }
        noteListBinding.root
    } catch (exception: Exception) {
        Timber.e(exception)
        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            resetSharedElementTransitionState()
            if (isViewBindingReusable) {
                initObserverViewState()
                noteListViewModel.performAction(LoadNotes)
            } else {
                configureNotesRecyclerView()
                configureAddNoteFabView()
                initObserverViewState()
                if (noteListViewModel.isAlreadyIntroduced()) {
                    noteListViewModel.performAction(LoadNotes)
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    private fun configureNotesRecyclerView() = noteListBinding.notesView.apply {
        layoutManager = StaggeredGridLayoutManager(
            SPAN_COUNT_NOTE_LIST,
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
        adapter = noteListAdapter
    }

    private fun configureAddNoteFabView() = with(noteListBinding) {
        addNoteView.shrink()
        addNoteView.setOnClickListener { navigateToNoteDetailCreate() }
    }

    private fun initObserverViewState() = with(noteListViewModel) {
        lifecycleScope.launchWhenResumed {
            uiState.collectLatest { uiState ->
                when (uiState) {
                    Introducing -> {
                        configureIntroducingAddNoteFabView()
                        noteListViewModel.performAction(LoadNotes)
                    }
                    is Loaded -> configureNoteListByState(uiState.noteListUiState)
                }
            }
        }
    }

    private fun configureNoteListByState(noteListUiState: NoteListUiState) =
        when (noteListUiState) {
            Loading -> {}
            Empty -> updateViewVisibilityForNoteListEmpty()
            is Error -> {}
            is Success -> {
                updateViewVisibilityForNoteListSuccess()
                updateNoteListViewData(notes = noteListUiState.data)
            }
            RemoveEmptyNoteSuccess -> showSnackBarLongMessage(
                message = getString(alert_remove_empty_note)
            )
        }

    private fun updateViewVisibilityForNoteListSuccess() = with(noteListBinding) {
        emptyDataStub.setGone()
        notesView.setVisible()
    }

    private fun updateViewVisibilityForNoteListEmpty() = with(noteListBinding) {
        emptyDataStub.setVisibleOrInflate()
        notesView.setInvisible()
    }

    private fun configureIntroducingAddNoteFabView() = with(noteListBinding) {
        lifecycleScope.launchDelayed(
            seconds = SECONDS_INTRODUCE_FAB_ACTION,
            actionBefore = { addNoteView.extend() },
            actionAfter = { addNoteView.shrink() }
        )
    }

    private fun updateNoteListViewData(notes: List<NoteDomain>) =
        noteListAdapter.submitList(notes)

    private fun removeEmptyNote(noteDomain: NoteDomain) =
        noteListViewModel.performAction(RemoveNote(noteDomain))

    private fun positionNotesAtTop() = with(noteListBinding.notesView) {
        smoothScrollToPosition(FIRST_POSITION)
    }

    private fun navigateToNoteDetailEdit(note: NoteDomain, noteView: View) =
        navigateToNoteDetail(uiMode = NoteDetailUiMode.Edit(note), noteView)

    private fun navigateToNoteDetailCreate() =
        navigateToNoteDetail(uiMode = NoteDetailUiMode.Create)

    private fun navigateToNoteDetail(
        uiMode: NoteDetailUiMode,
        noteView: View? = null
    ) = with(findNavController()) {
        NoteListFragmentDirections.actionToNoteDetail(uiMode).let { navDirection ->
            noteView?.let {
                navigate(navDirection, FragmentNavigatorExtras(buildNoteSharedElement(noteView)))
            } ?: navigate(navDirection)
        }
    }

    private fun buildNoteSharedElement(noteView: View): Pair<View, String> =
        noteView to getString(R.string.note_transition_name)

    override fun onPause() {
        super.onPause()
        forceShowAddNoteFabView()
    }

    private fun forceShowAddNoteFabView() = with(noteListBinding.addNoteView) {
        (layoutParams as CoordinatorLayout.LayoutParams).let { layoutParams ->
            (layoutParams.behavior as? HideBottomViewOnScrollBehavior)?.slideUp(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _noteListBinding = null
    }

    companion object {
        private const val SPAN_COUNT_NOTE_LIST = 2
        private const val SECONDS_INTRODUCE_FAB_ACTION = 3L
    }
}
