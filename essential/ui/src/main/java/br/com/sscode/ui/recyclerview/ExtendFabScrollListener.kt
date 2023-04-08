package br.com.sscode.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ExtendFabScrollListener(
    private val view: ExtendedFloatingActionButton
) : OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (isOnTopOfRecycler(recyclerView) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            view.extend()
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            view.shrink()
        }
    }

    private fun isOnTopOfRecycler(recyclerView: RecyclerView): Boolean {
//        recyclerView.layoutManager.let { layoutManager ->
//            layoutManager.findFirstCompletelyVisibleItemPositions(null)
//                .let { firstVisibleItemPositions ->
//                    return firstVisibleItemPositions[FIRST_ITEM_POSITION] == FIRST_ITEM_POSITION
//                }
//        }
        return true
    }

    companion object {
        private const val FIRST_ITEM_POSITION = 0
    }
}