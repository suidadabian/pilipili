package com.suidadabian.lixiaofeng.pilipili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class RefreshLoadFragment<T> extends Fragment {
    private SmartRefreshLayout mRefreshLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = view.findViewById(getRefreshLayoutId());
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                internalLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                internalRefresh();
            }
        });
        mRefreshLayout.autoRefresh();
    }

    protected abstract int getRefreshLayoutId();

    private void internalRefresh() {
        onRefreshStart();
        onRefresh().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    onRefreshFinish(data);
                    mRefreshLayout.finishRefresh(true);
                }, throwable -> {
                    onRefreshFail(throwable);
                    mRefreshLayout.finishRefresh(false);
                });
    }

    private void internalLoadMore() {
        onLoadMoreStart();
        onLoadMore().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    boolean noMore = onLoadMoreFinish(data);
                    mRefreshLayout.finishLoadMore(0, true, noMore);
                }, throwable -> {
                    onLoadMoreFail(throwable);
                    mRefreshLayout.finishLoadMore(false);
                });
    }

    protected abstract void onRefreshStart();

    protected abstract void onLoadMoreStart();

    protected abstract Observable<List<T>> onRefresh();

    protected abstract Observable<List<T>> onLoadMore();

    protected abstract void onRefreshFinish(List<T> data);

    protected abstract boolean onLoadMoreFinish(List<T> data);

    protected abstract void onRefreshFail(Throwable throwable);

    protected abstract void onLoadMoreFail(Throwable throwable);
}
