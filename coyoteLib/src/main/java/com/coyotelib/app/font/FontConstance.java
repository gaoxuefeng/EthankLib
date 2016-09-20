package com.coyotelib.app.font;

import android.content.Context;
import android.graphics.Typeface;

public class FontConstance {
    private static Typeface typeface;
    private static Typeface priceTypeface;
//    private static Typeface boldTypeface;

    private static void initText(Context context) {
//        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nomal.ttf");
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/PingFang_Regular.ttf");
//            Typeface.class.getField("DEFAULT").setAccessible(true);
//            Typeface.class.getField("DEFAULT_BOLD").setAccessible(true);
//            Typeface.class.getField("DEFAULT").set(null, typeface);
//            if (boldTypeface == null) {
//                boldTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/PingFang_Medium.ttf");
//            }
//            if (boldTypeface != null) {
//                Typeface.class.getField("DEFAULT_BOLD").set(null, boldTypeface);
//            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initPriceText(Context context) {
        priceTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/price.ttf");
        try {
//            Typeface.class.getField("DEFAULT").setAccessible(true);
//            Typeface.class.getField("DEFAULT_BOLD").setAccessible(true);
//            Typeface.class.getField("DEFAULT").set(null, priceTypeface);
//            Typeface.class.getField("DEFAULT_BOLD").set(null, priceTypeface);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Typeface getTypeface(Context context) {
        if (typeface == null) {
            initText(context);
        }
        return typeface;
    }

    public static Typeface getTypeface() {
        return typeface;
    }

    public static Typeface getPriceTypeface(Context context) {
        if (priceTypeface == null) {
            initPriceText(context);
        }
        return priceTypeface;
    }
}
