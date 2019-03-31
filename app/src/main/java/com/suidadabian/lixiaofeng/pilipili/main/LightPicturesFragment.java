package com.suidadabian.lixiaofeng.pilipili.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.RefreshLoadFragment;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.watch.PictureWatchActivity;

import java.util.List;

import io.reactivex.Observable;

public class LightPicturesFragment extends RefreshLoadFragment<LightPicture> {
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;
    private RecyclerView mRecyclerView;
    private LightPicturesAdapter mLightPicturesAdapter;
    private int mCurrentPageNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_pictures, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.light_pictures_rv);

        mLightPicturesAdapter = new LightPicturesAdapter(this);
        mLightPicturesAdapter.setOnItemClickListener(lightPicture -> {
            Intent intent = new Intent(getActivity(), PictureWatchActivity.class);
            intent.putExtra(PictureWatchActivity.KEY_PICTURE_URL, lightPicture.getUrl());
            intent.putExtra(PictureWatchActivity.KEY_FLAG_DOWNLOAD_ENABLE, true);
            startActivity(intent);
        });
        mRecyclerView.setAdapter(mLightPicturesAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.light_pictures_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
    }

    @Override
    protected Observable<List<LightPicture>> onRefresh() {
        return PiliPiliServer.getInstance().getLightPictures(START_PAGE_NO, PAGE_SIZE);
    }

    @Override
    protected Observable<List<LightPicture>> onLoadMore() {
        return PiliPiliServer.getInstance().getLightPictures(mCurrentPageNo + 1, PAGE_SIZE);
    }

    @Override
    protected void onRefreshFinish(List<LightPicture> data) {
        mCurrentPageNo = START_PAGE_NO;
        mLightPicturesAdapter.notifyItemRangeRemoved(0, mLightPicturesAdapter.getItemCount());
        mLightPicturesAdapter.refresh(data);
        mLightPicturesAdapter.notifyItemRangeInserted(0, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<LightPicture> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        mLightPicturesAdapter.notifyItemRangeInserted(mLightPicturesAdapter.getItemCount(), data.size());
        mLightPicturesAdapter.insertLightPicture(data);
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}
