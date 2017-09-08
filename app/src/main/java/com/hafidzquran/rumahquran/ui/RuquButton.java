package com.hafidzquran.rumahquran.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by LENOVO on 09/08/2017.
 */

public class RuquButton extends android.support.v7.widget.AppCompatButton {

    public RuquButton(Context context) {
        super(context);
    }
    public RuquButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
    public RuquButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}
