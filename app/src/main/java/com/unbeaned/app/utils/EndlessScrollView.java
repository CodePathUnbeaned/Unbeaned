package com.unbeaned.app.utils;

import android.os.Build;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

@RequiresApi(api = Build.VERSION_CODES.M)
public abstract class EndlessScrollView extends EndlessRecyclerViewScrollListener implements View.OnScrollChangeListener {

    private RecyclerView recyclerView;

    public EndlessScrollView(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        super(layoutManager);
        this.recyclerView = recyclerView;
    }

    public EndlessScrollView(LinearLayoutManager layoutManager) {
        super(layoutManager);
    }

    public EndlessScrollView(GridLayoutManager layoutManager) {
        super(layoutManager);
    }

    public EndlessScrollView(StaggeredGridLayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrolled(recyclerView, scrollX - oldScrollX, scrollY - oldScrollY);
    }
}
