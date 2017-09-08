package com.hafidzquran.rumahquran.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RuquEditText extends android.support.v7.widget.AppCompatEditText {

    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public RuquEditText(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public RuquEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        init();
    }

    public RuquEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.attrs=attrs;
        this.defStyle=defStyle;
        init();
    }

    private void init() {
        Typeface font=Typeface.createFromAsset(getContext().getAssets(), "font/Family&Friends.ttf");
        this.setTypeface(font);
    }
    @Override
    public void setTypeface(Typeface tf, int style) {
        tf=Typeface.createFromAsset(getContext().getAssets(), "font/Family&Friends.ttf");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf=Typeface.createFromAsset(getContext().getAssets(), "font/Family&Friends.ttf");
        super.setTypeface(tf);
    }
}
