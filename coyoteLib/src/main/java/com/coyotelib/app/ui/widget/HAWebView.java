package com.coyotelib.app.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;

/**
 * Created by chenjishi on 14-1-11.
 */
@SuppressLint("SetJavaScriptEnabled")
public class HAWebView extends WebView {
	private Method mCavansIsHAAcced;
	private Method mViewSetLayerType;
	private boolean mSupportAccApi;
	public OnScrollChangedCallback mOnScrollChangedCallback;

    public HAWebView(final Context context) {
        super(context);
    }

    public HAWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
        initSetting(context);
    }

    private void initSetting(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        webSettings.setSupportMultipleWindows(true);
//		webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        String dir = context.getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);


        Class<?> settingsClazz = webSettings.getClass();

        try {
            Method method = settingsClazz.getDeclaredMethod("setDomStorageEnabled", boolean.class);
            method.invoke(webSettings, new Object[]{true});
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Method method = settingsClazz.getDeclaredMethod("setAppCacheMaxSize", long.class);
            method.invoke(webSettings, new Object[]{1024 * 1024 * 8});
        } catch (Exception e) {
            e.printStackTrace();
        }
        String appCachePath = context.getCacheDir().getAbsolutePath();

        try {
            Method method = settingsClazz.getDeclaredMethod("setAppCachePath", String.class);
            method.invoke(webSettings, appCachePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webSettings.setAllowFileAccess(true);
        try {
            Method method = settingsClazz.getDeclaredMethod("setAppCacheEnabled", boolean.class);
            method.invoke(webSettings, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initWebView() {
        try {
            mCavansIsHAAcced = Canvas.class.getMethod("isHardwareAccelerated");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mViewSetLayerType = View.class.getMethod("setLayerType", int.class, Paint.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSupportAccApi = Build.VERSION.SDK_INT >= 11 && mCavansIsHAAcced != null && mViewSetLayerType != null;

    }

    public HAWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//		try {
//			if (mSupportAccApi && (Boolean) mCavansIsHAAcced.invoke(canvas)) {
//				mViewSetLayerType.invoke(this, 1, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        super.onDraw(canvas);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);
        }
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the
     * webview
     */
    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
