package com.example.gopaywallet.ui.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gopaywallet.R

class TransactionItemDecoration : RecyclerView.ItemDecoration() {
    
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.list_item_padding)
            outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.list_item_padding)
            outRect.top = view.context.resources.getDimensionPixelSize(R.dimen.list_item_vertical_spacing)
            outRect.bottom = view.context.resources.getDimensionPixelSize(R.dimen.list_item_vertical_spacing)
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPaint = Paint().apply {
            color = ContextCompat.getColor(parent.context, R.color.divider_color)
            strokeWidth = parent.context.resources.getDimension(R.dimen.divider_height)
        }

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val bottom = child.bottom + params.bottomMargin
            canvas.drawLine(
                child.left.toFloat(),
                bottom.toFloat(),
                child.right.toFloat(),
                bottom.toFloat(),
                dividerPaint
            )
        }
    }
} 