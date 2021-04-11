package com.unbeaned.app.utils;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessNestedScrollView extends EndlessRecyclerViewScrollListener implements NestedScrollView.OnScrollChangeListener {

    private RecyclerView recyclerView;

    public EndlessNestedScrollView(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        super(layoutManager);
        this.recyclerView = recyclerView;
    }

    public EndlessNestedScrollView(LinearLayoutManager layoutManager) {
        super(layoutManager);
    }

    public EndlessNestedScrollView(GridLayoutManager layoutManager) {
        super(layoutManager);
    }

    public EndlessNestedScrollView(StaggeredGridLayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        onUIChange(v);
        super.onScrolled(recyclerView, scrollX - oldScrollX, scrollY - oldScrollY);
    }

    public abstract void onUIChange(NestedScrollView v);
}
