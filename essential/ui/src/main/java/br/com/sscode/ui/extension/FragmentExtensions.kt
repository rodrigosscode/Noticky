package br.com.sscode.ui.extension

import android.graphics.Color
import androidx.annotation.IdRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import br.com.sscode.iu.R
import com.google.android.material.R.attr.colorSurface
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

fun Fragment.resetSharedElementTransitionState() {
    postponeEnterTransition()
    view?.doOnPreDraw { startPostponedEnterTransition() }
}

fun Fragment.prepareEnterExitWithLargeScaleTransitions() {
    exitTransition = MaterialElevationScale(false).apply {
        duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
    }
    reenterTransition = MaterialElevationScale(true).apply {
        duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
    }
}

fun Fragment.prepareEnterSharedElementWithLargeTransitions(@IdRes drawingContentViewId: Int) {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = drawingContentViewId
        duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        scrimColor = Color.TRANSPARENT
        setAllContainerColors(requireContext().themeColor(colorSurface))
    }
}