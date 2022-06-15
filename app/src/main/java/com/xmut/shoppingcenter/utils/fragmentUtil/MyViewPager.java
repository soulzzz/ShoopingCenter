package com.xmut.shoppingcenter.utils.fragmentUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager{
    private boolean CanScoll = false;
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    public boolean isCanScoll() {
        return CanScoll;
    }

    public void setCanScoll(boolean canScoll) {
        CanScoll = canScoll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return CanScoll;
//        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
            return CanScoll;
    }


}
