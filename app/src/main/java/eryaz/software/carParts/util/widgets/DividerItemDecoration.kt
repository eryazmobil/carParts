package eryaz.software.carParts.util.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DividerItemDecoration(private var spacingPx: Int) :
    RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var isVertical = true

        if (parent.layoutManager is LinearLayoutManager)
            isVertical =
                (parent.layoutManager as LinearLayoutManager).orientation == RecyclerView.VERTICAL
        else if (parent.layoutManager is GridLayoutManager)
            isVertical =
                (parent.layoutManager as GridLayoutManager).orientation == RecyclerView.VERTICAL

        if (isVertical) {
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

            val lp = view.layoutParams as RecyclerView.LayoutParams
            val itemPosition = lp.viewAdapterPosition

            if (itemPosition < spanCount)
                lp.topMargin = 0
            else
                lp.topMargin = spacingPx

            lp.leftMargin = spacingPx / 2
            lp.rightMargin = spacingPx / 2

            lp.bottomMargin = 0
            view.layoutParams = lp

            if (parent.tag == null) {
                parent.tag = true

                if (parent.paddingLeft > spacingPx / 2) {
                    parent.setPadding(
                        parent.paddingLeft - spacingPx / 2,
                        parent.paddingTop,
                        parent.paddingRight - spacingPx / 2,
                        parent.paddingBottom
                    )
                }
            }
        } else {
            val lp = view.layoutParams as RecyclerView.LayoutParams
            val itemPosition = lp.viewAdapterPosition

            lp.topMargin = 0

            if (itemPosition == 0) {
                lp.leftMargin = 0
                lp.rightMargin = spacingPx / 2
            } else {
                lp.leftMargin = spacingPx / 2
                lp.rightMargin = spacingPx / 2
            }

            lp.bottomMargin = 0
            view.layoutParams = lp

            if (parent.tag == null) {
                parent.tag = true

                if (parent.paddingLeft > spacingPx / 2) {
                    parent.setPadding(
                        parent.paddingLeft - spacingPx / 2,
                        parent.paddingTop,
                        parent.paddingRight - spacingPx / 2,
                        parent.paddingBottom
                    )
                }
            }
        }

        super.getItemOffsets(outRect, view, parent, state)
    }
}