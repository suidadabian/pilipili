package com.suidadabian.lixiaofeng.pilipili.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class RepliesActivity extends AppCompatActivity {
    public static final String KEY_COMMENT_ID = "comment_id";
    private LinearLayout mRootLinearLayout;
    private Toolbar mToolbar;
    private TextView mInputTextTv;
    private ImageButton mSendIbtn;
    private InputTextPopupWindow mInputTextPopupWindow;
    private RepliesFragment mRepliesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_replies);
        initView();
    }

    private void initView() {
        mRootLinearLayout = findViewById(R.id.replies_root_linear_layout);
        mToolbar = findViewById(R.id.replies_toolbar);
        mInputTextTv = findViewById(R.id.include_input_text_tv);
        mSendIbtn = findViewById(R.id.include_input_text_ibtn);

        mToolbar.setTitle(getResources().getString(R.string.replies_toolbar_title));
        mToolbar.setNavigationOnClickListener(v -> finish());

        mInputTextTv.setOnClickListener(v -> {
            if (mInputTextPopupWindow == null) {
                initInputTextPopupWindow();
            }

            mInputTextPopupWindow.showAtLocation(mRootLinearLayout, Gravity.BOTTOM, 0, 0);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.3f;
            getWindow().setAttributes(params);
        });
        mSendIbtn.setOnClickListener(v -> {
            if (mRepliesFragment != null) {
                mRepliesFragment.sendReply(mInputTextTv.getText().toString());
            }
        });

        mRepliesFragment = RepliesFragment.newInstance(getIntent().getLongExtra(KEY_COMMENT_ID, 0));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.replies_fl, mRepliesFragment)
                .commit();

    }

    private void initInputTextPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.popup_input_text, null);
        mInputTextPopupWindow = new InputTextPopupWindow(view, MATCH_PARENT, WRAP_CONTENT);
        mInputTextPopupWindow.setCallback(inputText -> {
            if (mRepliesFragment != null) {
                mRepliesFragment.sendReply(inputText);
            }
        });
        mInputTextPopupWindow.setOnDismissListener(() -> {
            mInputTextTv.setText(mInputTextPopupWindow.getInputText());
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        });
    }
}