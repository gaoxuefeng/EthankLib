package cn.com.ethank.refresh;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by dddd on 2016/3/23.
 */
public class PtrSelfFrameLayout extends PtrFrameLayout{

        private MaterialHeader mPtrClassicHeader;

        public PtrSelfFrameLayout(Context context) {
            super(context);
            this.initViews();
        }

        public PtrSelfFrameLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.initViews();
        }

        public PtrSelfFrameLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.initViews();
        }

        private void initViews() {
            this.mPtrClassicHeader = new MaterialHeader(this.getContext());
            this.setHeaderView(this.mPtrClassicHeader);
            this.addPtrUIHandler(this.mPtrClassicHeader);
        }

        public MaterialHeader getHeader() {
            return this.mPtrClassicHeader;
        }

    }
