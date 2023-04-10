package br.com.sscode.noticky.presentation.ui.fragment.notedetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.sscode.noticky.R
import br.com.sscode.noticky.databinding.FragmentNoteDetailBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode.Create
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode.Edit
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel.NoteDetailViewModel
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel.NoteDetailViewModel.UiAction.*
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel.NoteDetailViewModel.UiState.Loaded
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel.NoteDetailViewModel.UiState.Introducing
import br.com.sscode.ui.extensions.getValueText
import com.google.android.material.transition.MaterialContainerTransform
import timber.log.Timber

class NoteDetailFragment : Fragment() {

    private var _noteDetailBinding: FragmentNoteDetailBinding? = null
    private val noteDetailBinding: FragmentNoteDetailBinding
        get() = _noteDetailBinding ?: throw UninitializedPropertyAccessException()

    private val arguments: NoteDetailFragmentArgs by navArgs()

    private val noteDetailViewModel: NoteDetailViewModel by viewModels()

    private var isViewBindingReusable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) = try {
        super.onCreate(savedInstanceState)
        configureEnterSharedElementTransitions()
    } catch (exception: Exception) {
        Timber.e(exception)
    }

    private fun configureEnterSharedElementTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.WHITE
            setAllContainerColors(Color.WHITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = try {
        _noteDetailBinding?.run {
            isViewBindingReusable = true
            this
        } ?: run {
            isViewBindingReusable = false
            _noteDetailBinding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        }
        noteDetailBinding.root
    } catch (exception: Exception) {
        Timber.e(exception)
        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            setupToolbar()
            initObserverViewState()
            if (noteDetailViewModel.isIntroducing()) {
                loadViewStateActionByUiMode(arguments.uiMode)
            }
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    private fun setupToolbar() = with(noteDetailBinding) {
        toolbarView.setNavigationIcon(R.drawable.ic_back_screen)
        toolbarView.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initObserverViewState(): Unit = with(noteDetailViewModel) {
        lifecycleScope.launchWhenResumed {
            uiState.collect { uiState ->
                when (uiState) {
                    Introducing -> {}
                    is Loaded -> uiState.data.run {
                        configureDisplayNoteFields(title, description)
                    }
                }
            }
        }
    }

    private fun loadViewStateActionByUiMode(uiMode: NoteDetailUiMode) = when (uiMode) {
        Create -> noteDetailViewModel.performAction(LoadNewNote)
        is Edit -> with(uiMode) {
            loadDisplayNotePreview(note)
            noteDetailViewModel.performAction(LoadNote(note))
        }
    }

    private fun loadDisplayNotePreview(note: NoteDomain) = with(note) {
        configureDisplayNoteFields(title, description)
    }

    private fun configureDisplayNoteFields(noteTitle: String, noteDescription: String) =
        with(noteDetailBinding) {
            noteTitleView.setText(noteTitle)
            noteDescriptionView.setText(noteDescription)
        }

    private fun onBackPressed() = with(findNavController()) {
        navigateUp() || popBackStack()
    }

    override fun onPause() {
        super.onPause()
        updateNote()
    }

    private fun updateNote() {
        noteDetailViewModel.performAction(
            UpdateNote(
                title = getNoteTitle(),
                description = getNoteDescription()
            )
        )
    }

    private fun getNoteTitle(): String = noteDetailBinding.noteTitleView.getValueText()

    private fun getNoteDescription(): String = noteDetailBinding.noteDescriptionView.getValueText()

    override fun onDestroy() {
        super.onDestroy()
        _noteDetailBinding = null
    }
}