package br.com.sscode.noticky.presentation.ui.fragment.notedetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.sscode.noticky.R
import br.com.sscode.noticky.databinding.FragmentNoteDetailBinding
import br.com.sscode.noticky.domain.entity.NoteDomain
import com.google.android.material.transition.MaterialContainerTransform

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private val arguments: NoteDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureEnterSharedElementTransitions()
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
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureDisplayNoteFields(arguments.note)
    }

    private fun configureDisplayNoteFields(note: NoteDomain) = with(binding) {
        noteTitle.setText(note.title)
        noteDescription.setText(note.description)
    }
}