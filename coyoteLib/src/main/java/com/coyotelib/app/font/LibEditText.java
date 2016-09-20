package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class LibEditText extends EditText {

	public LibEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initEdittext(context);
	}

	public LibEditText(Context context) {
		super(context);
		initEdittext(context);
	}

	public LibEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initEdittext(context);
	}

	void initEdittext(Context context) {
		try {

			Typeface typeface=FontConstance.getTypeface(context);
			if (typeface != null) {
				super.setTypeface(typeface);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
