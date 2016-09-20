package cn.com.ethank.refresh;

/**
 * Created by dddd on 2016/2/19.
 */
public interface OnRefreshListener {
    public void onRefresh(MyPullToRefresh pullToRefresh);

    public void onLoadMore(MyPullToRefresh pullToRefresh);
}
