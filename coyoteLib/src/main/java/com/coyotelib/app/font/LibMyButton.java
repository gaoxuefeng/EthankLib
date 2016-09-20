package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class LibMyButton extends Button {

	public LibMyButton(Context context) {
		this(context, null);
	}

	public LibMyButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LibMyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try {

			Typeface typeface = FontConstance.getTypeface(context);
			if (typeface != null) {
				super.setTypeface(typeface);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
