package com.bn.yfc.base;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.bn.yfc.R;
import com.bn.yfc.tools.*;
import com.bn.yfc.tools.Tools;

/**
 * Created by Administrator on 2017/3/10.
 */

public class BaseSearchEditText extends EditText {
    private SearchStart searchStartlistener;

    public BaseSearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setSeachStartLinstener(SearchStart seachStartLinstener) {
        this.searchStartlistener = seachStartLinstener;
    }

    public BaseSearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseSearchEditText(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setLeftImarge();
    }

    //绘制左边搜索按钮
    private void setLeftImarge() {
        Tools.setLog("测试不设置图片");
       /* Drawable search = getResources().getDrawable(R.drawable.search_icon);
        setCompoundDrawablesWithIntrinsicBounds(search, null, null, null);*/
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left += 50;
            rect.right = rect.left + 36;
            if (rect.contains(eventX, eventY)) {
                SeachStart(true);
            } else {
                SeachStart(false);
            }
        }

        return super.onTouchEvent(event);
    }

    public void SeachStart(boolean start) {
        if (searchStartlistener != null) {
            searchStartlistener.SearchStart(start);
        }
    }
}
