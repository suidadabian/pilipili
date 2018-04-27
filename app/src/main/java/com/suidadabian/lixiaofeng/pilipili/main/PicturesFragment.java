package com.suidadabian.lixiaofeng.pilipili.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.suidadabian.lixiaofeng.pilipili.R;


public abstract class PicturesFragment extends Fragment {
    private RefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pictures, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRefreshLayout = view.findViewById(R.id.pictures_refresh_layout);
        mRecyclerView = view.findViewById(R.id.pictures_rv);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PicturesFragment.this.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                PicturesFragment.this.onRefresh();
            }
        });
    }


    protected void onLoadMore() {

    }

    protected void onRefresh() {

    }


    protected void onLoadMoreFinish() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadMore();
        }
    }

    protected void onRefreshFinish() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }
}
