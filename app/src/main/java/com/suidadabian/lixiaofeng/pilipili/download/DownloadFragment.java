package com.suidadabian.lixiaofeng.pilipili.download;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.RefreshLoadFragment;
import com.suidadabian.lixiaofeng.pilipili.watch.PictureWatchActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DownloadFragment extends RefreshLoadFragment<String> {
    private static final int PAGE_SIZE = 10;
    private List<String> mPictures;
    private RecyclerView mRecyclerView;
    private DownloadAdapter mDownloadAdapter;
    private MaterialDialog mDeleteDialog;
    private int mCurrentPageNo;

    public static DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.download_rv);

        mDownloadAdapter = new DownloadAdapter(this);
        mDownloadAdapter.setOnItemClickListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String picture, int position) {
                Intent intent = new Intent(getContext(), PictureWatchActivity.class);
                intent.putExtra(PictureWatchActivity.KEY_PICTURE_URL, picture);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(String picture, int position) {
                new MaterialDialog.Builder(getContext())
                        .title("删除下载图片")
                        .content("确定删除这张图片吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();

                            if (mDeleteDialog == null) {
                                mDeleteDialog = new MaterialDialog.Builder(getContext())
                                        .content("正在删除中，请稍后")
                                        .progress(true, 0)
                                        .show();
                            } else {
                                mDeleteDialog.show();
                            }

                            Observable.just(picture)
                                    .observeOn(Schedulers.io())
                                    .map(success -> {
                                        File file = new File(picture);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                        return true;
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(success -> deletePictureSuccess(picture, position), throwable -> deletePictureFail(throwable));
                        })
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
                return true;
            }
        });
        mRecyclerView.setAdapter(mDownloadAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void deletePictureSuccess(String picture, int position) {
        mDownloadAdapter.deletePicture(picture);
        mDownloadAdapter.notifyItemRemoved(position);
        mDeleteDialog.dismiss();
    }

    private void deletePictureFail(Throwable throwable) {
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.download_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
        // do nothing
    }

    @Override
    protected Observable<List<String>> onRefresh() {
        File path = DownloadManager.getInstance().getDownloadPath();

        if (path == null) {
            return Observable.just(Collections.EMPTY_LIST);
        }

        List<String> pictures = new ArrayList<>();
        for (File file : path.listFiles()) {
            pictures.add(file.getPath());
        }

        return Observable.just(pictures);
    }

    @Override
    protected Observable<List<String>> onLoadMore() {
        List<String> list = new ArrayList<>();

        int b = Math.min(mPictures.size(), (mCurrentPageNo + 1) * PAGE_SIZE);
        for (int i = mCurrentPageNo * PAGE_SIZE; i < b; i++) {
            list.add(mPictures.get(i));
        }

        return Observable.just(list);
    }

    @Override
    protected void onRefreshFinish(List<String> data) {
        mPictures = data;

        List<String> list = new ArrayList<>();
        int b = Math.min(data.size(), PAGE_SIZE);
        for (int i = 0; i < b; i++) {
            list.add(data.get(i));
        }

        mCurrentPageNo = 0;
        mDownloadAdapter.notifyItemRangeRemoved(0, mDownloadAdapter.getItemCount());
        mDownloadAdapter.refresh(list);
        mDownloadAdapter.notifyItemRangeInserted(0, list.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<String> data) {
        if (data.isEmpty()) {
            return true;
        }


        mCurrentPageNo++;
        mDownloadAdapter.notifyItemRangeInserted(mDownloadAdapter.getItemCount(), data.size());
        mDownloadAdapter.insertPictures(data);
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
        // do nothing
    }
}
