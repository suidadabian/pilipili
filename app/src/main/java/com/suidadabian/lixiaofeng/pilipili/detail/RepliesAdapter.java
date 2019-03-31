package com.suidadabian.lixiaofeng.pilipili.detail;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.Comment;
import com.suidadabian.lixiaofeng.pilipili.model.Reply;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RepliesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_COMMENT = 1;
    private static final int TYPE_REPLY = 2;
    private WeakReference<Fragment> mFragmentRef;
    private Comment mComment;
    private List<Reply> mReplies;

    public RepliesAdapter(Fragment fragment) {
        mFragmentRef = new WeakReference<>(fragment);
        mReplies = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_COMMENT : TYPE_REPLY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        switch (viewType) {
            case TYPE_COMMENT: {
                holder = new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
                break;
            }
            case TYPE_REPLY: {
                holder = new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false));
                break;
            }
            default: {
                break;
            }
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_COMMENT: {
                bindCommentViewHolder((CommentViewHolder) holder, position);
                break;
            }
            case TYPE_REPLY: {
                bindReplyViewHolder((ReplyViewHolder) holder, position - 1);
                break;
            }
        }
    }

    private void bindCommentViewHolder(CommentViewHolder holder, int position) {
        if (mFragmentRef.get() == null) {
            return;
        }

        if (mComment == null) {
            return;
        } else {
            holder.commentTv.setText(mComment.getComment());
            holder.dateTv.setText(DateUtil.getDateString(mComment.getDate()));
            holder.replyNumTv.setText(holder.itemView.getContext().getString(R.string.comment_reply_num_text, mComment.getReplyNum()));
            holder.itemView.setTag(position);
            PiliPiliServer.getInstance().getUser(mComment.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        Fragment fragment = mFragmentRef.get();

                        if (fragment == null || (int) holder.itemView.getTag() != position) {
                            return;
                        }

                        holder.usernameTv.setText(user.getName());
                        GlideApp.with(fragment)
                                .load(user.getAvatarUrl())
                                .placeholder(R.drawable.icon_placeholder)
                                .error(R.drawable.icon_error)
                                .into(holder.avatarIv);
                    });
        }
    }

    private void bindReplyViewHolder(ReplyViewHolder holder, int position) {
        Reply reply = mReplies.get(position);

        if (reply == null || mFragmentRef.get() == null) {
            return;
        }

        holder.replyTv.setText(reply.getReply());
        holder.dateTv.setText(DateUtil.getDateString(reply.getDate()));
        holder.itemView.setTag(position);
        PiliPiliServer.getInstance().getUser(reply.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Fragment fragment = mFragmentRef.get();

                    if (fragment == null || (int) holder.itemView.getTag() != position) {
                        return;
                    }

                    holder.usernameTv.setText(user.getName());
                    GlideApp.with(fragment)
                            .load(user.getAvatarUrl())
                            .placeholder(R.drawable.icon_placeholder)
                            .error(R.drawable.icon_error)
                            .into(holder.avatarIv);
                });
    }

    public void refresh(Comment comment) {
        if (comment == null) {
            return;
        }

        mComment = comment;
    }

    public void refresh(List<Reply> replies) {
        if (replies == null) {
            return;
        }

        mReplies.clear();
        mReplies.addAll(replies);
    }

    public void insertReplies(List<Reply> replies) {
        if (replies == null) {
            return;
        }

        mReplies.addAll(replies);
    }

    public void insertReply(Reply reply) {
        if (reply == null) {
            return;
        }

        mReplies.add(0, reply);
    }

    @Override
    public int getItemCount() {
        return mReplies == null ? 1 : mReplies.size() + 1;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIv;
        TextView usernameTv;
        TextView dateTv;
        TextView commentTv;
        TextView replyNumTv;

        public CommentViewHolder(View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.comment_avatar_civ);
            usernameTv = itemView.findViewById(R.id.comment_username_tv);
            dateTv = itemView.findViewById(R.id.comment_date_tv);
            commentTv = itemView.findViewById(R.id.comment_tv);
            replyNumTv = itemView.findViewById(R.id.comment_reply_num_tv);
        }
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIv;
        TextView usernameTv;
        TextView dateTv;
        TextView replyTv;

        public ReplyViewHolder(View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.reply_avatar_civ);
            usernameTv = itemView.findViewById(R.id.reply_username_tv);
            dateTv = itemView.findViewById(R.id.reply_date_tv);
            replyTv = itemView.findViewById(R.id.reply_tv);
        }
    }
}
