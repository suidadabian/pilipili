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
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.util.DateUtil;
import com.suidadabian.lixiaofeng.pilipili.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserLightPictureAdapter extends RecyclerView.Adapter<UserLightPictureAdapter.UserLightPictureViewHolder> {
    private WeakReference<Fragment> mFragmentRef;
    private List<LightPicture> mLightPictures;
    private boolean self;
    private OnItemClickListener mOnItemClickListener;

    public UserLightPictureAdapter(Fragment fragment, boolean self) {
        mFragmentRef = new WeakReference<>(fragment);
        mLightPictures = new ArrayList<>();
        this.self = self;
    }

    @Override
    public UserLightPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserLightPictureViewHolder holder = new UserLightPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_light_picture, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mLightPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.deleteIbtn.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemDelete(mLightPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(UserLightPictureViewHolder holder, int position) {
        Fragment fragment = mFragmentRef.get();
        LightPicture lightPicture = mLightPictures.get(position);

        if (fragment == null || lightPicture == null) {
            return;
        }

        holder.shareTv.setVisibility(self ? View.VISIBLE : View.GONE);
        holder.deleteIbtn.setVisibility(self ? View.VISIBLE : View.GONE);

        ViewGroup.LayoutParams layoutParams = holder.pictureIv.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth() / 2;
        layoutParams.height = (int) (layoutParams.width * lightPicture.getScale());
        holder.dateTv.setText(DateUtil.getDateString(lightPicture.getDate(), DateUtil.YYYY_MM_DD));
        holder.shareTv.setText(lightPicture.isShare() ? "分享" : "隐私");
        GlideApp.with(fragment)
                .load(lightPicture.getUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.pictureIv);
    }

    @Override
    public int getItemCount() {
        return mLightPictures == null ? 0 : mLightPictures.size();
    }

    public void refresh(List<LightPicture> lightPictures) {
        if (lightPictures == null) {
            return;
        }

        mLightPictures.clear();
        mLightPictures.addAll(lightPictures);
    }

    public void insertLightPictures(List<LightPicture> lightPictures) {
        if (lightPictures == null) {
            return;
        }

        mLightPictures.addAll(lightPictures);
    }

    public void deleteLightPicture(LightPicture lightPicture) {
        if (lightPicture == null) {
            return;
        }

        mLightPictures.remove(lightPicture);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class UserLightPictureViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;
        TextView dateTv;
        TextView shareTv;
        ImageButton deleteIbtn;

        public UserLightPictureViewHolder(View itemView) {
            super(itemView);

            pictureIv = itemView.findViewById(R.id.user_light_picture_iv);
            shareTv = itemView.findViewById(R.id.user_light_picture_share_tv);
            dateTv = itemView.findViewById(R.id.user_light_picture_date_tv);
            deleteIbtn = itemView.findViewById(R.id.user_light_picture_delete_ibtn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LightPicture lightPicture, int position);

        void onItemDelete(LightPicture lightPicture, int position);
    }
}
