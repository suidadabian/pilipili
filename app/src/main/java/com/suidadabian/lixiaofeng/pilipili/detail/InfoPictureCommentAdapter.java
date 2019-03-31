package com.suidadabian.lixiaofeng.pilipili.detail;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.Comment;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoPictureCommentAdapter extends RecyclerView.Adapter<InfoPictureCommentAdapter.CommentViewHolder> {
    private WeakReference<Fragment> mFragmentWeakRef;
    private List<Comment> mComments;
    private OnItemClickListener mOnItemClickListener;

    public InfoPictureCommentAdapter(Fragment fragment) {
        mFragmentWeakRef = new WeakReference<>(fragment);
        mComments = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentViewHolder holder = new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_picture_comment, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mComments.get(holder.getAdapterPosition()));
            }
        });
        holder.avatarCiv.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onUserClick(mComments.get(holder.getAdapterPosition()).getUserId());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);

        if (mFragmentWeakRef.get() == null || comment == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.dateTv.setText(DateUtil.getDateString(comment.getDate()));
        holder.replyNumTv.setText(holder.itemView.getContext().getString(R.string.comment_reply_num_text, comment.getReplyNum()));
        holder.commentTv.setText(comment.getComment());
        PiliPiliServer.getInstance().getUser(comment.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Fragment fragment = mFragmentWeakRef.get();

                    if (fragment == null || (int) holder.itemView.getTag() != position) {
                        return;
                    }

                    holder.usernameTv.setText(user.getName());
                    GlideApp.with(fragment)
                            .load(user.getAvatarUrl())
                            .placeholder(R.drawable.icon_placeholder)
                            .error(R.drawable.icon_error)
                            .into(holder.avatarCiv);
                });
    }

    @Override
    public int getItemCount() {
        return mComments == null ? 0 : mComments.size();
    }

    public void refresh(List<Comment> comments) {
        if (comments == null) {
            return;
        }

        mComments.clear();
        mComments.addAll(comments);
    }

    public void insertComments(List<Comment> comments) {
        if (comments == null) {
            return;
        }

        mComments.addAll(comments);
    }

    public void insertComment(Comment comment) {
        if (comment == null) {
            return;
        }

        mComments.add(0, comment);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarCiv;
        TextView usernameTv;
        TextView indexTv;
        TextView dateTv;
        TextView replyNumTv;
        TextView commentTv;

        public CommentViewHolder(View itemView) {
            super(itemView);

            avatarCiv = itemView.findViewById(R.id.info_picture_comment_avatar_civ);
            usernameTv = itemView.findViewById(R.id.info_picture_comment_username_tv);
            dateTv = itemView.findViewById(R.id.info_picture_comment_date_tv);
            replyNumTv = itemView.findViewById(R.id.info_picture_comment_reply_num_tv);
            commentTv = itemView.findViewById(R.id.info_picture_comment_tv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Comment comment);

        void onUserClick(long userId);
    }
}
