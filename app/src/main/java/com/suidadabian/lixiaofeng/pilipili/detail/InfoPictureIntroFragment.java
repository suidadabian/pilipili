package com.suidadabian.lixiaofeng.pilipili.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.Constant;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.DateUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoPictureIntroFragment extends Fragment {
    private static final String KEY_INFO_PICTURE_ID = "info_picture_id";
    private TextView mTitleTv;
    private TextView mIntroTv;
    private TextView mDateTv;
    private RecyclerView mRecommendRv;
    private RecommendAdapter mRecommendAdapter;
    private InfoPicture mInfoPicture;

    public static InfoPictureIntroFragment newInstance(long infoPictureId) {
        InfoPictureIntroFragment fragment = new InfoPictureIntroFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_INFO_PICTURE_ID, infoPictureId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_picture_intro, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mTitleTv = view.findViewById(R.id.info_picture_intro_title_tv);
        mIntroTv = view.findViewById(R.id.info_picture_intro_intro_tv);
        mDateTv = view.findViewById(R.id.info_picture_intro_date_tv);
        mRecommendRv = view.findViewById(R.id.info_picture_intro_recommend_rv);


        mRecommendAdapter = new RecommendAdapter(this);
        mRecommendAdapter.setOnItemClickListener(infoPicture -> {
            Intent intent = new Intent(getContext(), InfoPictureActivity.class);
            intent.putExtra(InfoPictureActivity.kEY_INFO_PICTURE_ID, infoPicture.getId());
            startActivity(intent);
        });
        mRecommendRv.setAdapter(mRecommendAdapter);
        mRecommendRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        PiliPiliServer.getInstance().getInfoPicture(getArguments().getLong(KEY_INFO_PICTURE_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(infoPicture -> {
                    getInfoPictureSuccess(infoPicture);
                    return infoPicture.getId();
                })
                .observeOn(Schedulers.io())
                .flatMap(infoPictureId -> PiliPiliServer.getInstance().getRecommendInfoPictures(infoPictureId))
                .observeOn(AndroidSchedulers.mainThread())
                .map(infoPictures -> {
                    getRecommendInfoPicturesSuccess(infoPictures);
                    return Constant.NULL;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> initDataSuccess(), throwable -> initDataFail(throwable));
    }

    private void getInfoPictureSuccess(InfoPicture infoPicture) {
        mInfoPicture = infoPicture;

        mTitleTv.setText(mInfoPicture.getTitle());
        mIntroTv.setText(mInfoPicture.getIntro());
        mDateTv.setText(DateUtil.getDateString(mInfoPicture.getDate()));
    }

    private void getRecommendInfoPicturesSuccess(List<InfoPicture> infoPictures) {
        mRecommendAdapter.notifyItemRangeRemoved(0, mRecommendAdapter.getItemCount());
        mRecommendAdapter.refresh(infoPictures);
        mRecommendAdapter.notifyItemRangeInserted(0, infoPictures.size());
    }

    private void initDataSuccess() {
    }

    private void initDataFail(Throwable throwable) {
        new NetExceptionHandler().handleException(getContext(), throwable);
    }
}
