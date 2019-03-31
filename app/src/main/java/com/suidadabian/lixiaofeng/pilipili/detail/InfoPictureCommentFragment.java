package com.suidadabian.lixiaofeng.pilipili.detail;

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
import com.suidadabian.lixiaofeng.pilipili.model.Comment;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.user.UserActivity;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoPictureCommentFragment extends RefreshLoadFragment<Comment> {
    private static final String TAG = InfoPictureCommentFragment.class.getSimpleName();
    private static final String kEY_INFO_PICTURE_ID = "info_picture_id";
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;
    private long mInfoPictureId;
    private RecyclerView mRecyclerView;
    private InfoPictureCommentAdapter mInfoPictureCommentAdapter;
    private int mCurrentPageNo;
    private MaterialDialog mSendDialog;

    public static InfoPictureCommentFragment newInstance(long infoPictureId) {
        InfoPictureCommentFragment fragment = new InfoPictureCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(kEY_INFO_PICTURE_ID, infoPictureId);
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
        View view = inflater.inflate(R.layout.fragment_info_picture_comment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.info_picture_comment_rv);

        mInfoPictureCommentAdapter = new InfoPictureCommentAdapter(this);
        mInfoPictureCommentAdapter.setOnItemClickListener(new InfoPictureCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comment comment) {
                Intent intent = new Intent(getContext(), RepliesActivity.class);
                intent.putExtra(RepliesActivity.KEY_COMMENT_ID, comment.getId());
                startActivity(intent);
            }

            @Override
            public void onUserClick(long userId) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra(UserActivity.KEY_USER_ID, userId);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mInfoPictureCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        mInfoPictureId = getArguments().getLong(kEY_INFO_PICTURE_ID);
    }

    public void sendComment(String commentText) {
        if (!CheckUtil.checkComment(commentText)) {
            sendCommentInvalid();
        }

        if (mSendDialog == null) {
            mSendDialog = new MaterialDialog.Builder(getContext())
                    .content("发表评论中，请稍候")
                    .progress(true, 0)
                    .show();
        } else {
            mSendDialog.show();
        }

        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setUserId(UserManager.getInstance().getUser().getId());
        comment.setInfoPictureId(mInfoPictureId);
        PiliPiliServer.getInstance().sendComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxComment -> sendCommentSuccess(rxComment), throwable -> sendCommentFail(throwable));
    }

    private void sendCommentSuccess(Comment comment) {
        mSendDialog.dismiss();
        mInfoPictureCommentAdapter.insertComment(comment);
        mInfoPictureCommentAdapter.notifyItemInserted(0);
    }

    private void sendCommentFail(Throwable throwable) {
        mSendDialog.dismiss();
        new NetExceptionHandler().handleException(getContext(), throwable);
    }

    private void sendCommentInvalid() {
        Toast.makeText(getContext(), "评论无效", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected int getRefreshLayoutId() {
        return R.id.info_picture_comment_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
    }

    @Override
    protected Observable<List<Comment>> onRefresh() {
        return PiliPiliServer.getInstance().getComments(mInfoPictureId, START_PAGE_NO, PAGE_SIZE);
    }

    @Override
    protected Observable<List<Comment>> onLoadMore() {
        return PiliPiliServer.getInstance().getComments(mInfoPictureId, mCurrentPageNo + 1, PAGE_SIZE);
    }

    @Override
    protected void onRefreshFinish(List<Comment> data) {
        mCurrentPageNo = START_PAGE_NO;
        mInfoPictureCommentAdapter.notifyItemRangeRemoved(0, mInfoPictureCommentAdapter.getItemCount());
        mInfoPictureCommentAdapter.refresh(data);
        mInfoPictureCommentAdapter.notifyItemRangeInserted(0, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<Comment> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        mInfoPictureCommentAdapter.notifyItemRangeInserted(mInfoPictureCommentAdapter.getItemCount(), data.size());
        mInfoPictureCommentAdapter.insertComments(data);
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}
