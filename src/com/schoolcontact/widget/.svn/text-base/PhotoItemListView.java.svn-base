package com.schoolcontact.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 
 * @author Star
 * @description 解决了嵌套LISTVIEW 冲突问题
 */
public class PhotoItemListView extends ListView implements OnScrollListener {

	private int mMaxItemCount;
	private float mLastY = -1; // save event y
	private IPhotoItemListViewListener mIPhotoItemListViewListener;
	private Scroller mScroller;
	private XListViewFooter mFooterView;
	private boolean mIsFooterReady = false;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private final static int SCROLL_DURATION = 400;
	private OnPhotoItemScrollListener mOnPhotoItemScrollListener;
	private static final int SCROLLBACK_FOOTER = 1;
	private int mScrollBack;
	private XListViewHeader mHeaderView;
	private static final int PULL_LOAD_MORE_DELTA = 50;
	private final static float OFFSET_RADIO = 1.8f;

	public IPhotoItemListViewListener getmIPhotoItemListViewListener() {
		return mIPhotoItemListViewListener;
	}

	public void setmIPhotoItemListViewListener(
			IPhotoItemListViewListener mIPhotoItemListViewListener) {
		this.mIPhotoItemListViewListener = mIPhotoItemListViewListener;
	}

	public PhotoItemListView(Context context) {
		super(context);
		onInit(context);
	}

	public PhotoItemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInit(context);
	}

	public PhotoItemListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		onInit(context);

	}

	private void onInit(Context context) {

		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);

		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	private void invokeOnScrolling() {
		if (mOnPhotoItemScrollListener instanceof OnPhotoItemScrollListener) {
			OnPhotoItemScrollListener l = (OnPhotoItemScrollListener) mOnPhotoItemScrollListener;
			l.onPhotoItemScrolling(this);
		}
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mIPhotoItemListViewListener != null) {
			mIPhotoItemListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getLastVisiblePosition() == mMaxItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;

		default:
			mLastY = -1; // reset
			if (getLastVisiblePosition() == mMaxItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_FOOTER) {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mOnPhotoItemScrollListener != null) {
			mOnPhotoItemScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mMaxItemCount = totalItemCount;
		if (mOnPhotoItemScrollListener != null) {
			mOnPhotoItemScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

	}

	public interface OnPhotoItemScrollListener extends OnScrollListener {
		public void onPhotoItemScrolling(View view);
	}

	public interface IPhotoItemListViewListener {
		// public void onRefresh();
		public void onLoadMore();
	}
}
