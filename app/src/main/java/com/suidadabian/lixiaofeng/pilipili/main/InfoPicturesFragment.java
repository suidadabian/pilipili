package com.suidadabian.lixiaofeng.pilipili.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.RefreshLoadFragment;
import com.suidadabian.lixiaofeng.pilipili.detail.InfoPictureActivity;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;

import java.util.List;

import io.reactivex.Observable;

public class InfoPicturesFragment extends RefreshLoadFragment<InfoPicture> {
    private static final String TAG = InfoPicturesFragment.class.getSimpleName();
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 6;
    protected RecyclerView mRecyclerView;
    private InfoPicturesAdapter mInfoPicturesAdapter;
    private int mCurrentPageNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_pictures, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.info_pictures_rv);
        mInfoPicturesAdapter = new InfoPicturesAdapter(this);
        mRecyclerView.setAdapter(mInfoPicturesAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mInfoPicturesAdapter.setOnItemClickListener(infoPicture -> {
            Intent intent = new Intent(getContext(), InfoPictureActivity.class);
            intent.putExtra(InfoPictureActivity.kEY_INFO_PICTURE_ID, infoPicture.getId());
            startActivity(intent);
        });
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.info_pictures_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {

    }

    @Override
    protected void onLoadMoreStart() {

    }

    @Override
    protected Observable<List<InfoPicture>> onRefresh() {
        Log.d(TAG, "onRefresh");
        return PiliPiliServer.getInstance().getInfoPictures(START_PAGE_NO, PAGE_SIZE);
    }

    @Override
    protected Observable<List<InfoPicture>> onLoadMore() {
        return PiliPiliServer.getInstance().getInfoPictures(mCurrentPageNo + 1, PAGE_SIZE);
    }

    @Override
    protected void onRefreshFinish(List<InfoPicture> data) {
        mCurrentPageNo = START_PAGE_NO;
        mInfoPicturesAdapter.notifyItemRangeRemoved(0, mInfoPicturesAdapter.getItemCount());
        mInfoPicturesAdapter.refresh(data);
        mInfoPicturesAdapter.notifyItemRangeInserted(0, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<InfoPicture> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        int position = mInfoPicturesAdapter.getItemCount();
        mInfoPicturesAdapter.insertInfoPictures(data);
        mInfoPicturesAdapter.notifyItemRangeInserted(position, data.size());
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}