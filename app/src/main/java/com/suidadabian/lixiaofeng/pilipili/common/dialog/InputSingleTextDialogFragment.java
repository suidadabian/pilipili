package com.suidadabian.lixiaofeng.pilipili.common.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.suidadabian.lixiaofeng.pilipili.R;

public class InputSingleTextDialogFragment extends AppCompatDialogFragment {
    private EditText mEditText;
    private String mTitle;
    private String mText;
    private OnActionListener mOnActionListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_single_text, null);
        mEditText = view.findViewById(R.id.dialog_input_single_et);
        mEditText.setText(mText);
        return new AlertDialog.Builder(getContext())
                .setTitle(mTitle)
                .setView(view)
                .setPositiveButton("确定", (d, which) -> {
                    if (mOnActionListener != null) {
                        mOnActionListener.onPositive(mEditText.getText().toString());
                    }
                })
                .setNegativeButton("取消", (d, which) -> {
                    if (mOnActionListener != null) {
                        mOnActionListener.onNegative();
                    }
                })
                .create();
    }

    public void setTitle(String title) {
        this.mTitle = title;
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setTitle(title);
        }
    }

    public void setText(String text) {
        this.mText = text;
        if (mEditText != null) {
            mEditText.setText(text);
        }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
    }

    public interface OnActionListener {
        void onPositive(String text);

        void onNegative();
    }
}
