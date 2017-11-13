package com.hafidzquran.rumahquran.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RuquTextview extends android.support.v7.widget.AppCompatTextView {

    public RuquTextview(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RuquTextview(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RuquTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/Family&Friends.ttf", context);
        setTypeface(customFont);
    }
}
