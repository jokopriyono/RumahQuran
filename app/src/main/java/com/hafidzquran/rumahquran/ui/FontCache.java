package com.hafidzquran.rumahquran.ui;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    static Typeface getTypeface(String font, Context context) {
        Typeface typeface = fontCache.get(font);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), font);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(font, typeface);
        }

        return typeface;
    }
}