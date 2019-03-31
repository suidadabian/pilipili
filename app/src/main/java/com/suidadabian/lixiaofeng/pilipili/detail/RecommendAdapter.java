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
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {
    private WeakReference<Fragment> mFragmentWeakRef;
    private List<InfoPicture> mInfoPictures;
    private OnItemClickListener mOnItemClickListener;

    public RecommendAdapter(Fragment fragment) {
        mFragmentWeakRef = new WeakReference<>(fragment);
        mInfoPictures = new ArrayList<>();
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecommendViewHolder holder = new RecommendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mInfoPictures.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        Fragment fragment = mFragmentWeakRef.get();
        InfoPicture infoPicture = mInfoPictures.get(position);

        if (fragment == null || infoPicture == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.titleTv.setText(infoPicture.getTitle());
        GlideApp.with(fragment)
                .load(infoPicture.getUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.pictureIv);
        PiliPiliServer.getInstance().getUser(infoPicture.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if ((int) holder.itemView.getTag() != position) {
                        return;
                    }

                    holder.usernameTv.setText(user.getName());
                });
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class RecommendViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;
        TextView titleTv;
        TextView usernameTv;

        public RecommendViewHolder(View itemView) {
            super(itemView);

            pictureIv = itemView.findViewById(R.id.recommend_picture_riv);
            titleTv = itemView.findViewById(R.id.recommend_title_tv);
            usernameTv = itemView.findViewById(R.id.recommend_username_tv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(InfoPicture infoPicture);
    }
}
