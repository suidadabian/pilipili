package com.suidadabian.lixiaofeng.pilipili.user;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserInfoPictureAdapter extends RecyclerView.Adapter<UserInfoPictureAdapter.UserInfoPictureViewHolder> {
    private WeakReference<Fragment> mFragmentRef;
    private List<InfoPicture> mInfoPictures;
    private OnItemClickListener mOnItemClickListener;
    private boolean self;

    public UserInfoPictureAdapter(Fragment fragment, boolean self) {
        mFragmentRef = new WeakReference<>(fragment);
        mInfoPictures = new ArrayList<>();
        this.self = self;
    }

    @Override
    public UserInfoPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserInfoPictureViewHolder holder = new UserInfoPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_picture, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mInfoPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.deleteIbtn.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemDelete(mInfoPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(UserInfoPictureViewHolder holder, int position) {
        Fragment fragment = mFragmentRef.get();
        InfoPicture infoPicture = mInfoPictures.get(position);

        if (fragment == null || infoPicture == null) {
            return;
        }

        holder.shareTv.setVisibility(self ? View.VISIBLE : View.GONE);
        holder.deleteIbtn.setVisibility(self ? View.VISIBLE : View.GONE);

        holder.titleTv.setText(infoPicture.getTitle());
        holder.dateTv.setText(DateUtil.getDateString(infoPicture.getDate(), DateUtil.YYYY_MM_DD));
        holder.shareTv.setText(infoPicture.isShare() ? "分享" : "隐私");
        GlideApp.with(fragment)
                .load(infoPicture.getUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.pictureIv);
    }

    @Override
    public int getItemCount() {
        return mInfoPictures == null ? 0 : mInfoPictures.size();
    }

    public void refresh(List<InfoPicture> infoPictures) {
        if (infoPictures == null) {
            return;
        }

        mInfoPictures.clear();
        mInfoPictures.addAll(infoPictures);
    }

    public void insertInfoPictures(List<InfoPicture> infoPictures) {
        if (infoPictures == null) {
            return;
        }

        mInfoPictures.addAll(infoPictures);
    }

    public void removeInfoPictures(InfoPicture infoPicture) {
        if (infoPicture == null) {
            return;
        }

        mInfoPictures.remove(infoPicture);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class UserInfoPictureViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;
        TextView titleTv;
        TextView dateTv;
        TextView shareTv;
        ImageButton deleteIbtn;

        public UserInfoPictureViewHolder(View itemView) {
            super(itemView);

            pictureIv = itemView.findViewById(R.id.user_info_picture_iv);
            titleTv = itemView.findViewById(R.id.user_info_picture_title_tv);
            dateTv = itemView.findViewById(R.id.user_info_picture_date_tv);
            shareTv = itemView.findViewById(R.id.user_info_picture_share_tv);
            deleteIbtn = itemView.findViewById(R.id.user_info_picture_delete_ibtn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(InfoPicture infoPicture, int position);

        void onItemDelete(InfoPicture infoPicture, int position);
    }
}
