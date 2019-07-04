package com.augmentedreality.simplus.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.augmentedreality.simplus.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



/**
 * Draws a divider line using a drawable divider. Considers that the view holder layout doesn't have
 * its own layout-wide margins and paddings.
 *
 * @author Denver Abelarde
 */
public class DividerItemDecorator extends RecyclerView.ItemDecoration {

    private final Drawable divider;

    public DividerItemDecorator(Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getLeft();
        int right = parent.getRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int top = child.getBottom();
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }
}
