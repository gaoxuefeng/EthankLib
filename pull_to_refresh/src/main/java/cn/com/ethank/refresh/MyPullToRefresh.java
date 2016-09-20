package cn.com.ethank.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import in.srain.cube.views.loadmore.ListViewScrollListener;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreDefaultFooterView;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by dddd on 2016/2/19.
 */
public class MyPullToRefresh extends PtrSelfFrameLayout {

    private LoadMoreListViewContainer moreListViewContainer;
    private ListView listView;
    private OnRefreshListener mOnRefreshListener;
    private boolean canLoadMore = false;//默认不能加载更多
    private boolean isLoadingMore = false;
    private LoadMoreDefaultFooterView footerView;

    public MyPullToRefresh(Context context) {
        super(context);
        initView(null);
    }


    public MyPullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public MyPullToRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        inflate(getContext(), R.layout.refresh_layout, this);
        moreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_contain);
        listView = (ListView) findViewById(R.id.load_more_listview);
        listView.setFooterDividersEnabled(false);
        if (getBackground() != null) {
            listView.setBackground(getBackground());
            if (getChildCount() >= 2) {
                getChildAt(1).setBackground(getBackground());
            }
        }

        initListViewAttrs(attrs);
        initHandler();
        setCanLoadMore(false);
        moreListViewContainer.setShowLoadingForFirstPage(false);
        moreListViewContainer.setListScroolListener(new ListViewScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        moreListViewContainer.setListScroolListener(new ListViewScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (footerView != null && canLoadMore) {
                    if (firstVisibleItem == 0) {
                        footerView.setVisibility(GONE);
                    } else {
                        footerView.setVisibility(VISIBLE);
                        if (!isNetConenct()&&((TextView) (footerView.findViewById(R.id.cube_views_load_more_default_footer_text_view))).getText().toString().contains("加载中")) {
                            refreshComplete(true);
                        }
                    }
                }
            }
        });
    }

    private void initListViewAttrs(AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.MyPullToRefresh);
            if (a != null) {
                final Drawable d = a.getDrawable(R.styleable.MyPullToRefresh_myDivider);
                if (d != null) {
                    // If a divider is specified use its intrinsic height for divider height
                    listView.setDivider(d);
                }
                final int dividerHeight = a.getDimensionPixelSize(
                        R.styleable.MyPullToRefresh_myDividerHeight, 0);
                if (dividerHeight != 0) {
                    listView.setDividerHeight(dividerHeight);
                    listView.setFooterDividersEnabled(false);
                }
            }
        }
    }

    private void initHandler() {
        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mOnRefreshListener != null) {
                    if (isLoadingMore && canLoadMore) {
                        MyPullToRefresh.super.refreshComplete();
                    } else {
                        mOnRefreshListener.onRefresh(MyPullToRefresh.this);
                    }
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, listView, header);
            }
        });
        moreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                // 请求下一页数据
                if (mOnRefreshListener != null && canLoadMore && !isRefreshing()) {
                    if (MyPullToRefresh.super.isRefreshing()) {
                        loadMoreContainer.loadMoreFinish(false, true);
                    } else {
                        mOnRefreshListener.onLoadMore(MyPullToRefresh.this);
                    }
                }
            }

        });
    }

    public ListView getListView() {
        return listView;
    }

    public final void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void refreshComplete(boolean hasMoreData) {
        super.refreshComplete();
        moreListViewContainer.loadMoreFinish(false, hasMoreData);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    /**
     * 设置是否能加载更多
     *
     * @param canLoadMore
     */
    public void setCanLoadMore(boolean canLoadMore) {
        if (canLoadMore) {
            footerView = new LoadMoreDefaultFooterView(this.getContext());
            footerView.setVisibility(GONE);
            moreListViewContainer.setLoadMoreView(footerView);
            moreListViewContainer.setLoadMoreUIHandler(footerView);
        } else {
            removeAllFootView();
        }
        this.canLoadMore = canLoadMore;
        if (footerView != null && getBackground() != null) {
            footerView.setBackground(getBackground());
        }
    }

    private final void removeAllFootView() {
        for (int i = 0; i < listView.getFooterViewsCount(); i++) {
            try {
                listView.removeFooterView(listView.getChildAt(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isRefreshing() {
        // 父控件没有加载更多,需要判断是否正在加载更多或者下拉刷新
        return (canLoadMore && isLoadingMore) || super.isRefreshing();
    }

    /**
     * 加载完成,小于10说明是最后一页,请求失败怎不管是不是最后一页 传-1
     *
     * @param addCount
     */
    public void refreshComplete(int addCount) {
        try {
            if (addCount < 0) {
                if (((TextView) (footerView.findViewById(R.id.cube_views_load_more_default_footer_text_view))).getText().toString().contains("加载中")) {
                    refreshComplete(true);
                } else {
                    refreshComplete();
                }
            } else if (addCount < 10) {
                refreshComplete(false);
            } else {
                refreshComplete(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPullBackground(int drawid) {
        listView.setBackgroundResource(drawid);
    }

    public boolean isNetConenct() {
        return true;
    }
}
