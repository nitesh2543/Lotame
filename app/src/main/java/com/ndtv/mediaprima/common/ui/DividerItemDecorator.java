package com.ndtv.mediaprima.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Elavarasan on 8/28/2016.
 */
public class DividerItemDecorator extends RecyclerView.ItemDecoration {

    public static final int LINEAR_HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int LINEAR_VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    public static final int LINE_FLAT = 0;

    private int space;
    private Paint paint = new Paint();
    private int color;
    private int orientation;
    private int top, bottom, left, right;
    private int lineType;
    private View child;

    public DividerItemDecorator(Context context, int orientation, int resourceColor) {
        color = context.getResources().getColor(resourceColor);
        paint.setColor(color);
        paint.setStrokeWidth(1);
        checkOrientation(orientation);
        lineType = LINE_FLAT;
    }

    public DividerItemDecorator(int space) {
        this.space = space;
    }

    private void checkOrientation(int orientation) {
        if (orientation != LINEAR_HORIZONTAL_LIST && orientation != LINEAR_VERTICAL_LIST) {
            throw new IllegalArgumentException("Invalid Orientation");
        } else
            this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (orientation == LINEAR_HORIZONTAL_LIST) {
            //drawLinearHorizontalDivider(c, parent);
        } else if (orientation == LINEAR_VERTICAL_LIST) {
            drawLinearVerticalDivider(c, parent);
        }

    }

    private void drawLinearVerticalDivider(Canvas c, RecyclerView parent) {
        left = parent.getLeft();
        right = parent.getWidth();

        for (int i = 0; i < parent.getChildCount(); i++) {
            child = parent.getChildAt(i);
            top = child.getTop();
            bottom = child.getBottom();
            Log.d("Dimensions: ", "Top is: " + top + ", Bottom is " + bottom);
            if (i != parent.getChildCount() - 1) {
                c.drawLine(left, bottom, right, bottom, paint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}
