package com.suidadabian.lixiaofeng.pilipili.main;

import android.content.res.Resources;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LightPicturesAdapter extends RecyclerView.Adapter<LightPicturesAdapter.LightPictureViewHolder> {
    private WeakReference<Fragment> mFragmentWeakRef;
    private List<LightPicture> mLightPictures;
    private OnItemClickListener mOnItemClickListener;

    public LightPicturesAdapter(Fragment fragment) {
        mFragmentWeakRef = new WeakReference<>(fragment);
        mLightPictures = new ArrayList<>();
    }

    @Override
    public LightPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightPicturesAdapter.LightPictureViewHolder holder = new LightPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_light_picture, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mLightPictures.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(LightPictureViewHolder holder, int position) {
        Fragment fragment = mFragmentWeakRef.get();
        LightPicture lightPicture = mLightPictures.get(position);

        if (fragment == null || lightPicture == null) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = holder.pictureIv.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth() / 2;
        layoutParams.height = (int) (layoutParams.width * lightPicture.getScale());

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

    public void refresh(List<LightPicture> data) {
        if (data == null) {
            return;
        }

        mLightPictures.clear();
        mLightPictures.addAll(data);
    }

    public void insertLightPicture(List<LightPicture> lightPictures) {
        if (lightPictures == null) {
            return;
        }

        mLightPictures.addAll(lightPictures);
    }

    public void setOnItemClickListener(LightPicturesAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class LightPictureViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;

        public LightPictureViewHolder(View itemView) {
            super(itemView);

            pictureIv = (ImageView) itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LightPicture lightPicture);
    }
}