package com.unbeaned.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.unbeaned.app.R;

public class DynamicScrollView extends ScrollView {
    private int watchID;

    public DynamicScrollView(Context context) {
        super(context);
    }

    public DynamicScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicScrollView);

        watchID = a.getResourceId(R.styleable.DynamicScrollView_watchID, -1);

        a.recycle();

    }

    public DynamicScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    public DynamicScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // Get View specified by watchID attribute
        View targetView = getRootView().findViewById(watchID);



        Log.i("ScrollView", "Hero: " + targetView);
//        Log.i("ScrollView", "Hero: " + rootGroup.getChildAt(1).getId());

//        Log.i("ScrollView", "ScrollY: " + getScrollY());
//        Log.i("ScrollView", "Bottom Hero: " + bottomHero.getBottom());


        super.onScrollChanged(l, t, oldl, oldt);



    }
}
