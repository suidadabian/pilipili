package com.suidadabian.lixiaofeng.pilipili.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.RefreshLoadFragment;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.detail.InfoPictureActivity;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserInfoPictureFragment extends RefreshLoadFragment<InfoPicture> {
    public static final String KEY_USER_ID = "user_id";
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;
    private RecyclerView mRecyclerView;
    private UserInfoPictureAdapter mUserInfoPictureAdapter;
    private int mCurrentPageNo;
    private boolean self;
    private long mUserId;
    private MaterialDialog mDeleteDialog;

    public static UserInfoPictureFragment newInstance(long userId) {
        UserInfoPictureFragment fragment = new UserInfoPictureFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_picture, container, false);
        initView(view);
        return view;
    }

    private void initData() {
        mUserId = getArguments().getLong(KEY_USER_ID);
        self = UserManager.getInstance().getUser().getId() == mUserId;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.user_info_picture_rv);

        mUserInfoPictureAdapter = new UserInfoPictureAdapter(this, self);
        mUserInfoPictureAdapter.setOnItemClickListener(new UserInfoPictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(InfoPicture infoPicture, int position) {
                Intent intent = new Intent(getActivity(), InfoPictureActivity.class);
                intent.putExtra(InfoPictureActivity.kEY_INFO_PICTURE_ID, infoPicture.getId());
                startActivity(intent);
            }

            @Override
            public void onItemDelete(InfoPicture infoPicture, int position) {
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("确定删除该重图？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            deleteInfoPicture(infoPicture, position);
                        })
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        mRecyclerView.setAdapter(mUserInfoPictureAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void deleteInfoPicture(InfoPicture infoPicture, int position) {
        if (infoPicture == null) {
            return;
        }

        if (mDeleteDialog == null) {
            mDeleteDialog = new MaterialDialog.Builder(getContext())
                    .content("正在删除中，请稍后")
                    .progress(true, 0)
                    .show();
        } else {
            mDeleteDialog.show();
        }

        PiliPiliServer.getInstance().deleteInfoPicture(UserManager.getInstance().getUser().getId(), infoPicture.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxInfoPicture -> deleteInfoPictureSuccess(infoPicture, position), throwable -> deleteInfoPictureFail(throwable));
    }

    private void deleteInfoPictureSuccess(InfoPicture infoPicture, int position) {
        mDeleteDialog.dismiss();
        mUserInfoPictureAdapter.removeInfoPictures(infoPicture);
        mUserInfoPictureAdapter.notifyItemRemoved(position);
        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
    }

    private void deleteInfoPictureFail(Throwable throwable) {
        mDeleteDialog.dismiss();
        new NetExceptionHandler().handleException(getContext(), throwable);
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.user_info_picture_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
    }

    @Override
    protected Observable<List<InfoPicture>> onRefresh() {
        return PiliPiliServer.getInstance().getInfoPictures(mUserId, START_PAGE_NO, PAGE_SIZE, self);
    }

    @Override
    protected Observable<List<InfoPicture>> onLoadMore() {
        return PiliPiliServer.getInstance().getInfoPictures(mUserId, mCurrentPageNo + 1, PAGE_SIZE, self);
    }

    @Override
    protected void onRefreshFinish(List<InfoPicture> data) {
        mCurrentPageNo = START_PAGE_NO;
        mUserInfoPictureAdapter.notifyItemRangeRemoved(0, mUserInfoPictureAdapter.getItemCount());
        mUserInfoPictureAdapter.refresh(data);
        mUserInfoPictureAdapter.notifyItemRangeInserted(0, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<InfoPicture> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        mUserInfoPictureAdapter.notifyItemRangeInserted(mUserInfoPictureAdapter.getItemCount(), data.size());
        mUserInfoPictureAdapter.insertInfoPictures(data);
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}
