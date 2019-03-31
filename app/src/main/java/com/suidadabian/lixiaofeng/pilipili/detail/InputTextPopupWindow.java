package com.suidadabian.lixiaofeng.pilipili.detail;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.suidadabian.lixiaofeng.pilipili.R;

public class InputTextPopupWindow extends PopupWindow {
    private EditText mInputEt;
    private ImageButton mSendIbtn;
    private Callback mCallback;

    public InputTextPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, true);
        mInputEt = contentView.findViewById(R.id.popup_input_text_et);
        mSendIbtn = contentView.findViewById(R.id.popup_input_text_ibtn);
        mSendIbtn.setOnClickListener(v -> {
            if (mCallback != null) {
                mCallback.onSend(mInputEt.getText().toString());
            }
        });
    }

    public String getInputText() {
        return mInputEt.getText().toString() ;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onSend(String inputText);
    }
}
