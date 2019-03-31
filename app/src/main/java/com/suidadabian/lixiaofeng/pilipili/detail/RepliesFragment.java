package com.suidadabian.lixiaofeng.pilipili.detail;

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
import com.suidadabian.lixiaofeng.pilipili.model.Reply;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RepliesFragment extends RefreshLoadFragment<Reply> {
    private static final int START_PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;
    private static final String KEY_COMMENT_ID = "comment_id";
    private RecyclerView mRecyclerView;
    private RepliesAdapter mRepliesAdapter;
    private int mCurrentPageNo;
    private long mCommentId;
    private MaterialDialog mSendDialog;

    public static RepliesFragment newInstance(long commentId) {
        RepliesFragment fragment = new RepliesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_COMMENT_ID, commentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mCommentId = getArguments().getLong(KEY_COMMENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_replies, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.replies_rv);

        mRepliesAdapter = new RepliesAdapter(this);
        mRecyclerView.setAdapter(mRepliesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void sendReply(String replyText) {
        if (!CheckUtil.checkReply(replyText)) {
            sendReplyInvalid();
        }

        if (mSendDialog == null) {
            mSendDialog = new MaterialDialog.Builder(getContext())
                    .content("回复评论中，请稍候")
                    .progress(true, 0)
                    .show();
        } else {
            mSendDialog.show();
        }

        Reply reply = new Reply();
        reply.setReply(replyText);
        reply.setUserId(UserManager.getInstance().getUser().getId());
        reply.setCommentId(mCommentId);
        PiliPiliServer.getInstance().sendReply(reply)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxReply -> sendReplySuccess(rxReply), throwable -> sendReplyFail(throwable));
    }

    private void sendReplySuccess(Reply reply) {
        mSendDialog.dismiss();
        mRepliesAdapter.insertReply(reply);
        mRepliesAdapter.notifyItemInserted(0);
    }

    private void sendReplyFail(Throwable throwable) {
        mSendDialog.dismiss();
        new NetExceptionHandler().handleException(getContext(), throwable);
    }

    private void sendReplyInvalid() {
        Toast.makeText(getContext(), "回复失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.replies_refresh_layout;
    }

    @Override
    protected void onRefreshStart() {
    }

    @Override
    protected void onLoadMoreStart() {
    }

    @Override
    protected Observable<List<Reply>> onRefresh() {
        return PiliPiliServer.getInstance().getComment(getArguments().getLong(KEY_COMMENT_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(comment -> {
                    getCommentSuccess(comment);
                    return comment.getId();
                })
                .observeOn(Schedulers.io())
                .flatMap(commentId -> PiliPiliServer.getInstance().getReplies(commentId, START_PAGE_NO, PAGE_SIZE));
    }

    private void getCommentSuccess(Comment comment) {
        mRepliesAdapter.refresh(comment);
    }

    @Override
    protected Observable<List<Reply>> onLoadMore() {
        return PiliPiliServer.getInstance().getReplies(getArguments().getLong(KEY_COMMENT_ID), mCurrentPageNo + 1, PAGE_SIZE);
    }

    @Override
    protected void onRefreshFinish(List<Reply> data) {
        mCurrentPageNo = START_PAGE_NO;
        mRepliesAdapter.notifyItemChanged(0);
        mRepliesAdapter.notifyItemRangeRemoved(1, mRepliesAdapter.getItemCount());
        mRepliesAdapter.refresh(data);
        mRepliesAdapter.notifyItemRangeInserted(1, data.size());
    }

    @Override
    protected boolean onLoadMoreFinish(List<Reply> data) {
        if (data.isEmpty()) {
            return true;
        }

        mCurrentPageNo++;
        mRepliesAdapter.notifyItemRangeRemoved(1, mRepliesAdapter.getItemCount());
        mRepliesAdapter.insertReplies(data);
        mRepliesAdapter.notifyItemRangeInserted(1, data.size());
        return data.size() < PAGE_SIZE;
    }

    @Override
    protected void onRefreshFail(Throwable throwable) {
    }

    @Override
    protected void onLoadMoreFail(Throwable throwable) {
    }
}