package com.suidadabian.lixiaofeng.pilipili.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.RefreshLoadFragment;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.watch.PictureWatchActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserLightPictureFragment extends RefreshLoadFragment<LightPicture> {
    private static final String KEY_USER_ID = "user_id";
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;
    private RecyclerView mRecyclerView;
    private UserLightPictureAdapter mUserLightPictureAdapter;
    private int mCurrentPageNo;
    private long mUserId;
    private boolean self;
    private MaterialDialog mDeleteDialog;

    public static UserLightPictureFragment newInstance(long userId) {
        UserLightPictureFragment fragment = new UserLightPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mUserId = getArguments().getLong(KEY_USER_ID);
        self = mUserId == UserManager.getInstance().getUser().getId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_light_picture, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.user_light_picture_rv);

        mUserLightPictureAdapter = new UserLightPictureAdapter(this, self);
        mUserLightPictureAdapter.setOnItemClickListener(new UserLightPictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LightPicture lightPicture, int position) {
                Intent intent = new Intent(getActivity(), PictureWatchActivity.class);
                intent.putExtra(PictureWatchActivity.KEY_PICTURE_URL, lightPicture.getUrl());
                intent.putExtra(PictureWatchActivity.KEY_FLAG_DOWNLOAD_ENABLE, true);
                startActivity(intent);
            }

            @Override
            public void onItemDelete(LightPicture lightPicture, int position) {
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("确定删除该轻图吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            deleteLightPicture(lightPicture, position);
                        })
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .show();

            }
        });
        mRecyclerView.setAdapter(mUserLightPictureAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void deleteLightPicture(LightPicture lightPicture, int position) {
        if (mDeleteDialog == null) {
            mDeleteDialog = new MaterialDialog.Builder(getContext())
                    .content("正在删除中，请稍候")
                    .progress(true, 0)
                    .show();
        } else {
            mDeleteDialog.show();
        }

        PiliPiliServer.getInstance().deleteLightPicture(UserManager.getInstance().getUser().getId(), lightPicture.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxLightPicture -> deleteLightPictureSuccess(lightPicture, position), throwable -> deleteLightPictureFail(throwable));
    }

    private void deleteLightPictureSuccess(LightPicture lightPicture, int position) {
        mDeleteDialog.dismiss();
        mUserLightPictureAdapter.deleteLightPicture(lightPicture);
        mUserLightPictureAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
    }

    private void deleteLightPictureFail(Throwable throwable) {
        mDeleteDialog.dismiss();
        new NetExceptionHandler().handleException(getContext(), throwable);
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.user_light_picture_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
    }

    @Override
    protected Observable<List<LightPicture>> onRefresh() {
        return PiliPiliServer.getInstance().getLightPictures(mUserId, START_PAGE_NO, PAGE_SIZE, self);
    }

    @Override
    protected Observable<List<LightPicture>> onLoadMore() {
        return PiliPiliServer.getInstance().getLightPictures(mUserId, mCurrentPageNo + 1, PAGE_SIZE, self);
    }

    @Override
    protected void onRefreshFinish(List<LightPicture> data) {
        mCurrentPageNo = START_PAGE_NO;
        mUserLightPictureAdapter.notifyItemRangeRemoved(0, mUserLightPictureAdapter.getItemCount());
        mUserLightPictureAdapter.refresh(data);
        mUserLightPictureAdapter.notifyItemRangeInserted(0, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<LightPicture> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        mUserLightPictureAdapter.notifyItemRangeInserted(mUserLightPictureAdapter.getItemCount(), data.size());
        mUserLightPictureAdapter.insertLightPictures(data);
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}
