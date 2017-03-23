package com.lqr.articlepatch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.lqr.articlepatch.R;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;
import com.zhy.autolayout.utils.DimenUtils;

import java.lang.reflect.Field;

/**
 * Created by hupei on 2015/12/28 20:33.
 */
public class AutoToolbar extends Toolbar {
    private static final int NO_VALID = -1;
    private int mTextSize;
    private int mSubTextSize;
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Toolbar,
                defStyleAttr, R.style.Widget_AppCompat_Toolbar);

        int titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance,
                R.style.TextAppearance_Widget_AppCompat_Toolbar_Title);

        int subtitleTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance,
                R.style.TextAppearance_Widget_AppCompat_Toolbar_Subtitle);

        mTextSize = loadTextSizeFromTextAppearance(titleTextAppearance);
        mSubTextSize = loadTextSizeFromTextAppearance(subtitleTextAppearance);

//        TypedArray menuA = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Theme,
//                defStyleAttr, R.style.ThemeOverlay_AppCompat);
//        int menuTextAppearance = menuA.getResourceId(R.styleable.Theme_actionBarTheme,
//                R.style.ThemeOverlay_AppCompat_ActionBar);
//        int menuTextSize = loadTextSizeFromTextAppearance(menuTextAppearance);

        //防止 menu 定义 textSize，而 Toolbar 无定义 textSize 时，title 的 textSize 随 menu 变化
//        if (mTextSize == NO_VALID) mTextSize = menuTextSize;
//        if (mSubTextSize == NO_VALID) mSubTextSize = menuTextSize;

        a.recycle();
//        menuA.recycle();
    }

    public AutoToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoToolbar(Context context) {
        this(context, null);
    }

    private int loadTextSizeFromTextAppearance(int textAppearanceResId) {
        TypedArray a = getContext().obtainStyledAttributes(textAppearanceResId,
                R.styleable.TextAppearance);
        try {
            if (!DimenUtils.isPxVal(a.peekValue(R.styleable.TextAppearance_android_textSize)))
                return NO_VALID;
            return a.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, NO_VALID);
        } finally {
            a.recycle();
        }
    }

    private void setUpTitleTextSize() {
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title) && mTextSize != NO_VALID)
            setUpTitleTextSize("mTitleTextView", mTextSize);
        CharSequence subtitle = getSubtitle();
        if (!TextUtils.isEmpty(subtitle) && mSubTextSize != NO_VALID)
            setUpTitleTextSize("mSubtitleTextView", mSubTextSize);
    }

    private void setUpTitleTextSize(String name, int val) {
        try {
            //反射 Toolbar 的 TextView
            Field f = getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            TextView textView = (TextView) f.get(this);
            if (textView != null) {
                int autoTextSize = AutoUtils.getPercentHeightSize(val);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoTextSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.isInEditMode()) {
            setUpTitleTextSize();
            this.mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    public static class LayoutParams extends Toolbar.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mDimenLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mDimenLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return this.mDimenLayoutInfo;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}