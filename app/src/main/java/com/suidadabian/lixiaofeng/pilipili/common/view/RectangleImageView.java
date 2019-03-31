package com.suidadabian.lixiaofeng.pilipili.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class RectangleImageView extends AppCompatImageView {
    public RectangleImageView(Context context) {
        super(context);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() / 4 * 3);
    }
}
