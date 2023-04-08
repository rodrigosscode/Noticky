package br.com.sscode.ui.recyclerview.helper

import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredGridScrollStateHelper(
    private val layoutManager: StaggeredGridLayoutManager
) {
    private var scrollPositions: IntArray? = null

    fun saveScrollState(): Parcelable {
        return Bundle().apply {
            scrollPositions = IntArray(layoutManager.spanCount)
            layoutManager.findFirstVisibleItemPositions(scrollPositions)
            putIntArray(KEY_SCROLL_POSITIONS, scrollPositions)
        }
    }

    fun restoreScrollState(state: Parcelable?) {
        if (state is Bundle) {
            scrollPositions = state.getIntArray(KEY_SCROLL_POSITIONS)
            layoutManager.scrollToPositionWithOffset(scrollPositions?.minOrNull() ?: 0, 0)
        }
    }

    companion object {
        private const val KEY_SCROLL_POSITIONS = "key_scroll_positions_noticky_app"
    }
}