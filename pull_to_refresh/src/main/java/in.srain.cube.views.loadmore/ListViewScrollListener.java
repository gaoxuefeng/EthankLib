package in.srain.cube.views.loadmore;

import android.widget.AbsListView;

/**
 * Created by dddd on 2016/5/6.
 */
public interface ListViewScrollListener {
  void   onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
