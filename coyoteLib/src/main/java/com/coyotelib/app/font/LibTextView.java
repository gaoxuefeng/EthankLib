package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LibTextView extends TextView {

	public LibTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// if (!this.isInEditMode())
		// setEnabled(true);
		try {

			Typeface typeface = FontConstance.getTypeface(context);
			if (typeface != null) {
				super.setTypeface(typeface);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public LibTextView(Context context) {
		this(context, null);
	}

	public LibTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

}
