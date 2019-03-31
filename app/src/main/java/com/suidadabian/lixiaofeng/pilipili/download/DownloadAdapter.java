package com.suidadabian.lixiaofeng.pilipili.download;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.util.ScaleUtil;
import com.suidadabian.lixiaofeng.pilipili.util.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
    private WeakReference<Fragment> mFragmentRef;
    private List<String> mPictures;
    private Map<String, Float> mScaleMap;
    private OnItemClickListener mOnItemClickListener;

    public DownloadAdapter(Fragment fragment) {
        mFragmentRef = new WeakReference<>(fragment);
        mPictures = new ArrayList<>();
        mScaleMap = new ConcurrentHashMap<>();
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DownloadViewHolder holder = new DownloadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false));
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                return mOnItemClickListener.onItemLongClick(mPictures.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }

            return false;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {
        String picture = mPictures.get(position);

        if (picture == null) {
            return;
        }

        if (mScaleMap.containsKey(picture)) {
            displayPicture(holder, picture, mScaleMap.get(picture));
            return;
        }

        holder.itemView.setTag(position);
        Observable.just(picture)
                .observeOn(Schedulers.io())
                .map(rxPicture -> {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(rxPicture, options);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    return ScaleUtil.getScale(width, height);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scale -> {
                    if ((int) holder.itemView.getTag() != position) {
                        return;
                    }

                    displayPicture(holder, picture, scale);
                });
    }

    private void displayPicture(DownloadViewHolder holder, String picture, float scale) {
        Fragment fragment = mFragmentRef.get();

        if (fragment == null || holder == null || picture == null) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = holder.pictureIv.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth() / 2;
        layoutParams.height = (int) (layoutParams.width * scale);

        GlideApp.with(fragment)
                .load(picture)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.pictureIv);
    }

    @Override
    public int getItemCount() {
        return mPictures == null ? 0 : mPictures.size();
    }

    public void refresh(List<String> pictures) {
        if (pictures == null) {
            return;
        }

        mPictures.clear();
        mPictures.addAll(pictures);
    }

    public void deletePicture(String picture) {
        if (picture == null) {
            return;
        }

        mPictures.remove(picture);
        mScaleMap.remove(picture);
    }

    public void insertPictures(List<String> pictures) {
        if (pictures == null) {
            return;
        }

        mPictures.addAll(pictures);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class DownloadViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;

        public DownloadViewHolder(View itemView) {
            super(itemView);

            pictureIv = itemView.findViewById(R.id.download_picture_iv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String picture, int position);

        boolean onItemLongClick(String picture, int position);
    }
}