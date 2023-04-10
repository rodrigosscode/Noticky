package br.com.sscode.noticky.presentation.ui.fragment.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.sscode.noticky.R
import br.com.sscode.noticky.databinding.FragmentNoteListBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.presentation.ui.fragment.adapter.notelist.NoteListAdapter
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.NoteListUiState
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.NoteListUiState.*
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiAction.LoadNotes
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiState.Introducing
import br.com.sscode.noticky.presentation.ui.fragment.notelist.viewmodel.NoteListViewModel.UiState.Loaded
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class NoteListFragment : Fragment() {

    private var _noteListBinding: FragmentNoteListBinding? = null
    private val noteListBinding: FragmentNoteListBinding
        get() = _noteListBinding ?: throw UninitializedPropertyAccessException()

    private val noteListViewModel: NoteListViewModel by viewModels()

    private var isViewBindingReusable: Boolean = false

    private val noteListAdapter: NoteListAdapter by lazy {
        NoteListAdapter(this@NoteListFragment::navigateToNoteDetailEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) = try {
        super.onCreate(savedInstanceState)
        configureSharedElementTransitions()
    } catch (exception: Exception) {
        Timber.e(exception)
    }

    private fun configureSharedElementTransitions() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = try {
        super.onViewCreated(view, savedInstanceState)
        if (isViewBindingReusable) {
            initObserverViewState()
            noteListViewModel.performAction(LoadNotes)
        } else {
            configureNotesRecyclerView()
            configureAddNoteFab()
            initObserverViewState()
            if (noteListViewModel.isAlreadyIntroduced()) {
                noteListViewModel.performAction(LoadNotes)
            }
        }
        resetSharedElementTransitionState(view)
    } catch (exception: Exception) {
        Timber.e(exception)
    }

    private fun resetSharedElementTransitionState(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun configureNotesRecyclerView() = noteListBinding.notesView.apply {
        layoutManager = StaggeredGridLayoutManager(
            SPAN_COUNT_NOTE_LIST,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapter = noteListAdapter
    }

    private fun configureAddNoteFab() = with(noteListBinding) {
        addNoteView.shrink()
        addNoteView.setOnClickListener {
            navigateToNoteDetailCreate()
        }
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
            Empty -> {}
            is Error -> {}
            is Success -> updateNoteListData(noteListUiState.data)
        }

    private fun configureIntroducingAddNoteFabView() = with(noteListBinding) {
        lifecycleScope.launch {
            addNoteView.extend()
            TimeUnit.SECONDS.toMillis(SECONDS_INTRODUCE_FAB_ACTION).let { secondsInMillis ->
                delay(secondsInMillis)
            }
            addNoteView.shrink()
        }
    }

    private fun updateNoteListData(notes: List<NoteDomain>) {
        noteListAdapter.submitList(notes)
    }

    private fun navigateToNoteDetailEdit(note: NoteDomain, noteView: View) {
        navigateToNoteDetail(uiMode = NoteDetailUiMode.Edit(note), noteView)
    }

    private fun navigateToNoteDetailCreate() {
        navigateToNoteDetail(uiMode = NoteDetailUiMode.Create)
    }

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

    override fun onDestroy() {
        super.onDestroy()
        _noteListBinding = null
    }

    companion object {
        private const val SPAN_COUNT_NOTE_LIST = 2
        private const val SECONDS_INTRODUCE_FAB_ACTION = 3L
    }
}