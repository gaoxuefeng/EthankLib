package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class LibButton extends Button {

	public LibButton(Context context) {
		this(context, null);
	}

	public LibButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LibButton(Context context, AttributeSet attrs, int defStyle) {
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
