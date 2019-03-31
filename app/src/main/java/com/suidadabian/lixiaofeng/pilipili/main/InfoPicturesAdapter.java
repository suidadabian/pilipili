package com.suidadabian.lixiaofeng.pilipili.main;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class InfoPicturesAdapter extends RecyclerView.Adapter<InfoPicturesAdapter.InfoPictureViewHolder> {
    private WeakReference<Fragment> mFragmentWeekRef;
    private List<InfoPicture> mInfoPictures;
    private InfoPicturesAdapter.OnItemClickListener mOnItemClickListener;

    public InfoPicturesAdapter(Fragment fragment) {
        mFragmentWeekRef = new WeakReference<>(fragment);
        mInfoPictures = new ArrayList<>();
    }

    @Override
    public InfoPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InfoPictureViewHolder holder = new InfoPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_picture, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItmClick(mInfoPictures.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(InfoPictureViewHolder holder, int position) {
        Fragment fragment = mFragmentWeekRef.get();
        InfoPicture infoPicture = mInfoPictures.get(position);

        if (fragment == null || infoPicture == null) {
            return;
        }

        GlideApp.with(fragment)
                .load(infoPicture.getUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.pictureIv);
        holder.titleTv.setText(infoPicture.getTitle());
    }

    @Override
    public int getItemCount() {
        return mInfoPictures == null ? 0 : mInfoPictures.size();
    }

    public void setOnItemClickListener(InfoPicturesAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class InfoPictureViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;
        TextView titleTv;

        public InfoPictureViewHolder(View itemView) {
            super(itemView);

            pictureIv = itemView.findViewById(R.id.info_picture_iv);
            titleTv = itemView.findViewById(R.id.info_picture_tv);
        }
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

    public interface OnItemClickListener {
        void onItmClick(InfoPicture infoPicture);
    }
}