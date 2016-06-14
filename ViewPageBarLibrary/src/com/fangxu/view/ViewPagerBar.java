package com.fangxu.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerBar extends HorizontalScrollView {

	// ArgbEvaluator colorEva;
	private Paint mlinePaint;
	/**
	 * 标题正常时的颜色
	 */
	private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
	/**
	 * 标题选中时的颜色
	 */
	private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;

	private int lineHeght = 5;// 底部线条高度
	private int InitTitleBarOffset = 300;// titlebar 允许活动的最小偏移�??

	// private int scrWidth;
	private ViewPager pager;
	private LinearLayout tabsContainer;
	private OnPageChangeListener pageChangeListener;
	float pageChangeOffsetRate = 0.0f;// 0~1; //滑动�?
	int mCurrentPosition = 0;// 滑动趋向位置 不要真正的当前位�?
	float scrollTate = 0.0f;

	public ViewPagerBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setHorizontalScrollBarEnabled(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		mlinePaint = new Paint();
		mlinePaint.setAntiAlias(true);
		mlinePaint.setColor(Color.parseColor("#ff515151"));
		mlinePaint.setStyle(Style.FILL);
		mlinePaint.setPathEffect(new CornerPathEffect(3));
		// scrWidth = getScreenWidth();
	}

	public void setOnPageChangedListener(OnPageChangeListener pageChangeListener) {
		this.pageChangeListener = pageChangeListener;
	}

	int selectedItem = -1;

	/***
	 * 界面滑动进度�? [0~1]
	 * 
	 * @return
	 */
	private float getScrollrate() {
		float rate = 0;

		if (pageChangeOffsetRate == 0) {
			rate = 1.0f;
			return 1.0f;
		} else {
			if (mCurrentPosition == selectedItem) {
				rate = pageChangeOffsetRate;
			} else {
				rate = 1.0f - pageChangeOffsetRate;
			}

		}
		// Log.e("百分�?", rate + "");
		return rate;
	}

	public void setViewPager(final ViewPager pager) {
		setBackgroundColor(Color.GREEN);
		this.pager = pager;

		this.pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (pageChangeListener != null) {
					pageChangeListener.onPageSelected(arg0);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				// Log.e("show", "position=" + arg0 + " " +
				// "positionoffsetRate=" + arg1 + " " + "positionOffset=" +
				// arg2);
				if (pageChangeListener != null) {
					pageChangeListener.onPageScrolled(arg0, arg1, arg2);
				}
				mCurrentPosition = arg0;
				pageChangeOffsetRate = arg1;
				adjustScroll();
				invalidate();
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if (null != pageChangeListener) {
					pageChangeListener.onPageScrollStateChanged(arg0);
				}
				if (arg0 == 0) {
					selectedItem = pager.getCurrentItem();
				}
			}
		});

		makeTabItem();
		invalidate();
	}

	/**
	 * 自动调整titlebar 偏移
	 */
	private void adjustScroll() {
		// TODO Auto-generated method stub
		int scrrenWidth = getScreenWidth();

		int scrollOffset = (int) (tabsContainer.getChildAt(mCurrentPosition).getLeft() - InitTitleBarOffset
				+ tabsContainer.getChildAt(mCurrentPosition).getWidth() * pageChangeOffsetRate);

		if (tabsContainer.getChildAt(mCurrentPosition).getRight() >= InitTitleBarOffset
				&& scrollOffset <= (tabsContainer.getWidth() - scrrenWidth) && scrollOffset >= 0) {
			scrollTo(scrollOffset, 0);
		}

	}

	private void makeTabItem() {
		int count = pager.getAdapter().getCount();
		tabsContainer.removeAllViews();

		for (int i = 0; i < count; i++) {
			TextView tvTextView = generateTextView(pager.getAdapter().getPageTitle(i).toString());
			final int j = i;
			tvTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pager.setCurrentItem(j, true);
				}
			});
			tabsContainer.addView(tvTextView);
		}

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.save();
		View currentTab = tabsContainer.getChildAt(mCurrentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right

		// between current and next tab
		if (pageChangeOffsetRate > 0f && mCurrentPosition < pager.getAdapter().getCount() - 1) {

			View nextTab = tabsContainer.getChildAt(mCurrentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			// lineLeft = (pageChangeOffsetRate * nextTabLeft + (1f -
			// pageChangeOffsetRate) * lineLeft);
			lineLeft = lineLeft + pageChangeOffsetRate * (nextTabLeft - lineLeft);
			lineRight = (pageChangeOffsetRate * nextTabRight + (1f - pageChangeOffsetRate) * lineRight);
		}
		// canvas.drawRect(lineLeft, getHeight() - lineHeght, lineRight,
		// getHeight(), mlinePaint);

		// mlinePaint.setColor((Integer) evaluate(getScrollrate(),
		// mlinePaint.getColor(), nextColor));
		mlinePaint.setColor(Color.GRAY);
		if (pageChangeOffsetRate == 0) {
			nextColor = getRandomColor();
		}
		canvas.drawRect(lineLeft, 0, lineRight, getHeight(), mlinePaint);
		canvas.restore();
		super.dispatchDraw(canvas);
	}

	int nextColor = -1;

	private int getRandomColor() {
		return Color.rgb(getRanNum(), getRanNum(), getRanNum());

	}

	private int getRanNum() {
		return new Random().nextInt(255);
	}

	/**
	 * 获得屏幕的宽�?
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private TextView generateTextView(String text) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		// lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setSingleLine(true);
		tv.setPadding(30, 0, 30, 0);
		tv.setTextColor(Color.BLACK);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);
		return tv;
	}

	/**
	 * This function returns the calculated in-between value for a color given
	 * integers that represent the start and end values in the four bytes of the
	 * 32-bit int. Each channel is separately linearly interpolated and the
	 * resulting calculated values are recombined into the return value.
	 *
	 * @param fraction
	 *            The fraction from the starting to the ending values
	 * @param startValue
	 *            A 32-bit int value representing colors in the separate bytes
	 *            of the parameter
	 * @param endValue
	 *            A 32-bit int value representing colors in the separate bytes
	 *            of the parameter
	 * @return A value that is calculated to be the linearly interpolated
	 *         result, derived by separating the start and end values into
	 *         separate color channels and interpolating each one separately,
	 *         recombining the resulting values in the same way.
	 */
	public Object evaluate(float fraction, Object startValue, Object endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}

}
