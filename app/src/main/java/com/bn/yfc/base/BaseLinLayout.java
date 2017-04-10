package com.bn.yfc.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/3/8.
 */

public class BaseLinLayout extends LinearLayout {

    public BaseLinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseLinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
