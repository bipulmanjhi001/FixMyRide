package com.ust.fixmyride.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Sudipta Paul on 11-08-2016.
 */
public class RoundedLayout extends LinearLayout {
    private RectF rect;
    private Paint paint;

    public RoundedLayout(Context context) {
        super(context);
        init();
    }
    public RoundedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#7EB5D6"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(rect, 20, 20, paint);
    }
}