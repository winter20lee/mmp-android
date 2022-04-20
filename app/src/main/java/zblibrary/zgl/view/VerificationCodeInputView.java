package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zuo.biao.library.util.StringUtil;

public class VerificationCodeInputView extends LinearLayout {
    private static final String TAG = "VerificationCodeInput";
    private int box = 4;
    private int childLayoutId = 0;
    private int childedImg = 0;
    private int childEditId = 0;
    boolean isPassWord = false;
    int marginRight = 15;
    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;
    private OnChangeTextListener onChangeTextListener;
    private Listener listener;


    public interface Listener {
        void onComplete(String content);
    }

    public interface OnChangeTextListener {
        void onChangeText(String content);
    }


    /**
     * 监听文本输入内容
     * @param mOnChangeTextListener
     */
    public void setOnChangeTextListener( OnChangeTextListener mOnChangeTextListener) {
        this.onChangeTextListener = mOnChangeTextListener;
    }

    /**
     * 监听输入完成
     * @param listener
     */
    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }


    public VerificationCodeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        marginRight = StringUtil.dp2px(getContext(), 14);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeInput);
        box = a.getInt(R.styleable.vericationCodeInput_inputlength, 4);
        childLayoutId = a.getResourceId(R.styleable.vericationCodeInput_child_layout, R.layout.input_code_view);
        childEditId = a.getResourceId(R.styleable.vericationCodeInput_child_edit_id, R.id.ed_inputcode);
        childedImg = a.getResourceId(R.styleable.vericationCodeInput_child_img_id, R.id.ed_img);
        isPassWord = a.getBoolean(R.styleable.vericationCodeInput_ispwd, false);
        setOrientation(HORIZONTAL);
        initViews();

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    private void initViews() {
        final OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText editText = (EditText) v;
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!TextUtils.isEmpty(editText.getText())) {
                        editText.setSelection(1);
                    }
                    backFocus(editText);
                }
                return false;
            }
        };


        for (int i = 0; i < box; i++) {
            View etCodeLayout = LayoutInflater.from(getContext()).inflate(childLayoutId, null);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            layoutParams.rightMargin = marginRight;
            final EditText etCode = etCodeLayout.findViewById(childEditId);
            final ImageView edImg = etCodeLayout.findViewById(childedImg);
            etCode.setId(i);
            etCodeLayout.setLayoutParams(layoutParams);
            if (isPassWord) {
                etCode.setTextColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            }
            if (i == 0) {
                etCode.requestFocus();
                etCode.setSelected(true);
            }

            etCode.setFilters(new InputFilter[]{new LengthFilter(1)});
            etCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        if (isPassWord) {
                            edImg.setVisibility(GONE);
                        }
                    } else {
                        if (isPassWord) {
                            edImg.setVisibility(GONE);
                        }
                        focus();
                        checkAndCommit();
                    }
                }

            });
            etCode.setOnKeyListener(onKeyListener);
            addView(etCodeLayout, i);
            mEditTextList.add(etCode);
        }


    }

    public EditText getFirstEtCode() {
        return mEditTextList.size() > 0 ? mEditTextList.get(0) : null;
    }

    @SuppressLint("ResourceType")
    public void backFocus(EditText backEditText) {
        int count = getChildCount();
        EditText etCode;
        setGrayLine();
        for (int i = count - 1; i >= 0; i--) {
            etCode = (EditText) getChildAt(i).findViewById(i);
            if (etCode.getText().length() == 1) {
                etCode.requestFocus();
                etCode.setSelected(true);
                if (backEditText.getId() < etCode.getId()){
                    etCode.setSelection(1);
                }else {
                    etCode.setText("");
                }
                return;
            }
            if (i == 0) {
                etCode.requestFocus();
                etCode.setSelection(0);
                etCode.setSelected(true);
            }
        }
    }

    public void setGrayLine() {
        int count = getChildCount();
        EditText etCode;
        for (int i = 0; i < count; i++) {
            etCode = (EditText) getChildAt(i).findViewById(i);
            etCode.setSelected(false);
        }
    }

    public void clearText() {
        int count = getChildCount();
        EditText etCode;
        for (int i = count - 1; i >= 0; i--) {
            etCode = (EditText) getChildAt(i).findViewById(i);
            if (etCode.getText().length() == 1) {
                etCode.setText("");
            }
        }
        etCode = (EditText) getChildAt(0).findViewById(0);
        etCode.requestFocus();
    }

    private void focus() {
        int count = getChildCount();
        EditText etCode;
        setGrayLine();
        for (int i = 0; i < count; i++) {
            etCode = (EditText) getChildAt(i).findViewById(i);
            if (etCode.getText().length() < 1) {
                etCode.requestFocus();
                etCode.setCursorVisible(true);
                etCode.setSelection(0);
                etCode.setSelected(true);
                return;
            }
        }
    }

    public String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText etCode = (EditText) getChildAt(i).findViewById(i);
            String content = etCode.getText().toString();
            if (content.length() != 0) {
                stringBuilder.append(content);
            }

        }
        return stringBuilder.toString();
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText etCode = getChildAt(i).findViewById(i);
            String content = etCode.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (onChangeTextListener != null) {
            onChangeTextListener.onChangeText(stringBuilder.toString());
        }
        Log.d(TAG, "checkAndCommit:" + stringBuilder.toString());
        if (full) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
                //设置最后位置选中
                int lastIndex = 5;
                EditText etCode = getChildAt(lastIndex).findViewById(lastIndex);
                etCode.requestFocus();
                etCode.setSelected(true);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void updateNextViewText(CharSequence source) {
        int count = getChildCount();
        EditText etCode;
        for (int i = 0; i < count; i++) {
            etCode = (EditText) getChildAt(i).findViewById(i);
            if (etCode.getText().length() < 1) {
                etCode.setText(source);
                etCode.requestFocus();
                return;
            }
        }
    }

    /**
     * This filter will constrain edits not to make the length of the text
     * greater than the specified length.
     */
    public class LengthFilter implements InputFilter {
        private final int mMax;

        public LengthFilter(int max) {
            mMax = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                updateNextViewText(source);
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }
    }
}

