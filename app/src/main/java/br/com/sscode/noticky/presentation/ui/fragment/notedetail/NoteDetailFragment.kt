package br.com.sscode.noticky.presentation.ui.fragment.notedetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.sscode.noticky.R
import br.com.sscode.noticky.databinding.FragmentNoteDetailBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.mode.NoteDetailUiMode
import br.com.sscode.noticky.presentation.ui.fragment.notedetail.viewmodel.NoteDetailViewModel
import com.google.android.material.transition.MaterialContainerTransform
import timber.log.Timber

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private val arguments: NoteDetailFragmentArgs by navArgs()
    private val noteDetailViewModel: NoteDetailViewModel by viewModels()

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = try {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        binding.root
    } catch (exception: Exception) {
        Timber.e(exception)
        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = try {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        configureFieldsByUiMode(arguments.uiMode)
    } catch (exception: Exception) {
        Timber.e(exception)
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationIcon(R.drawable.ic_back_screen)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun configureFieldsByUiMode(uiMode: NoteDetailUiMode) = when(uiMode) {
        NoteDetailUiMode.Create -> {
            noteDetailViewModel.loadNewNote()
        }
        is NoteDetailUiMode.Edit -> {
            configureDisplayNoteFields(uiMode.data)
            noteDetailViewModel.setNote(uiMode.data)
        }
    }

    private fun configureDisplayNoteFields(note: NoteDomain) = with(binding) {
        noteTitle.setText(note.title)
        noteDescription.setText(note.description)
    }

    private fun onBackPressed() = with(findNavController()) {
        updateNote()
        navigateUp() || popBackStack()
    }

    private fun updateNote() {
        val noteTitle = binding.noteTitle.text.toString()
        val noteDescription = binding.noteDescription.text.toString()
        noteDetailViewModel.updateNote(noteTitle, noteDescription)
    }
}