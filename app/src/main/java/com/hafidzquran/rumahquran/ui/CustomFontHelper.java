package com.hafidzquran.rumahquran.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hafidzquran.rumahquran.R;

class CustomFontHelper {
    static void setCustomFont(TextView textview, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        String font = a.getString(R.styleable.CustomFont_font);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    private static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = FontCache.getTypeface(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }

}