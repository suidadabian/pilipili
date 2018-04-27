package com.suidadabian.lixiaofeng.pilipili.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LightPicturesFragment extends PicturesFragment {
    private LightPicturesAdapter mLightPicturesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mLightPicturesAdapter = new LightPicturesAdapter(this);
        mLightPicturesAdapter.setOnItemClickListener(lightPicture -> {
            // TODO: 2018/4/25 更换逻辑
        });
        mRecyclerView.setAdapter(mLightPicturesAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        return view;
    }

    public static class LightPicturesAdapter extends RecyclerView.Adapter<LightPictureViewHolder> {
        private WeakReference<Fragment> mFragmentWeakRef;
        private List<LightPicture> mLightPictures;
        private OnItemClickListener mOnItemClickListener;

        public LightPicturesAdapter(Fragment fragment) {
            mFragmentWeakRef = new WeakReference<>(fragment);
            mLightPictures = new ArrayList<>();
        }

        @Override
        public LightPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LightPictureViewHolder holder = new LightPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_light_picture, parent, false));
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

            GlideApp.with(fragment)
                    .load(lightPicture.getUrl())
                    // TODO: 2018/4/25 更换图片
                    .placeholder(R.drawable.icon_preview)
                    .error(R.drawable.icon_preview)
                    .into(holder.pictureIv);
        }

        @Override
        public int getItemCount() {
            return mLightPictures == null ? 0 : mLightPictures.size();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }
    }

    private static class LightPictureViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;

        public LightPictureViewHolder(View itemView) {
            super(itemView);

            pictureIv = (ImageView) itemView;
        }
    }

    private interface OnItemClickListener {
        void onItemClick(LightPicture lightPicture);
    }
}
