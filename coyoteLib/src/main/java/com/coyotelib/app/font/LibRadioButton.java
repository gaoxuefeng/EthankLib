package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class LibRadioButton extends RadioButton {

	public LibRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initEdittext(context);
	}

	public LibRadioButton(Context context) {
		super(context);
		initEdittext(context);
	}

	public LibRadioButton(Context context, AttributeSet attrs) {
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
